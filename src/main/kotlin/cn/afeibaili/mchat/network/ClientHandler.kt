package cn.afeibaili.mchat.network

import cn.afeibaili.mchat.MessageListener
import cn.afeibaili.mchat.network.message.MessageType
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter


/**
 * # 客户端处理器
 *
 * @author AfeiBaili
 * @version 2026/10/19 23:15
 */

class ClientHandler : ChannelInboundHandlerAdapter() {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val message: MessageType = msg as MessageType
        MessageListener.sendToGroup(message)
        GlobalChannelSet.channels.forEach {
            if (it != ctx.channel()) it.writeAndFlush(message)
        }
    }
}