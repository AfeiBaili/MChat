plugins {
    val kotlinVersion = "1.9.0"
    kotlin("jvm") version kotlinVersion
    id("net.mamoe.mirai-console") version "2.16.0"
}

group = "cn.afeibaili"
version = "MChatMiraiV4"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}