package cn.afeibaili.mchat.network.message

import cn.afeibaili.mchat.Main
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder


/**
 * # 消息编码器
 *
 * @author AfeiBaili
 * @version 2026/10/19 23:34
 */

class MessageEncoder : MessageToByteEncoder<MessageType>() {
    override fun encode(
        ctx: ChannelHandlerContext,
        msg: MessageType,
        out: ByteBuf,
    ) {
        val text: String = msg.toString()
        val array: ByteArray = text.toByteArray(Charsets.UTF_8)
        val encrypt: ByteArray = Main.cipher.encrypt(array)
        out.writeInt(encrypt.size)
        out.writeBytes(encrypt)
    }
}