package cn.afeibaili.mchat;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = MChat.MODID, version = Tags.VERSION, name = "MChat", acceptedMinecraftVersions = "[1.7.10]")
public class MChat {

    public static final String MODID = "mchat";
    public static final Logger logger = LogManager.getLogger(MODID);

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {

    }
}
