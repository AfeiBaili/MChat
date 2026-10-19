package cn.afeibaili.mchat

import cn.afeibaili.mchat.cipher.CipherProcessor
import cn.afeibaili.mchat.config.Config
import cn.afeibaili.mchat.network.MChatServer
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

/**
 * # 主入口
 *
 * @author AfeiBaili
 * @version 2026/10/19 22:09
 */

object Main : KotlinPlugin(
    JvmPluginDescription(
        id = "cn.afeibaili.mchat",
        name = "MChat",
        version = "5.0.0",
    ) {
        author("AfeiBaili")
    }) {

    val config = Config.load()
    val cipher: CipherProcessor = CipherProcessor(config.token)
    var server: MChatServer? = null

    override fun onEnable() {
        logger.info("MChat已就绪")
        MessageListener.load()
        server = MChatServer(config.port)
    }

    override fun onDisable() {
        server?.stop()
    }

    fun log(message: Any) {
        logger.info(message.toString())
    }
}