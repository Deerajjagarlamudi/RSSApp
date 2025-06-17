package com.deeraj.rssfeedapp

import com.deeraj.rssfeedapp.utils.XMLParser
import org.junit.Assert.assertEquals
import org.junit.Test


class XMLParserTest {

    private val sampleXml = """
        <rss>
            <channel>
                <item>
                    <title>Test Article</title>
                    <description><![CDATA[<p>This is a <b>test</b> description.</p><img src="https://img.com/test.jpg" />]]></description>
                    <link>https://example.com/test</link>
                    <pubDate>2025-06-11</pubDate>
                    <category>News</category>
                    <location>India</location>
                    <interests>Tech</interests>
                </item>
            </channel>
        </rss>
    """.trimIndent()

 

    @Test
    fun parseRssFromXml_handlesEmptyImage() {
        val xmlWithoutImage = """
            <rss>
                <channel>
                    <item>
                        <title>No Image</title>
                        <description>No <b>HTML</b></description>
                        <link>https://example.com/no-image</link>
                    </item>
                </channel>
            </rss>
        """.trimIndent()

        val result = XMLParser.parseRssFromXmlForTestCases(xmlWithoutImage)
        val item = result.first()


        assertEquals("No Image", item.title)
        assertEquals("No HTML", item.description)
        assertEquals("", item.imageUrl)
    }
}
