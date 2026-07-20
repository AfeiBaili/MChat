import net.mamoe.mirai.console.gradle.BuildMiraiPluginV2

plugins {
    val kotlinVersion = "2.0.0"
    kotlin("jvm") version kotlinVersion
    id("net.mamoe.mirai-console") version "2.16.0"
}

group = "cn.afeibaili"
version = "4.2.0"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.6")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.6")
    implementation(fileTree("lib"))
}

tasks.test {
    useJUnitPlatform()
}

afterEvaluate {
    tasks.named<BuildMiraiPluginV2>("buildPlugin") {
        from(fileTree("lib").map { zipTree(it) })
    }
}