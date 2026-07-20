package online.afeibaili.mchat

import cn.afeibaili.mchat.message.MessageType
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.ServerChatEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import online.afeibaili.mchat.MChat.config


/**
 * # 消息监听器
 *
 * @author AfeiBaili
 * @version 2026/7/18 20:25
 */

class MessageListener {
    @SubscribeEvent
    fun onMessage(event: ServerChatEvent) {
        runCatching {
            MChat.client?.send(
                MessageType.Text(
                    "[${config!!.name}] ${event.username}",
                    event.message.string,
                    config!!.channel
                )
            )
        }
    }

    @SubscribeEvent
    fun onPlayerLoginIn(event: PlayerEvent.PlayerLoggedInEvent) {
        runCatching {
            MChat.client?.send(
                MessageType.Text(
                    "${event.entity.name.string}",
                    "加入了${config!!.name}",
                    config!!.channel
                )
            )
        }
    }

    @SubscribeEvent
    fun onPlayerLoginOut(event: PlayerEvent.PlayerLoggedOutEvent) {
        runCatching {
            MChat.client?.send(
                MessageType.Text(
                    "${event.entity.name.string}",
                    "退出了${config!!.name}",
                    config!!.channel
                )
            )
        }
    }
}