import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

tasks.named("shadowJar", ShadowJar::class) {
    exclude("META-INF/versions/**")
    exclude("META-INF/*.kotlin_module")
    exclude("module-info.class")
}

tasks.jar {
    archiveFileName.set("mchat-forge-1.7.10-${properties["mchatVersion"]}-${properties["mchatUpdateInfo"]}.jar")
}
