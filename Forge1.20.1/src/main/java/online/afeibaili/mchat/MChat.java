package online.afeibaili.mchat;

import cn.afeibaili.mchat.config.ClientConfig;
import cn.afeibaili.mchat.config.ConfigLoader;
import cn.afeibaili.mchat.message.MessageCallback;
import cn.afeibaili.mchat.message.MessageType;
import cn.afeibaili.mchat.socket.Client;
import com.mojang.logging.LogUtils;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import net.minecraft.network.chat.*;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Optional;

@Mod(MChat.MODID)
public class MChat {
    public static final String MODID = "mchat";
    private static final Logger logger = LogUtils.getLogger();
    protected static MinecraftServer server = null;
    protected static Client client = null;
    protected static ClientConfig config = null;
    protected static Style style = Style.EMPTY.withColor(TextColor.parseColor("#AAAAAA"));
    protected static Style hoverStyle = Style.EMPTY.withColor(TextColor.parseColor("#3D00A6")).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("打开图片链接")));

    protected static HashMap<MessageType.Identifiers, Function1<MessageType, Unit>> map = new HashMap<>();

    public MChat() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new MessageListener());
        MinecraftForge.EVENT_BUS.register(new Command());

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

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        server = event.getServer();
        startMchat();
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        Optional.of(client).ifPresent(Client::close);
    }

    private void sendTextToPlayer(MessageType message) {
        try {
            server.getPlayerList().getPlayers().forEach(player -> {
                player.sendSystemMessage(Component.literal(message.getSource() + ": " + message.getContent()).setStyle(style));
            });
        } catch (Exception ignored) {
        }
    }

    private void sendImageToPlayer(MessageType message) {
        try {
            server.getPlayerList().getPlayers().forEach(player -> {
                player.sendSystemMessage(Component.literal(message.getSource() + ": ").setStyle(style).append(Component.literal("[图片]").withStyle(hoverStyle.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, message.getContent())))));
            });
        } catch (Exception ignored) {
        }
    }
}
