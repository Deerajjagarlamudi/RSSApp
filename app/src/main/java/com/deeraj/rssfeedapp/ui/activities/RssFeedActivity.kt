package com.deeraj.rssfeedapp.ui.activities

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberAsyncImagePainter
import com.deeraj.rssfeedapp.db.schemas.RssFeed
import com.deeraj.rssfeedapp.ui.activities.ui.theme.RSSAppTheme
import com.deeraj.rssfeedapp.utils.LocationHelper
import com.deeraj.rssfeedapp.utils.Resources
import com.deeraj.rssfeedapp.viewmodels.MainViewModel
import com.deeraj.rssfeedapp.viewmodels.RoomViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RssFeedActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private val roomViewmodel: RoomViewmodel by viewModels()

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                val helper = LocationHelper(this)
                helper.getCityFromLocation { city ->
                    Log.d("City", "Current city is $city")
                }
            } else {
                Log.e("Location", "Permission denied")
            }
        }


        locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)


        val identifier = intent.getStringExtra("identifier") ?: ""
        // Fetch and save articles
        mainViewModel.getArticles(identifier)
        lifecycleScope.launch {
            mainViewModel.articles.collectLatest { state ->
                if (state is Resources.Success<*>) {
                    state.data?.let { list ->
                        if (list.isNotEmpty()) {
                            val rssFeedList = list.map {
                                RssFeed(
                                    title = it.title.orEmpty(),
                                    description = it.description.orEmpty(),
                                    pubDate = it.pubDate.orEmpty(),
                                    link = it.link.orEmpty(),
                                    identifierUrl = identifier,
                                    imageUrl = it.imageUrl,
                                    category = it.category,
                                    interests = it.interests,
                                    location = it.location
                                )
                            }
                            roomViewmodel.saveArticles(rssFeedList)
                        }
                    }
                }
            }
        }

        // UI
        setContent {
            RSSAppTheme(false) {
                val allArticles by roomViewmodel.getAllArticles.collectAsState()
                var searchQuery by remember { mutableStateOf("") }

                val relevantArticles = allArticles.filter { it.identifierUrl == identifier }

                val filteredArticles = remember(searchQuery, relevantArticles) {
                    val base = if (searchQuery.isBlank()) {
                        relevantArticles
                    } else {
                        relevantArticles.filter {
                            it.title.contains(
                                searchQuery,
                                ignoreCase = true
                            ) || it.description.contains(searchQuery, ignoreCase = true)
                        }
                    }

                    base.distinctBy { it.link }
                }

                Column(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Blue)
                        .statusBarsPadding()
                ) {
                    CustomToolbarWithBackButton("Articles"){
                        finish()
                    }

                    // Search Box
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search feeds...") },
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                            .fillMaxWidth()
                            .height(55.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White),
                        singleLine = true
                    )

                    LazyColumn(
                        Modifier
                            .background(Color.White)
                            .fillMaxSize()
                    ) {
                        items(filteredArticles) {
                            RssFeedCard(
                                title = it.title,
                                description = it.description,
                                pubDate = it.pubDate,
                                image = it.imageUrl.orEmpty(),
                                click = {
                                    try {
                                        startActivity(Intent(Intent.ACTION_VIEW, it.link.toUri()))
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RssFeedCard(
    title: String,
    description: String,
    pubDate: String,
    image: String,
    modifier: Modifier = Modifier,
    click: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { click() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = rememberAsyncImagePainter(if (image.isNullOrBlank()) "https://external-preview.redd.it/new-contraceptive-pill-endometriosis-treatment-and-ivf-drug-v0-Lv0fxE6V2bdlvgNJVVJElVGWurIIOuywie7RIHHHy48.jpg?width=640&crop=smart&auto=webp&s=bccdab8f886cde61ec7b5b6a93a35c17325e1766" else image),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .padding(start = 16.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.FillBounds
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = pubDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}


@Composable
fun CustomToolbarWithBackButton(
    title: String, click:()-> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.Blue), // or your theme color
        contentAlignment = Alignment.CenterStart
    ) {

        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.padding(start = 16.dp).size(24.dp).clickable{
                click()
            }
        )

        // Title centered
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )


    }
}

