/*
 *  Copyright (c) 2020, 2021 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors: 1
 *       Microsoft Corporation - initial API and implementation
 *
 */

plugins {
    `java-library`
}

val jwtVersion: String by project

dependencies {
    api(project(":spi"))
    implementation("com.auth0:java-jwt:${jwtVersion}")
}

publishing {
    publications {
        create<MavenPublication>("iam.oauth2") {
            artifactId = "iam.oauth2"
            from(components["java"])
        }
    }
}
