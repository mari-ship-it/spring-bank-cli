plugins {
	java
	id("org.springframework.boot") version "4.1.1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "sirenko.mar"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework:spring-context")
	implementation("jakarta.annotation:jakarta.annotation-api:3.0.0")
	implementation("org.hibernate.orm:hibernate-core:7.4.11.Final")
	implementation("org.postgresql:postgresql:42.7.13")
	implementation("org.projectlombok:lombok:1.18.48")

//	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
