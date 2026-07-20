package online.afeibaili.mchat

import cn.afeibaili.mchat.config.ClientConfig
import cn.afeibaili.mchat.config.ConfigLoader
import cn.afeibaili.mchat.message.MessageCallback
import cn.afeibaili.mchat.message.MessageType
import cn.afeibaili.mchat.socket.Client
import net.minecraft.network.chat.*
import net.minecraft.server.MinecraftServer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.server.ServerStartingEvent
import net.neoforged.neoforge.event.server.ServerStoppingEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(MChat.ID)
object MChat {
    const val ID = "mchat"
    val logger: Logger = LogManager.getLogger(ID)
    var server: MinecraftServer? = null
    var client: Client? = null
    var config: ClientConfig? = null
    var style: Style = Style.EMPTY.withColor(TextColor.parseColor("#AAAAAA").result().get())
    val hoverStyle: Style = Style.EMPTY
        .withColor(TextColor.parseColor("#3D00A6").result().get())
        .withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("打开图片链接")))

    init {
        NeoForge.EVENT_BUS.register(this)
        NeoForge.EVENT_BUS.register(MessageListener())
        NeoForge.EVENT_BUS.register(Command())
    }

    fun startMchat() {
        client?.close()
        config = ConfigLoader.loadClient()
        if (config == null) {
            logger.error("无法加载配置文件，请检查是否完整")
            return
        }
        client = Client(
            config!!, MessageCallback(
                mapOf(
                    MessageType.Identifiers.Text to { sendTextToPlayer(it) },
                    MessageType.Identifiers.Image to { sendImageToPlayer(it) })
            ),
            config!!.channel
        )
        client?.connect()
        logger.info("客户端已就绪")
    }

    @SubscribeEvent
    fun onServerStarting(event: ServerStartingEvent) {
        server = event.server
        startMchat()
    }

    @SubscribeEvent
    fun onServerStopping(event: ServerStoppingEvent) {
        client?.close()
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
                        hoverStyle.withClickEvent(
                            ClickEvent(ClickEvent.Action.OPEN_URL, message.content)
                        )
                    )
                )
            )
        }
    }
}
