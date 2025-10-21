import org.gradle.internal.impldep.org.jsoup.nodes.Document

plugins {
	java
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
    jacoco
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "Experiment1"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}


repositories {
	mavenCentral()
}


dependencies {
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.6.0")
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
	testImplementation("org.projectlombok:lombok:1.18.32")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.32")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.6.0")
	implementation ("jakarta.persistence:jakarta.persistence-api:3.2.0")
	implementation("org.hibernate.orm:hibernate-core:7.1.1.Final")
    implementation("com.h2database:h2:2.3.232")
	implementation("redis.clients:jedis:6.2.0")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("com.rabbitmq:amqp-client:5.20.0")
	//implementation("org.neo4j.driver:neo4j-java-driver:5.6.0") too outdated version, let spring handle version below.
	implementation("org.springframework.boot:spring-boot-starter-data-neo4j")

}

tasks.withType<Test> {
	useJUnitPlatform()
}

