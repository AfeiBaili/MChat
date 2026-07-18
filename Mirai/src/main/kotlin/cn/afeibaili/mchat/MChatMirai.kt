package cn.afeibaili.mchat

import cn.afeibaili.mchat.Listener.bot
import cn.afeibaili.mchat.message.MessageParser
import cn.afeibaili.mchat.message.MessageType
import cn.afeibaili.mchat.socket.Server
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

/**
 * # Mirai插件入口
 *
 * @author AfeiBaili
 * @version 2026/7/18 16:57
 */

object MChatMirai : KotlinPlugin(
    JvmPluginDescription(
        id = "cn.afeibaili.mchat",
        name = "MChat",
        version = "4.0.0",
    ) {
        author("AfeiBaili")
    }
) {
    override fun onEnable() {
        Listener.load()
        server.start()
        logger.info("MChat已就绪")
    }

    val config = Config.load()

    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    val server = Server(
        config,
        MessageParser(
            mapOf(
                MessageType.Identifiers.Text to { type ->
                    config.groups.forEach { group ->
                        scope.launch { bot?.groups?.get(group)?.sendMessage("${type.source}: ${type.content}") }
                    }
                }
            )
        )
    )
}