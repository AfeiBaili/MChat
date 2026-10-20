package cn.afeibaili.mchat;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

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
                if (args.length < 1) {
                    IChatComponent chatStyle = new ChatComponentText("请输入相关命令: reload").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED));
                    sender.addChatMessage(chatStyle);
                    return;
                }
                String arg = args[0];
                if (arg.equals("reload")) {
                    boolean restartMChat = MChat.restartMChat();
                    if (!restartMChat) {
                        IChatComponent chatStyle = new ChatComponentText("重载MChat配置文件失败，请检查配置文件").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED));
                        sender.addChatMessage(chatStyle);
                    }
                }
                IChatComponent chatStyle = new ChatComponentText("重载MChat配置文件成功").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN));
                sender.addChatMessage(chatStyle);
            }
        });
    }
}
