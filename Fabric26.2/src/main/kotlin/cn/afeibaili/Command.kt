package cn.afeibaili

import com.mojang.brigadier.arguments.StringArgumentType
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.ChatFormatting
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor
import net.minecraft.server.permissions.Permissions


/**
 * # 命令类
 *
 * @author AfeiBaili
 * @version 2026/7/25 19:15
 */

object Command {
    fun register() {
        CommandRegistrationCallback.EVENT.register { dispatcher, builder, command ->
            dispatcher.register(
                Commands.literal("mchat")
                    .requires { it.permissions().hasPermission(Permissions.COMMANDS_ADMIN) }
                    .then(
                        Commands.literal("reload").executes {
                            MChat.startMchat()
                            1
                        })
                    .then(
                        Commands.literal("colors")
                            .then(
                                Commands.argument("hexcolor", StringArgumentType.greedyString())
                                    .executes { it ->
                                        val text: String = StringArgumentType.getString(it, "hexcolor")
                                        try {
                                            MChat.style =
                                                Style.EMPTY.withColor(TextColor.parseColor(text).result().get())
                                        } catch (e: Exception) {
                                            it.source.sendSystemMessage(
                                                Component
                                                    .literal("failed, unknown hex color")
                                                    .withStyle(ChatFormatting.RED)
                                            )
                                            return@executes 0
                                        }
                                        it.source.sendSystemMessage(
                                            Component
                                                .literal("succeed, update chat color for $text")
                                                .withStyle(ChatFormatting.WHITE)
                                        )
                                        return@executes 1
                                    }
                            )
                    )
            )
        }
    }
}