package cn.afeibaili.mchat.network

import io.netty.channel.group.DefaultChannelGroup
import io.netty.util.concurrent.GlobalEventExecutor


/**
 * # 全局通道集合
 *
 * @author AfeiBaili
 * @version 2026/10/19 23:01
 */

object GlobalChannelSet {
    @JvmStatic
    val channels = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
}