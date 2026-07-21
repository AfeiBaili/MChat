package cn.afeibaili.mchat

import cn.afeibaili.mchat.MChatMirai.config
import cn.afeibaili.mchat.MChatMirai.server
import cn.afeibaili.mchat.message.MessageType
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.BotOnlineEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.content

object Listener {
    var bot: Bot? = null
    var channel = config.groups.joinToString(",")

    fun load() {
        GlobalEventChannel.filter { filterGroup(it) }.subscribeAlways<GroupMessageEvent> { event ->
            val groupName = event.group.name
            val nick = event.sender.nick

            event.message.filter { it.content.isNotBlank() }.forEach {
                if (it is Image) server.sendAll(
                    MessageType.Image(
                        "$groupName $nick", it.queryUrl()
                    )
                ) else {
                    server.sendAll(MessageType.Text("$groupName $nick", it.content, channel))
                }
            }
        }

        GlobalEventChannel.subscribeAlways<BotOnlineEvent> { event ->
            Listener.bot = event.bot
        }
    }

    suspend fun sendMessage(channel: String, text: String) {
        val channel: String = channel
        if (channel == "all") {
            config.groups.forEach {
                bot?.groups?.get(it)?.sendMessage(text)
            }
            return
        }

        val groupStrings: List<String> = channel.split(",")
        val longs: List<Long> = groupStrings.mapNotNull { it.toLongOrNull() }
        longs.forEach { if (it in config.groups) bot?.groups?.get(it)?.sendMessage(text) }
    }

    fun filterGroup(event: Event): Boolean {
        if (event !is GroupMessageEvent) return false
        return MChatMirai.config.groups.contains(event.group.id)
    }
}