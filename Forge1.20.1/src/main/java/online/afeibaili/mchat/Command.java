package online.afeibaili.mchat;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * # 命令注册
 *
 * @author AfeiBaili
 * @version 2026/7/19 22:10
 */

public class Command {
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("mchat")
                        .requires((it) -> it.hasPermission(3))
                        .then(Commands.literal("reload").executes((it) -> {
                            MChat.startMchat();
                            return 1;
                        }))
                        .then(Commands.literal("colors").then(Commands.argument("hexcolor", StringArgumentType.greedyString())
                                .executes((context) -> {
                                    String hexcolor = StringArgumentType.getString(context, "hexcolor");
                                    try {
                                        MChat.style = Style.EMPTY.withColor(TextColor.parseColor(hexcolor));
                                    } catch (Exception ignored) {
                                        context.getSource().sendSystemMessage(Component.literal("failed, unknown hex color").withStyle(ChatFormatting.GRAY));
                                        return 0;
                                    }
                                    context.getSource().sendSystemMessage(Component.literal("succeed, update chat color for" + hexcolor).withStyle(ChatFormatting.WHITE));
                                    return 1;
                                }))));
    }
}
