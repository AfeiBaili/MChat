package cn.afeibaili.mchat

import cn.afeibaili.mchat.MChatMirai.server
import cn.afeibaili.mchat.message.MessageType
import kotlinx.coroutines.launch
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

    fun load() {
        GlobalEventChannel.filter { filterGroup(it) }.subscribeAlways<GroupMessageEvent> { event ->
            val groupName = event.group.name
            val nick = event.sender.nick
            val message = event.message.content
            server.sendAll(MessageType.Text("$groupName $nick", message, ""))
            event.message.forEach {
                if (it is Image) server.sendAll(
                    MessageType.Image(
                        "$groupName $nick",
                        it.queryUrl()
                    )
                )
            }
        }

        GlobalEventChannel.subscribeAlways<BotOnlineEvent> { event ->
            Listener.bot = event.bot
        }
    }

    fun sendMessage(message: String) {
        MChatMirai.scope.launch {
            MChatMirai.config.groups.forEach {
                bot?.groups[it]?.sendMessage(message)
            }
        }
    }

    fun filterGroup(event: Event): Boolean {
        if (event !is GroupMessageEvent) return false
        return MChatMirai.config.groups.contains(event.group.id)
    }
}