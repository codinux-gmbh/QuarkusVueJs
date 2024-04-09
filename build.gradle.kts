import com.github.gradle.node.npm.task.NpxTask

plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.allopen") version "1.9.23"

    id("io.quarkus")

    id("com.github.node-gradle.node") version "7.0.2"
}


group = "net.codinux.test"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}


val quarkusVersion: String by project

dependencies {
    implementation(enforcedPlatform("io.quarkus.platform:quarkus-bom:${quarkusVersion}"))

    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-rest-jackson")


    testImplementation("io.quarkus:quarkus-junit5")
}


tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("jakarta.persistence.Entity")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.javaParameters = true
}


val buildVueJsAppTask = tasks.register<NpxTask>("buildVueJsApp") {
    dependsOn("npmInstall")
    group = "frontend"
    description = "Transpiles Vue.js components to standard JavaScript and CSS files and copies them to src/resources/META-INF/resources"

    workingDir.set(File("$projectDir/src/main/webapp"))

    command.set("npm")
    args.addAll("run", "build")
}

tasks.named("quarkusBuild") {
    dependsOn(buildVueJsAppTask)
}

val watchVueJsChangesTask = tasks.register("watchVueJsChanges") {
    dependsOn("npmInstall")
    group = "frontend"
    description = "On each change to Vue.js files transpiles components to standard JavaScript and CSS files and copies them to src/resources/META-INF/resources"

    doFirst {
        // asynchronously start file watch that transpiles Vue.js files and copies them to src/resources/META-INF/resources
        this.extra["process"] = ProcessBuilder()
            .directory(File("$projectDir/src/main/webapp"))
            .command("npm", "run", "watch")
            .start()
    }
}

val stopWatchingVueJsChangesTask = tasks.register("stopWatchingVueJsChanges") {
    group = "frontend"
    description = "Stops watching changes to Vue.js files"

    doFirst {
        // stop async file watch process again
        (watchVueJsChangesTask.get().extra["process"] as? Process)?.destroy()
    }
}

tasks.named("quarkusDev") {
    dependsOn(watchVueJsChangesTask)

    finalizedBy(stopWatchingVueJsChangesTask)
}
