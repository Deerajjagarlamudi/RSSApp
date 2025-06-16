package com.deeraj.rssfeedapp.utils


import com.deeraj.rssfeedapp.models.RssItem
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

object XMLParser {


    fun parseRssWithJsoup(rssUrl: String): List<RssItem> {
        val rssItems = mutableListOf<RssItem>()

        val doc: Document = Jsoup.connect(rssUrl).get()
        val items: Elements = doc.select("item")

        for (item in items) {
            val title = item.selectFirst("title")?.text().orEmpty()
            val description = item.selectFirst("description")?.text().orEmpty()
            val category = item.selectFirst("category")?.text().orEmpty()
            val location = item.selectFirst("location")?.text().orEmpty()
            val interests = item.selectFirst("interests")?.text().orEmpty()
            val pubDate = item.selectFirst("pubDate")?.text().orEmpty()
            val link = item.selectFirst("link")?.text().orEmpty()

            // Parse image URL from <media:thumbnail> or embedded <img> in <description>
            var imageUrl = item.selectFirst("media|thumbnail")?.attr("url").orEmpty()

            // Fallback: Extract image from HTML in description
            if (imageUrl.isEmpty()) {
                val descDoc = Jsoup.parse(description)
                imageUrl = descDoc.selectFirst("img")?.attr("src").orEmpty()
            }

            rssItems.add(
                RssItem(
                    title = title,
                    description = Jsoup.parse(description).text(), // remove HTML tags
                    pubDate = pubDate,
                    link = link,
                    imageUrl = imageUrl,
                    category = category,
                    location = location,
                    interests = interests
                )
            )
        }

        return rssItems
    }





}