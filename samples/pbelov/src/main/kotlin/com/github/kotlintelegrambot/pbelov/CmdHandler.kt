package com.github.kotlintelegrambot.pbelov

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Update
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class CmdHandler {
    fun cmdCNY(bot: Bot, update: Update) {
        cmdRate(bot, update, "CNY")
    }

    fun cmdEUR(bot: Bot, update: Update) {
        cmdRate(bot, update, "EUR")
    }

    fun cmdUSD(bot: Bot, update: Update) {
        cmdRate(bot, update, "USD")
    }

    private fun cmdRate(bot: Bot, update: Update, currency: String) {
        bot.sendMessage(chatId = ChatId.fromId(update.message!!.chat.id), text = getRateFor(currency))
    }

    private fun getRateFor(currency: String): String {
        var rate = "";

        val doc =
            Jsoup.connect("https://www.moex.com/en/derivatives/currency-rate.aspx?currency={${currency.uppercase()}}_RUB")
                .get()
        val element = findElement(doc.allElements, "ctl00_PageContent_tbxCurrentRate")
        if (element != null) {
            rate = element.text()
        }

        return rate
    }

    var myElement: Element? = null

    private fun findElement(elements: Elements, id: String): Element? {
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
