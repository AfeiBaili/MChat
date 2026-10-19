package cn.afeibaili.mchat.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File

/**
 * # 配置文件
 *
 * @author AfeiBaili
 * @version 2026/7/18 17:45
 */

class Config(
    val port: Int,
    val token: String,
    val groups: Set<Long>,
) {
    companion object {
        val json = ObjectMapper().registerKotlinModule()

        fun load(): Config {
            val config = "${System.getProperty("user.dir")}/config/mchat.json"
            val file = File(config)
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
                file.writeText(
                    json.writerWithDefaultPrettyPrinter().writeValueAsString(
                        Config(33393, "qwd#5q&4e", mutableSetOf())
                    )
                )
            }
            val text: String = file.readText()
            return json.readValue(text, Config::class.java)
        }
    }
}