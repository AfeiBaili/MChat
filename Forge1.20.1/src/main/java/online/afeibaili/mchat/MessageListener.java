package online.afeibaili.mchat;

import cn.afeibaili.mchat.message.MessageType;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

import static online.afeibaili.mchat.MChat.config;

/**
 * # 消息监听类
 *
 * @author AfeiBaili
 * @version 2026/7/19 22:00
 */

public class MessageListener {
    @SubscribeEvent
    public void onMessage(ServerChatEvent event) {
        String source = "[" + config.getName() + "] " + event.getUsername();
        Optional.ofNullable(MChat.client).ifPresent(client -> client.send(new MessageType.Text(source, event.getMessage().getString(), "")));
    }

    @SubscribeEvent
    public void onPlayerLoginIn(PlayerEvent.PlayerLoggedInEvent event) {
        Optional.ofNullable(MChat.client).ifPresent(client -> client.send(new MessageType.Text(event.getEntity().getName().getString(), "加入了" + config.getName(), "")));
    }

    @SubscribeEvent
    public void onPlayerLoginOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Optional.ofNullable(MChat.client).ifPresent(client -> client.send(new MessageType.Text(event.getEntity().getName().getString(), "退出了" + config.getName(), "")));
    }
}
