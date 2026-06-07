package com.example.eboraazule.data.remote

import android.util.Xml
import com.example.eboraazule.data.model.CulturalEvent
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

class TalaveraRssParser {
    fun parse(inputStream: InputStream): List<CulturalEvent> {
        inputStream.use {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(it, null)
            parser.nextTag()
            return readRss(parser)
        }
    }

    private fun readRss(parser: XmlPullParser): List<CulturalEvent> {
        val items = mutableListOf<CulturalEvent>()
        parser.require(XmlPullParser.START_TAG, null, "rss")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            if (parser.name == "channel") {
                items.addAll(readChannel(parser))
            } else {
                skip(parser)
            }
        }
        return items
    }

    private fun readChannel(parser: XmlPullParser): List<CulturalEvent> {
        val items = mutableListOf<CulturalEvent>()
        parser.require(XmlPullParser.START_TAG, null, "channel")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            if (parser.name == "item") {
                items.add(readItem(parser))
            } else {
                skip(parser)
            }
        }
        return items
    }

    private fun readItem(parser: XmlPullParser): CulturalEvent {
        parser.require(XmlPullParser.START_TAG, null, "item")
        var title = ""
        var link = ""
        var description = ""
        var pubDate = ""
        var imageUrl = ""

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            when (parser.name) {
                "title" -> title = readText(parser)
                "link" -> link = readText(parser)
                "description" -> description = stripHtml(readText(parser))
                "pubDate" -> pubDate = readText(parser)
                "enclosure" -> {
                    imageUrl = parser.getAttributeValue(null, "url") ?: ""
                    parser.nextTag()
                }
                else -> skip(parser)
            }
        }
        
        return CulturalEvent(
            id = link, // Usamos el link como ID único
            title = title,
            date = pubDate,
            location = "Ayuntamiento de Talavera",
            imageUrl = imageUrl.ifEmpty { "https://www.talavera.es/wp-content/uploads/2023/07/logo-talavera.png" },
            description = description
        )
    }

    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }

    private fun stripHtml(html: String): String {
        return html.replace(Regex("<[^>]*>"), "").trim()
    }
}
