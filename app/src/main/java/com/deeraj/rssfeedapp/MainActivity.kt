package com.deeraj.rssfeedapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deeraj.rssfeedapp.ui.activities.RssFeedActivity
import com.deeraj.rssfeedapp.ui.theme.RSSAppTheme
import com.deeraj.rssfeedapp.viewmodels.RoomViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

 //   val mainViewModel: MainViewModel by viewModels()

    val roomViewmodel: RoomViewmodel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Delete Old Articles after 30 Days.
        roomViewmodel.deleteOldArticles()


        setContent {
            RSSAppTheme {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Blue)
                        .statusBarsPadding()
                ) {
                    CustomToolbar("Rss Feed App")

                    val stringList by roomViewmodel.getAllStrings.collectAsState()





                    if (stringList.isEmpty()){
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 56.dp)
                            .background(Color.White),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No RSS feeds added yet!",
                                fontSize = 20.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Click the '+' button to add your first feed.",
                                fontSize = 16.sp,
                                color = Color.LightGray
                            )

                        }
                    }else{
                        LazyColumn(
                            Modifier
                                .padding(top = 56.dp)
                                .background(Color.White)
                                .fillMaxSize()
                        ) {
                            items(stringList) {
                                RssLayout(it.value, onItemClick = {
                                    Intent(applicationContext, RssFeedActivity::class.java).apply {
                                        putExtra("identifier",it.value)
                                        startActivity(this)
                                    }

                                }, onDeleteButtonClicked = {
                                    roomViewmodel.deleteString(it.id)
                                })

                            }


                        }

                    }


                    var showDialog by remember { mutableStateOf(false) }

                    FloatingActionButton(
                        onClick = {
                            showDialog = true


                        }, modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(50.dp)

                    ) {
                        Icon(
                            imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color.White
                        )

                        if (showDialog) {
                            InputTextDialog(onDismiss = {
                                showDialog = false
                            }, onConfirm = {
                                showDialog = false
                                roomViewmodel.saveString(it)
                            })
                        }


                    }

                }
            }


      }
    }


}


@Composable
fun RssLayout(url: String, onDeleteButtonClicked: () -> Unit,onItemClick:()-> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.LightGray)
            .padding(16.dp).clickable{
                onItemClick()
            },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            url,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Outlined.Delete,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.clickable {
                onDeleteButtonClicked()
            })
    }

}


@Composable
fun CustomToolbar(
    title: String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.Blue), // or your theme color
        contentAlignment = Alignment.CenterStart
    ) {


        // Title Start
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp)
        )


    }
}


@Composable
fun InputTextDialog(
    onDismiss: () -> Unit, onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    AlertDialog(onDismissRequest = onDismiss, title = { Text("Enter RSS URL") }, text = {
        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("https://example.com/feed") })
    }, confirmButton = {
        TextButton(onClick = {
            onConfirm(text) // Call confirm and dismiss
        }) {
            Text("OK")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) { // Dismiss directly
            Text("Cancel")
        }
    })
}







