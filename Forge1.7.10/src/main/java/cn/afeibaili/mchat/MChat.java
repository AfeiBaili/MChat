package cn.afeibaili.mchat;

import cn.afeibaili.mchat.config.ClientConfig;
import cn.afeibaili.mchat.config.ConfigLoader;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = MChat.MODID,
    version = Tags.VERSION,
    name = "MChat",
    acceptedMinecraftVersions = "[1.7.10]",
    acceptableRemoteVersions = "*",
    acceptableSaveVersions = "*"
)
public class MChat {

    public static final String MODID = "mchat";
    public static final Logger logger = LogManager.getLogger(MODID);
    public static final MChatClient client = new MChatClient(new ClientHandler());
    protected static MinecraftServer server = null;
    protected static ClientConfig config = null;

    public MChat() {
        MessageListener listener = new MessageListener();
        MinecraftForge.EVENT_BUS.register(listener);
        FMLCommonHandler.instance().bus().register(listener);
        config = ConfigLoader.INSTANCE.loadClient();
    }

    public static boolean restartMChat() {
        ClientConfig config = ConfigLoader.INSTANCE.loadClient();
        MChat.config = config;
        if (config == null) return false;
        MChat.client.restart(config);
        return true;
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        server = event.getServer();
        Command.registerCommand(event);
        client.start(config);
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        client.stop();
    }
}
