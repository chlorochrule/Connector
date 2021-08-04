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

package org.eclipse.dataspaceconnector.transfer.nifi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.eclipse.dataspaceconnector.schema.Schema;
import org.eclipse.dataspaceconnector.schema.SchemaAttribute;
import org.eclipse.dataspaceconnector.schema.SchemaRegistry;
import org.eclipse.dataspaceconnector.schema.SchemaValidationException;
import org.eclipse.dataspaceconnector.spi.security.Vault;
import org.eclipse.dataspaceconnector.spi.types.TypeManager;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.SecretToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.easymock.EasyMock.*;

class NifiTransferEndpointConverterTest {

    private NifiTransferEndpointConverter converter;
    private SchemaRegistry registry;

    @BeforeEach
    void setup() {
        Vault vault = mock(Vault.class);
        registry = mock(SchemaRegistry.class);
        var typeManager = new TypeManager();
        typeManager.registerTypes(TestToken.class);
        converter = new NifiTransferEndpointConverter(registry, vault, typeManager);

        expect(vault.resolveSecret("VerySecret")).andReturn(typeManager.writeValueAsString(new TestToken("muchSecret"))).times(2);
        replay(vault);
    }

    @Test
    void convert_success() {
        final String type = "SomeType";
        var da = DataAddress.Builder.newInstance()
                .type(type)
                .keyName("VerySecret")
                .property("someprop", "someval")
                .build();

        var schema = new Schema() {

            @Override
            protected void addAttributes() {
                attributes.add(new SchemaAttribute("someprop", true));
            }

            @Override
            public String getName() {
                return type;
            }
        };
        expect(registry.getSchema(type)).andReturn(schema);
        replay(registry);

        var endpoint = converter.convert(da);
        assertThat(endpoint.getType()).isEqualTo(type);
        assertThat(endpoint.getProperties()).hasSizeGreaterThanOrEqualTo(2)
                .containsEntry("token", "muchSecret")
                .containsEntry("someprop", "someval");
        verify(registry);
    }

    @Test
    void convert_noSchemaFound() {
        final String type = "SomeType";
        var da = DataAddress.Builder.newInstance()
                .type(type)
                .keyName("VerySecret")
                .property("someprop", "someval")
                .build();
        expect(registry.getSchema(type)).andReturn(null);
        replay(registry);

        assertThatThrownBy(() -> converter.convert(da)).isInstanceOf(NifiTransferException.class)
                .hasMessageContaining("No schema is registered for type " + type);
    }

    @Test
    void convert_noTypeSpecified() {
        final String type = "SomeType";
        var da = DataAddress.Builder.newInstance()
                .keyName("VerySecret")
                .type(type)
                .property("someprop", "someval")
                .build();
        da.getProperties().replace("type", null);
        replay(registry);

        assertThatThrownBy(() -> converter.convert(da)).isInstanceOf(NifiTransferException.class)
                .hasMessageContaining("No type was specified!");
    }

    @Test
    void convert_requiredPropsMissing() {
        final String type = "SomeType";
        var da = DataAddress.Builder.newInstance()
                .type(type)
                .keyName("VerySecret")
                .property("someprop", "someval")
                .build();

        var schema = new Schema() {

            @Override
            protected void addAttributes() {
                attributes.add(new SchemaAttribute("someprop", true));
                attributes.add(new SchemaAttribute("anotherprop", true));//this one is missing in the DataAddress
            }

            @Override
            public String getName() {
                return type;
            }
        };
        expect(registry.getSchema(type)).andReturn(schema);
        replay(registry);

        assertThatThrownBy(() -> converter.convert(da)).isInstanceOf(SchemaValidationException.class)
                .hasMessage("Required property is missing in DataAddress: anotherprop (schema: SomeType)");
    }

    @Test
    void convert_propertyHasWrongType() {
        final String type = "SomeType";
        var da = DataAddress.Builder.newInstance()
                .type(type)
                .keyName("VerySecret")
                .property("someprop", "someval")
                .property("anotherprop", "notAnInt")
                .build();

        var schema = new Schema() {

            @Override
            protected void addAttributes() {
                attributes.add(new SchemaAttribute("someprop", true));
                attributes.add(new SchemaAttribute("anotherprop", "int", false));
            }

            @Override
            public String getName() {
                return type;
            }
        };
        expect(registry.getSchema(type)).andReturn(schema);
        replay(registry);

        var endpoint = converter.convert(da);
        //currently this does not throw anything since we don't validate attribute types
        assertThat(endpoint.getProperties()).containsEntry("anotherprop", "notAnInt");
    }


    @Test
    void convert_addressHasNoKeyName() {
        final String type = "SomeType";
        var da = DataAddress.Builder.newInstance()
                .type(type)
                .keyName("VerySecret")
                .property("someprop", "someval")
                .property("anotherprop", "notAnInt")
                .build();

        da.getProperties().replace("keyName", null);

        var schema = new Schema() {

            @Override
            protected void addAttributes() {
                attributes.add(new SchemaAttribute("someprop", true));
                attributes.add(new SchemaAttribute("anotherprop", "int", false));
            }

            @Override
            public String getName() {
                return type;
            }
        };
        expect(registry.getSchema(type)).andReturn(schema);
        replay(registry);

        assertThatThrownBy(() -> converter.convert(da)).isInstanceOf(NullPointerException.class)
                .hasMessage("DataAddress must have a keyName!");
    }

    @Test
    void convert_addressHasNoType() {
        final String type = "SomeType";
        var da = DataAddress.Builder.newInstance()
                .type(type)
                .keyName("VerySecret")
                .property("someprop", "someval")
                .property("anotherprop", "notAnInt")
                .build();

        da.getProperties().replace("type", null);

        var schema = new Schema() {

            @Override
            protected void addAttributes() {
                attributes.add(new SchemaAttribute("someprop", true));
                attributes.add(new SchemaAttribute("anotherprop", "int", false));
            }

            @Override
            public String getName() {
                return type;
            }
        };
        expect(registry.getSchema(type)).andReturn(schema);
        replay(registry);

        assertThatThrownBy(() -> converter.convert(da)).isInstanceOf(NifiTransferException.class)
                .hasMessage("No type was specified!");
    }

    @JsonTypeName("dataspaceconnector:testtoken")
    private static class TestToken implements SecretToken {

        @JsonProperty("token")
        private String token;

        private TestToken(String token) {
            this.token = token;
        }

        public TestToken() {
        }

        @Override
        public long getExpiration() {
            return 0;
        }

        @Override
        public Map<String, ?> flatten() {
            return Map.of("token", token);
        }
    }
}
