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

package org.eclipse.dataspaceconnector.schema;

public abstract class DataSchema extends Schema {
    //marker class to allow filtering etc.

    public DataSchema() {
        super();
        attributes.add(new SchemaAttribute("keyName", true));
        attributes.add(new SchemaAttribute("policyId", false));
    }
}
