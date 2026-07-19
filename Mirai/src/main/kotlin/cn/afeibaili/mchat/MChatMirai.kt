package cn.afeibaili.mchat

import cn.afeibaili.mchat.Listener.bot
import cn.afeibaili.mchat.message.MessageCallback
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
    }) {
    override fun onEnable() {
        Listener.load()
        server.start()
        logger.info("MChat已就绪")
    }

    val config = Config.load()

    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    val server: Server =
        Server(
            config,
            MessageCallback(
                mapOf(
                    MessageType.Identifiers.Text to { type ->
                        scope.launch {
                            config.groups.forEach {
                                bot?.groups?.get(it)?.sendMessage("${type.source}: ${type.content}")
                            }
                        }
                    }),
                onMessage = { message, socket ->
                    server.sendAll(message, socket)
                }
            ),
            onVerify = { Listener.sendMessage("${it.source}服务器已连接") })
}