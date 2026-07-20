package cn.afeibaili.mchat;

import cn.afeibaili.mchat.message.MessageType;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.event.ServerChatEvent;

import java.util.Optional;

import static cn.afeibaili.mchat.MChat.client;
import static cn.afeibaili.mchat.MChat.config;

/**
 * # 消息监听器
 *
 * @author AfeiBaili
 * @version 2026/7/20 18:42
 */

public class MessageListener {

    @SubscribeEvent
    public void onMessage(ServerChatEvent event) {
        String source = "[" + config.getName() + "] " + event.username;
        Optional.ofNullable(client).ifPresent(client -> client.send(new MessageType.Text(source, event.message, config.getChannel())));
    }

    @SubscribeEvent
    public void onPlayerLoginIn(PlayerEvent.PlayerLoggedInEvent event) {
        Optional.ofNullable(MChat.client).ifPresent(client -> client.send(new MessageType.Text(event.player.getCommandSenderName(), "加入了" + config.getName(), config.getChannel())));

    }

    @SubscribeEvent
    public void onPlayerLoginOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Optional.ofNullable(MChat.client).ifPresent(client -> client.send(new MessageType.Text(event.player.getCommandSenderName(), "退出了" + config.getName(), config.getChannel())));
    }
}
