package cn.afeibaili.mchat.network

import cn.afeibaili.mchat.Main.log
import cn.afeibaili.mchat.network.message.MessageDecoder
import cn.afeibaili.mchat.network.message.MessageEncoder
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelPipeline
import io.netty.channel.socket.nio.NioSocketChannel


/**
 * # 客户端进入处理器
 *
 * @author AfeiBaili
 * @version 2026/10/19 22:32
 */

class ClientInitHandler : ChannelInitializer<NioSocketChannel>() {
    override fun initChannel(ch: NioSocketChannel) {
        log("MChat客户端加入连接: ${ch.remoteAddress()}")
        GlobalChannelSet.channels.add(ch)
        val pipeline: ChannelPipeline = ch.pipeline()
        pipeline.addLast(MessageEncoder())
        pipeline.addLast(MessageDecoder())
        pipeline.addLast(ClientHandler())
    }
}