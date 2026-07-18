package cn.afeibaili.mchat

import cn.afeibaili.mchat.MChatMirai.server
import cn.afeibaili.mchat.message.MessageType
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.BotOnlineEvent
import net.mamoe.mirai.event.events.GroupMessageEvent

object Listener {
    var bot: Bot? = null

    fun load() {
        GlobalEventChannel.filter { filterGroup(it) }.subscribeAlways<GroupMessageEvent> { event ->
            val messageSender: Member = event.sender
            server.send(MessageType.Text(messageSender.nick, event.message.contentToString(), ""))
        }

        GlobalEventChannel.subscribeAlways<BotOnlineEvent> { event ->
            Listener.bot = event.bot
        }
    }

    fun filterGroup(event: Event): Boolean {
        if (event !is GroupMessageEvent) return false
        return MChatMirai.config.groups.contains(event.group.id)
    }
}