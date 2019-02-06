plugins {
    java
    id("net.ltgt.apt-idea") version "0.20"
}

group = "com.ns"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":annotations"))
    annotationProcessor(project(":processor"))
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}