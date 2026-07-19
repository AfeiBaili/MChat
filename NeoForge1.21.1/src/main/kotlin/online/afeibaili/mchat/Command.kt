package online.afeibaili.mchat

import com.mojang.brigadier.arguments.StringArgumentType
import net.minecraft.ChatFormatting
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.RegisterCommandsEvent
import online.afeibaili.mchat.MChat.style


/**
 * # 命令事件
 *
 * @author AfeiBaili
 * @version 2026/7/19 15:11
 */

class Command {
    @SubscribeEvent
    fun registerCommands(register: RegisterCommandsEvent) {
        register.dispatcher.register(
            Commands.literal("mchat")
                .requires { it.hasPermission(3) }
                .then(
                    Commands.literal("reload")
                        .executes {
                            MChat.startMchat()
                            1
                        }
                )
                .then(
                    Commands.literal("colors").then(
                        Commands.argument("hexcolor", StringArgumentType.greedyString())
                            .executes { context ->
                                val hexColorStr: String = StringArgumentType.getString(context, "hexcolor")
                                style = runCatching {
                                    Style.EMPTY.withColor(TextColor.parseColor(hexColorStr).result().get())
                                }.getOrElse {
                                    context.source.sendSystemMessage(
                                        Component
                                            .literal("failed, unknown hex color")
                                            .withStyle(ChatFormatting.RED)
                                    )
                                    return@executes 0
                                }
                                context.source.sendSystemMessage(
                                    Component
                                        .literal("succeed, update chat color for $hexColorStr")
                                        .withStyle(ChatFormatting.WHITE)
                                )
                                1
                            }
                    )
                )
        )
    }
}