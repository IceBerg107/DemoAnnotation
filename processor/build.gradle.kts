plugins {
    java
}

group = "com.ns"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":annotations"))
    implementation("com.squareup:javapoet:1.11.1")
    implementation("com.google.auto.service:auto-service:1.0-rc4")
    annotationProcessor("com.google.auto.service:auto-service:1.0-rc4")
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}