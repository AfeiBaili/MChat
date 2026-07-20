package cn.afeibaili.mchat;

import cn.afeibaili.mchat.config.ClientConfig;
import cn.afeibaili.mchat.config.ConfigLoader;
import cn.afeibaili.mchat.message.MessageCallback;
import cn.afeibaili.mchat.message.MessageType;
import cn.afeibaili.mchat.socket.Client;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Optional;

@Mod(modid = MChat.MODID, version = Tags.VERSION, name = "MChat", acceptedMinecraftVersions = "[1.7.10]")
public class MChat {

    public static final String MODID = "mchat";
    public static final Logger logger = LogManager.getLogger(MODID);
    protected static MinecraftServer server = null;
    protected static Client client = null;
    protected static ClientConfig config = null;
    protected static HashMap<MessageType.Identifiers, Function1<MessageType, Unit>> map = new HashMap<>();

    public MChat() {
        MinecraftForge.EVENT_BUS.register(new MessageListener());

        map.put(MessageType.Identifiers.Text, (message) -> {
            sendTextToPlayer(message);
            return Unit.INSTANCE;
        });
        map.put(MessageType.Identifiers.Image, (message) -> {
            sendImageToPlayer(message);
            return Unit.INSTANCE;
        });
    }

    public static void startMchat() {
        Optional.ofNullable(client).ifPresent(Client::close);
        config = ConfigLoader.INSTANCE.loadClient();
        client = new Client(config, new MessageCallback(map, (m, s) -> Unit.INSTANCE));
        Optional.of(client).ifPresent(Client::connect);
        logger.info("客户端已就绪");
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        server = event.getServer();
        Command.registerCommand(event);
        startMchat();
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        Optional.ofNullable(client).ifPresent(Client::close);
    }

    private void sendTextToPlayer(MessageType message) {
        try {
            IChatComponent chatStyle = new ChatComponentText(message.getSource() + ": " + message.getContent()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY));
            server.getConfigurationManager().sendChatMsg(chatStyle);
        } catch (Exception ignored) {
        }
    }

    private void sendImageToPlayer(MessageType message) {
        try {
            IChatComponent chatStyle = new ChatComponentText(message.getSource() + ": ").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY))
                .appendSibling(new ChatComponentText(message.getContent()).setChatStyle(
                    new ChatStyle().setColor(EnumChatFormatting.BLUE)
                        .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("打开图片链接")))
                        .setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, message.getContent()))));

            server.getConfigurationManager().sendChatMsg(chatStyle);
        } catch (Exception ignored) {
        }
    }
}
