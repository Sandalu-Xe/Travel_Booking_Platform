import os

services = [
    {
        "name": "user-service",
        "port": 8081,
        "dependencies": ["web", "data-jpa", "h2", "lombok"]
    },
    {
        "name": "flight-service",
        "port": 8082,
        "dependencies": ["web", "data-jpa", "h2", "lombok"]
    },
    {
        "name": "hotel-service",
        "port": 8083,
        "dependencies": ["web", "data-jpa", "h2", "lombok"]
    },
    {
        "name": "notification-service",
        "port": 8084,
        "dependencies": ["web", "lombok"]
    },
    {
        "name": "payment-service",
        "port": 8085,
        "dependencies": ["web", "lombok"]
    },
    {
        "name": "booking-service",
        "port": 8080,
        "dependencies": ["web", "webflux", "openfeign", "lombok"]
    }
]

base_dir = "smart-travel-platform"

pom_template = """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>3.2.0</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.travel</groupId>
	<artifactId>{name}</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>{name}</name>
	<description>Demo project for Spring Boot</description>
	<properties>
		<java.version>17</java.version>
		<spring-cloud.version>2023.0.0</spring-cloud.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
{dependencies_xml}
	</dependencies>
	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-dependencies</artifactId>
				<version>${{spring-cloud.version}}</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>
	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<excludes>
						<exclude>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
						</exclude>
					</excludes>
				</configuration>
			</plugin>
		</plugins>
	</build>
</project>
"""

dep_map = {
    "web": """		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>""",
    "data-jpa": """		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>""",
    "h2": """		<dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
			<scope>runtime</scope>
		</dependency>""",
    "lombok": """		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>""",
    "webflux": """		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webflux</artifactId>
		</dependency>""",
    "openfeign": """		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-openfeign</artifactId>
		</dependency>"""
}

app_class_template = """package com.travel.{package_name};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
{imports}

@SpringBootApplication
{annotations}
public class {class_name}Application {{

	public static void main(String[] args) {{
		SpringApplication.run({class_name}Application.class, args);
	}}

}}
"""

if not os.path.exists(base_dir):
    os.makedirs(base_dir)

for service in services:
    service_name = service["name"]
    service_dir = os.path.join(base_dir, service_name)
    
    # Create directories
    package_name = service_name.replace("-", "")
    src_main_java = os.path.join(service_dir, "src/main/java/com/travel", package_name)
    src_main_resources = os.path.join(service_dir, "src/main/resources")
    os.makedirs(src_main_java, exist_ok=True)
    os.makedirs(src_main_resources, exist_ok=True)
    
    # Create pom.xml
    deps_xml = "\n".join([dep_map[d] for d in service["dependencies"]])
    pom_content = pom_template.format(name=service_name, dependencies_xml=deps_xml)
    with open(os.path.join(service_dir, "pom.xml"), "w") as f:
        f.write(pom_content)
        
    # Create Application class
    class_name = service_name.replace("-", " ").title().replace(" ", "")
    imports = ""
    annotations = ""
    if "openfeign" in service["dependencies"]:
        imports = "import org.springframework.cloud.openfeign.EnableFeignClients;"
        annotations = "@EnableFeignClients"
        
    app_content = app_class_template.format(
        package_name=package_name,
        class_name=class_name,
        imports=imports,
        annotations=annotations
    )
    with open(os.path.join(src_main_java, f"{class_name}Application.java"), "w") as f:
        f.write(app_content)
        
    # Create application.properties
    with open(os.path.join(src_main_resources, "application.properties"), "w") as f:
        f.write(f"server.port={service['port']}\nspring.application.name={service_name}\n")

print("Scaffolding complete.")
