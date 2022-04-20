package com.github.kotlintelegrambot.pbelov

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Update
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class CmdHandler {
    fun cmdCNY(bot: Bot, update: Update) {
        var cnyRate = "12"

        val doc = Jsoup.connect("https://www.moex.com/en/derivatives/currency-rate.aspx?currency=CNY_RUB").get()
        val element = findElement(doc.allElements, "ctl00_PageContent_tbxCurrentRate")
        if (element != null) {
            cnyRate = element.text()
        }
        val result = bot.sendMessage(chatId = ChatId.fromId(update.message!!.chat.id), text = cnyRate)

        result.fold(
            {
                // do something here with the response
            },
            {
                // do something with the error
            }
        )
    }

    var myElement: Element? = null

    private fun findElement(elements: Elements, id: String) : Element? {
        for (element in elements) {
            return if (element.id().contains(id)) {
                myElement = element
                myElement
            } else {
                myElement = findElement(element.children(), id)
                if (myElement != null) {
                    myElement
                } else {
                    continue
                }
            }
        }

        return null
    }
}
