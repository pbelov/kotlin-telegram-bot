package com.github.kotlintelegrambot.pbelov

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.entities.TelegramFile
import java.io.File
import java.util.*
import java.util.Calendar.*
import kotlin.collections.HashMap
import kotlin.random.Random

fun main() {

    val bot = bot {

        token = readFile("token")

        val todayMap: HashMap<Long, String> = HashMap()
        val prevDayMap : HashMap<Long, String> = HashMap()

        dispatch {
            val cmdHandler = CmdHandler()
            text {
                val today = getDateAsString()
                val prevDay = prevDayMap[message.chat.id]
                todayMap[message.chat.id] = today
                if (today != prevDay) {
                    prevDayMap[message.chat.id] = todayMap.getValue(message.chat.id)

                    when (getTime()) {
                        in 6..11 -> {
                            bot.sendMessage(
                                chatId = ChatId.fromId(message.chat.id),
                                replyToMessageId = update.message!!.messageId,
                                text = (mornings[Random.nextInt(mornings.size)]) + " " + weekDays[getWeekDay()])
                        }
                        in 18..23 -> {
                            bot.sendMessage(
                                chatId = ChatId.fromId(message.chat.id),
                                replyToMessageId = update.message!!.messageId,
                                text = (evenings[Random.nextInt(evenings.size)]) + " " + weekDays[getWeekDay()])
                        }
                        in 12..17 -> {
                            bot.sendMessage(
                                chatId = ChatId.fromId(message.chat.id),
                                replyToMessageId = update.message!!.messageId,
                                text = (middays[Random.nextInt(middays.size)]) + " " + weekDays[getWeekDay()])
                        }
                        in 0..5 -> {
                            bot.sendMessage(
                                chatId = ChatId.fromId(message.chat.id),
                                replyToMessageId = update.message!!.messageId,
                                text = (nights[Random.nextInt(nights.size)]) + " " + weekDays[getWeekDay()])
                        }
                        else -> {
                            bot.sendMessage(
                                chatId = ChatId.fromId(message.chat.id),
                                replyToMessageId = update.message!!.messageId,
                                text = "ШТА")
                        }
                    }
                }

                println("[${message.chat.title}] ${message.from}: ${message.text}")
            }

            command("start") {
                // TODO
                bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "hello there")
            }

            command("cny") {
                cmdHandler.cmdCNY(bot, update)
            }

            command("usd") {
                cmdHandler.cmdUSD(bot, update)
            }

            command("eur") {
                cmdHandler.cmdEUR(bot, update)
            }

            command("ping") {
                bot.sendMessage(chatId = ChatId.fromId(message.chat.id), replyToMessageId = update.message!!.messageId, text = "pong")
            }

            command("day") {
                bot.sendPhoto(chatId = ChatId.fromId(message.chat.id), TelegramFile.ByUrl("https://www.calend.ru/img/export/informer.png?" + getDateAsString()))
            }

            command("cat") {
                bot.sendPhoto(chatId = ChatId.fromId(message.chat.id), TelegramFile.ByUrl("https://thiscatdoesnotexist.com/?" + System.currentTimeMillis()))
            }

            command("me") {
                bot.deleteMessage(chatId = ChatId.fromId(message.chat.id), messageId = update.message!!.messageId)
                bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = ((message.from?.firstName ?: "") + " " + (message.from?.lastName
                    ?: "") + " " + args.joinToString(separator = " ")), parseMode = ParseMode.MARKDOWN_V2)
            }

            text("бот, утро?") {
                val hour = getTime()
                if (hour in 6..11) {
                    bot.sendMessage(chatId = ChatId.fromId(message.chat.id), replyToMessageId = update.message!!.messageId, text = "д.")
                } else {
                    bot.sendMessage(chatId = ChatId.fromId(message.chat.id), replyToMessageId = update.message!!.messageId, text = "нт.")
                }
            }

            text("бот, день?") {
                val hour = getTime()
                if (hour in 12..17) {
                    bot.sendMessage(
                        chatId = ChatId.fromId(message.chat.id),
                        replyToMessageId = update.message!!.messageId,
                        text = "д."
                    )
                } else {
                    bot.sendMessage(
                        chatId = ChatId.fromId(message.chat.id),
                        replyToMessageId = update.message!!.messageId,
                        text = "нт."
                    )
                }
            }

            text("бот, вечер?") {
                val hour = getTime()
                if (hour in 12..17) {
                    bot.sendMessage(
                        chatId = ChatId.fromId(message.chat.id),
                        replyToMessageId = update.message!!.messageId,
                        text = "д."
                    )
                } else {
                    bot.sendMessage(
                        chatId = ChatId.fromId(message.chat.id),
                        replyToMessageId = update.message!!.messageId,
                        text = "нт."
                    )
                }
            }

            text("бот, ночь?") {
                val hour = getTime()
                if (hour in 0..5) {
                    bot.sendMessage(chatId = ChatId.fromId(message.chat.id), replyToMessageId = update.message!!.messageId, text = "д.")
                } else {
                    bot.sendMessage(chatId = ChatId.fromId(message.chat.id), replyToMessageId = update.message!!.messageId, text = "нт.")
                }
            }

            text("42") {
                bot.sendMessage(chatId = ChatId.fromId(message.chat.id), replyToMessageId = update.message!!.messageId, text = "don't panic!")
            }

            message {
                println("message: " + (message.photo?.get(0)?.fileId))
//                bot.sendPhoto(chatId = ChatId.fromId(message.chat.id), TelegramFile.ByUrl(message.photo?.get(0)?.fileId!!))
            }
        }
    }

    bot.startPolling()
}

val mornings = arrayOf("утро", "утрота", "утрецо", "морнинг", "утрище")
val middays = arrayOf("день", "денёк", "днище", "деньцо")
val evenings = arrayOf("вечер", "вечерцо", "вечерок", "вечерочек", "вечерище")
val nights = arrayOf("ночь", "ночище", "ночка", "ночечка")

val weekDays = arrayOf("понедельника", "вторника", "среды", "четверга", "пятницы", "выходного дня", "выходного дня")

private fun getTime(): Int {
    val c = Calendar.getInstance()

    return c.get(Calendar.HOUR_OF_DAY)
}

private fun getWeekDay(): Int {
    val c = Calendar.getInstance()
    when (c.get(Calendar.DAY_OF_WEEK)) {
        MONDAY -> return 0
        TUESDAY -> return 1
        WEDNESDAY -> return 2
        THURSDAY -> return 3
        FRIDAY -> return 4
        SATURDAY -> return 5
        SUNDAY -> return 6
    }

    return -1
}

private fun getDateAsString() : String {
    val c = Calendar.getInstance()
    var day = c.get(Calendar.DAY_OF_MONTH).toString()
    if (day.length == 1) day = " $day"
    var month = c.get(Calendar.DAY_OF_MONTH).toString()
    if (month.length == 1) month = " $month"

    return c.get(Calendar.YEAR).toString() + month + day
}

fun handleText(bot: Bot, id: Long, text: String) {
    TODO("Not yet implemented")
}

private fun saveFile(filename: String, content: String) {
    val file = File(filename)
    file.printWriter().use { out -> out.write(content) }
}

private fun saveFile2(filename: String, map: Object) {
    val file = File(filename)
    file.printWriter().use { out -> out.print(map) }
}

private fun readFile(filename: String) : String {
    val file = File(filename)

    return file.readText()
}
