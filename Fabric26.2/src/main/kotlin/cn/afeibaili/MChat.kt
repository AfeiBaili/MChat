package cn.afeibaili

import cn.afeibaili.mchat.config.ClientConfig
import cn.afeibaili.mchat.config.ConfigLoader
import cn.afeibaili.mchat.message.MessageCallback
import cn.afeibaili.mchat.message.MessageType
import cn.afeibaili.mchat.socket.Client
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.Style
import net.minecraft.server.MinecraftServer
import org.slf4j.LoggerFactory
import java.net.URI
import java.util.HashMap

object MChat : ModInitializer {
    const val MOD_ID: String = "mchat"

    val logger = LoggerFactory.getLogger(MOD_ID)
    var server: MinecraftServer? = null
    var client: Client? = null
    var config: ClientConfig? = null
    var style: Style = Style.EMPTY.withColor(0xAAAAAA)
    val hoverStyle: Style =
        Style.EMPTY.withColor(0x3D00A6).withHoverEvent(HoverEvent.ShowText(Component.literal("打开图片链接")))
    val map = HashMap<MessageType.Identifiers, (MessageType) -> Unit>()

    override fun onInitialize() {
        Listener.register()
        Command.register()
        map.put(MessageType.Identifiers.Text, { message -> sendTextToPlayer(message) })
        map.put(MessageType.Identifiers.Image, { message -> sendImageToPlayer(message) })

        ServerLifecycleEvents.SERVER_STARTING.register { s ->
            server = s
            startMchat()
        }

        ServerLifecycleEvents.SERVER_STOPPING.register { client?.close() }
    }

    fun startMchat() {
        client?.close()
        config = ConfigLoader.loadClient()
        if (config == null) {
            logger.error("无法加载配置文件，请检查是否完整")
            return
        }
        client = Client(config!!, MessageCallback(map), config!!.channel)
        client?.connect()
        logger.info("客户端已就绪")
    }

    private fun sendTextToPlayer(message: MessageType) {
        server?.playerList?.players?.forEach { player ->
            player.sendSystemMessage(Component.literal("${message.source}: ${message.content}").setStyle(style))
        }
    }

    private fun sendImageToPlayer(message: MessageType) {
        server?.playerList?.players?.forEach { player ->
            player.sendSystemMessage(
                Component.literal("${message.source}: ").setStyle(style).append(
                    Component.literal("[图片]").withStyle(
                        hoverStyle.withClickEvent(ClickEvent.OpenUrl(URI(message.content)))
                    )
                )
            )
        }
    }
}
