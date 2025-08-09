plugins {
    id("java")
}

group = "ru.SocialMoods"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.opencollab.dev/maven-releases/")
    maven("https://repo.opencollab.dev/maven-snapshots/")
    maven {
        name = "luminiadevRepositorySnapshots"
        url = uri("https://repo.luminiadev.com/snapshots")
    }
}

dependencies {
    compileOnly("com.koshakmine:Lumi:1.1.0-SNAPSHOT")
    implementation(fileTree("libs") { include("*.jar") })
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}


tasks.test {
    useJUnitPlatform()
}