plugins {
    val kotlinVersion = "2.0.0"
    kotlin("jvm") version kotlinVersion
    id("net.mamoe.mirai-console") version "2.16.0"
}

group = "cn.afeibaili"
version = "5.0.3"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.6")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.6")
    implementation("io.netty:netty-all:4.1.138.Final")
    implementation(fileTree("lib"))
}

tasks.test {
    useJUnitPlatform()
}