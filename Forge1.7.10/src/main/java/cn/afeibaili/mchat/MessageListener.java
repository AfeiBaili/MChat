package cn.afeibaili.mchat;

import cn.afeibaili.mchat.network.message.MessageType;
import cpw.mods.fml.common.eventhandler.EventPriority;
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

    @SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
    public void onMessage(ServerChatEvent event) {
        String source = "[" + config.getName() + "] " + event.username;
        Optional.of(client).ifPresent(client -> client.sendMessage(new MessageType.Text(source, event.message, config.getChannel())));
    }

    @SubscribeEvent
    public void onPlayerLoginIn(PlayerEvent.PlayerLoggedInEvent event) {
        Optional.of(MChat.client).ifPresent(client -> client.sendMessage(new MessageType.Text(event.player.getCommandSenderName(), "加入了" + config.getName(), config.getChannel())));

    }

    @SubscribeEvent
    public void onPlayerLoginOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Optional.of(MChat.client).ifPresent(client -> client.sendMessage(new MessageType.Text(event.player.getCommandSenderName(), "退出了" + config.getName(), config.getChannel())));
    }
}
