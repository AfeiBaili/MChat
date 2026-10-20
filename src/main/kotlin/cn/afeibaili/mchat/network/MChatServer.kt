package cn.afeibaili.mchat.network

import cn.afeibaili.mchat.Main.log
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel

/**
 * # 服务端主要实现
 *
 * @author AfeiBaili
 * @version 2026/10/19 22:15
 */

class MChatServer(val port: Int) {
    val mainGroup = NioEventLoopGroup()
    val workerGroup = NioEventLoopGroup()
    var future: ChannelFuture? = null

    val server: ServerBootstrap = ServerBootstrap()
        .group(mainGroup, workerGroup)
        .channel(NioServerSocketChannel::class.java)
        .childOption(ChannelOption.SO_KEEPALIVE, true)
        .childHandler(ClientInitHandler())

    fun start() {
        log("MChat已启动至: $port 端口")
        future = server.bind(port).sync()
    }

    fun stop() {
        GlobalChannelSet.channels.close()
        future?.channel()?.closeFuture()
        mainGroup.shutdownGracefully()
        workerGroup.shutdownGracefully()
        log("已关闭MChat服务器")
    }
}