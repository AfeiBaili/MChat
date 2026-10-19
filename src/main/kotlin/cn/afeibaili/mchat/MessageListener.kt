package cn.afeibaili.mchat

import cn.afeibaili.mchat.Main.config
import cn.afeibaili.mchat.network.GlobalChannelSet
import cn.afeibaili.mchat.network.message.MessageType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.BotOnlineEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.content


/**
 * # 消息监听器
 *
 * @author AfeiBaili
 * @version 2026/10/19 23:18
 */

object MessageListener {
    var bot: Bot? = null
    val scope = CoroutineScope(Dispatchers.IO)

    fun load() {
        GlobalEventChannel.subscribeAlways<BotOnlineEvent> { event ->
            MessageListener.bot = event.bot
        }

        GlobalEventChannel.filter { filterGroup(it) }.subscribeAlways<GroupMessageEvent> { event ->
            val groupName = event.group.name
            val nick = event.sender.nick

            event.message.filter { it.content.isNotBlank() }.forEach {
                if (it is Image)
                    sendToClient(MessageType.Image("$groupName $nick", it.queryUrl()))
                else
                    sendToClient(MessageType.Text("$groupName $nick", it.content))
            }
        }

    }

    fun filterGroup(event: Event): Boolean {
        if (event !is GroupMessageEvent) return false
        return Main.config.groups.contains(event.group.id)
    }

    fun sendToGroup(message: MessageType) = scope.launch {
        val text = "${message.source}: ${message.content}"
        val channel: String = message.channel
        if (channel == "all") {
            config.groups.forEach {
                bot?.groups?.get(it)?.sendMessage(text)
            }
            return@launch
        }

        val groupStrings: List<String> = channel.split(",")
        val longs: List<Long> = groupStrings.mapNotNull { it.toLongOrNull() }
        longs.forEach { if (it in config.groups) bot?.groups?.get(it)?.sendMessage(text) }
    }

    fun sendToGroup(message: String) = scope.launch {
        config.groups.forEach { bot?.groups?.get(it)?.sendMessage(message) }
    }

    fun sendToClient(message: MessageType) {
        GlobalChannelSet.channels.writeAndFlush(message)
    }
}