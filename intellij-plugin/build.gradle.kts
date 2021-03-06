plugins {
    java
    idea
    id("org.jetbrains.intellij") version "0.4.2"
}

group = "com.ns"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}