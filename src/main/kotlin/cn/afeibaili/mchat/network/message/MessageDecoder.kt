package cn.afeibaili.mchat.network.message

import cn.afeibaili.mchat.Main
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import java.nio.charset.StandardCharsets


/**
 * # 消息解码器
 *
 * @author AfeiBaili
 * @version 2026/10/19 23:32
 */

class MessageDecoder : ByteToMessageDecoder() {
    override fun decode(
        ctx: ChannelHandlerContext,
        `in`: ByteBuf,
        out: MutableList<Any?>,
    ) {
        runCatching {
            if (`in`.readableBytes() < 4) return
            val length: Int = `in`.getInt(0)
            if (`in`.readableBytes() < length + 4) return
            `in`.skipBytes(4)
            val data = ByteArray(length)
            `in`.readBytes(data)
            val decrypt: ByteArray = Main.cipher.decrypt(data)
            val text = String(decrypt, StandardCharsets.UTF_8)
            val message: MessageType? = MessageType.fromString(text)
            message?.let { out.add(it) }
        }.onFailure { exception ->
            exception.printStackTrace()
            ctx.channel().close()
            return
        }
    }
}