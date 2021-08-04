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

val rsApi: String by project

plugins {
    `java-library`
}


dependencies {
    api(project(":spi"))
    implementation("jakarta.ws.rs:jakarta.ws.rs-api:${rsApi}")

}


