import cn.afeibaili.mchat.network.MChatServer
import kotlin.test.Test

/**
 * # 测试服务器
 *
 * @author AfeiBaili
 * @version 2026/10/19 23:16
 */

class TestServer {
    @Test
    fun test01() {
        val server = MChatServer(33393)
        server.start()
    }
}