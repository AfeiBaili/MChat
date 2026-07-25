package cn.afeibaili

import cn.afeibaili.MChat.config
import cn.afeibaili.mchat.message.MessageType
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents


/**
 * # 事件监听
 *
 * @author AfeiBaili
 * @version 2026/7/25 16:04
 */

object Listener {
    fun register() {
        ServerPlayerEvents.JOIN.register { player ->
            try {
                MChat.client?.send(
                    MessageType.Text(
                        "${player.name.string}",
                        "加入了${config!!.name}",
                        config!!.channel
                    )
                )
            } catch (_: Exception) {
            }
        }
        ServerPlayerEvents.LEAVE.register { player ->
            try {
                MChat.client?.send(
                    MessageType.Text(
                        "${player.name.string}",
                        "退出了${config!!.name}",
                        config!!.channel
                    )
                )
            } catch (_: Exception) {
            }
        }
        ServerMessageEvents.CHAT_MESSAGE.register { pcm, sp, cb ->
            try {
                MChat.client?.send(
                    MessageType.Text(
                        "[${config!!.name}] ${sp.name.string}",
                        pcm.signedContent(),
                        config!!.channel
                    )
                )
            } catch (_: Exception) {
            }
        }
    }
}