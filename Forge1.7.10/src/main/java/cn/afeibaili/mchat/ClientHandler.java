package cn.afeibaili.mchat;

import cn.afeibaili.mchat.config.ClientConfig;
import cn.afeibaili.mchat.network.ClientBootstrap;
import cn.afeibaili.mchat.network.message.MessageType;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.jetbrains.annotations.NotNull;

/**
 * # MChat客户端处理器
 *
 * @author AfeiBaili
 * @version 2026/10/20 22:20
 */

public class ClientHandler extends ClientCallback {
    public ClientHandler() {
        super();
    }

    @Override
    public void connect(@NotNull ClientBootstrap clientBootstrap) {
        ClientConfig config = MChat.config;
        MChat.client.sendMessage(new MessageType.Text(config.getName(), "已加入服务器", config.getChannel()));
    }

    @Override
    public void disconnect(@NotNull ClientBootstrap clientBootstrap) {
        ClientConfig config = MChat.config;
        MChat.client.sendMessage(new MessageType.Text(config.getName(), "已断开服务器", config.getChannel()));
    }

    @Override
    public void receiveMessage(@NotNull MessageType messageType) {
        MessageType type = MessageType.Companion.fromString(messageType.toString());
        if (type instanceof MessageType.Text) {
            sendTextToPlayer(type);
        } else if (type instanceof MessageType.Image) {
            sendImageToPlayer(type);
        }
    }

    private void sendTextToPlayer(MessageType message) {
        try {
            IChatComponent chatStyle = new ChatComponentText(message.getSource() + ": " + message.getContent()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY));
            MChat.server.getConfigurationManager().sendChatMsg(chatStyle);
        } catch (Exception ignored) {
        }
    }

    private void sendImageToPlayer(MessageType message) {
        try {
            IChatComponent chatStyle = new ChatComponentText(message.getSource() + ": ").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY))
                .appendSibling(new ChatComponentText("[图片]").setChatStyle(
                    new ChatStyle().setColor(EnumChatFormatting.BLUE)
                        .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("打开图片链接")))
                        .setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, message.getContent()))));

            MChat.server.getConfigurationManager().sendChatMsg(chatStyle);
        } catch (Exception ignored) {
        }
    }
}
