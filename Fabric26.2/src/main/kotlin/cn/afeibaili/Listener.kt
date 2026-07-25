package cn.afeibaili

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents


/**
 * # 事件监听
 *
 * @author AfeiBaili
 * @version 2026/7/25 16:04
 */

object Listener {
    fun register() {
        ServerPlayerEvents.JOIN.register { player -> }
    }
}