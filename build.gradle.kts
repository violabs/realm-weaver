import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	val kotlinVersion = "2.0.0"
	kotlin("jvm") version kotlinVersion
	kotlin("plugin.spring") version kotlinVersion
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"
	kotlin("plugin.jpa") version "1.9.25"
	id("org.jetbrains.kotlinx.kover") version "0.7.6"
}

val appGroup = "io.violabs"
val appVersion = "0.0.1"

group = appGroup
version = appVersion

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}


java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

allprojects {
	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "kotlin")
	apply(plugin = "kotlin-spring")
	apply(plugin = "kotlin-jpa")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "org.jetbrains.kotlinx.kover")

	group = appGroup
	version = appVersion

	repositories {
		mavenCentral()
	}

	dependencies {
		implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
	}

	kotlin {
		compilerOptions {
			freeCompilerArgs.addAll("-Xjsr305=strict")
			jvmTarget.set(JvmTarget.JVM_21)
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}

// Configure merged reports at root project level
koverReport {
	defaults {
		xml {
			onCheck = true
			setReportFile(layout.buildDirectory.file("reports/kover/merged-report.xml"))
		}
		html {
			onCheck = true
			setReportDir(layout.buildDirectory.dir("reports/kover/merged-html"))
		}
	}

	filters {
		excludes {
			classes()
		}
	}

	verify {
		rule {
			minBound(80)
		}
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(project(":player"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.liquibase:liquibase-core")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation(project(":core"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("io.mockk:mockk:1.13.12")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	kover(project(":player"))
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy(
		tasks.getByName("koverHtmlReport"),
	)
}

tasks.withType<ProcessResources>() {
	logger.lifecycle("ProcessResources: $name")
}

koverReport {
	defaults {
		xml {
			onCheck = true
		}
		html {
			onCheck = true
		}
	}

	filters {
		excludes {
			// Add any classes you want to exclude from coverage
			classes(
				// Example exclusions:
//				"*.config.*",
//				"*.configuration.*",
//				"*Application*",
//				"*Configuration*"
			)
		}
	}

	verify {
		rule {
			// Add verification rules
			minBound(80) // Minimum 80% coverage
			// Alternatively, you can set specific bounds:
			// bound {
			//     minValue = 80
			//     valueType = kotlinx.kover.api.VerificationValueType.COVERED_LINES_PERCENTAGE
			// }
		}
	}
}