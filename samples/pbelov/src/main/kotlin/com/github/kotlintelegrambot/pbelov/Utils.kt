package com.github.kotlintelegrambot.pbelov

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Update

class Utils {
    fun sendMessage(bot: Bot, update: Update, text: String) {
        bot.sendMessage(chatId = ChatId.fromId(update.message!!.chat.id), text = text)
    }

    fun sendReply(bot: Bot, update: Update, text: String) {
        bot.sendMessage(chatId = ChatId.fromId(update.message!!.chat.id), replyToMessageId = update.message!!.messageId, text = text)
    }
}
