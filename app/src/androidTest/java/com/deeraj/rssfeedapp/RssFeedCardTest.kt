package com.deeraj.rssfeedapp



import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.deeraj.rssfeedapp.db.schemas.RssFeed
import com.deeraj.rssfeedapp.ui.activities.RssFeedCard
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RssFeedCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun clickOnCard_opensIntent() {
        var clicked = false

        val article = RssFeed(
            title = "Test",
            description = "desc",
            pubDate = "2024-06-10",
            link = "https://news.com",
            identifierUrl = "id",
            imageUrl = "",
            category = null,
            interests = null,
            location = null
        )

        composeTestRule.setContent {
            RssFeedCard(
                title = article.title,
                description = article.description,
                pubDate = article.pubDate,
                image = "",
                click = { clicked = true }
            )
        }

        composeTestRule.onNodeWithText("Test").performClick()
        assertTrue(clicked)
    }
}
