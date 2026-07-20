package cn.afeibaili.mchat;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

/**
 * # 命令注册
 *
 * @author AfeiBaili
 * @version 2026/7/20 18:50
 */

public class Command {
    public static void registerCommand(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandBase() {
            @Override
            public String getCommandName() {
                return "mchat";
            }

            @Override
            public String getCommandUsage(ICommandSender sender) {
                return "/mchat <reload>";
            }

            @Override
            public void processCommand(ICommandSender sender, String[] args) {
                if (args.length < 1) return;
                String arg = args[0];
                if (arg.equals("reload")) {
                    MChat.startMchat();
                }
            }
        });
    }
}
