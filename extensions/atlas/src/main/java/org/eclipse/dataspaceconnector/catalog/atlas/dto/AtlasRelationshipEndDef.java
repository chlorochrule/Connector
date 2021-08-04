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

package org.eclipse.dataspaceconnector.catalog.atlas.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AtlasRelationshipEndDef implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * The type associated with the end.
     */
    private String type;
    /**
     * The name of the attribute for this end
     */
    private String name;

    /**
     * When set this indicates that this end is the container end
     */
    private boolean isContainer;
    /**
     * This is the cardinality of the end
     */
    private AtlasStructDef.AtlasAttributeDef.Cardinality cardinality;
    /**
     * When set this indicates that this end is a legacy attribute
     */
    private boolean isLegacyAttribute;
    /**
     * Description of the end
     */
    private String description;

    /**
     * Base constructor
     */
    public AtlasRelationshipEndDef() {
        this(null, null, AtlasStructDef.AtlasAttributeDef.Cardinality.SINGLE, false);
    }

    /**
     * @param typeName    - The name of an entityDef type
     * @param name        - The name of the new attribute that the entity instance will pick up.
     * @param cardinality - this indicates whether the end is SINGLE (1) or SET (many)
     */
    public AtlasRelationshipEndDef(String typeName, String name, AtlasStructDef.AtlasAttributeDef.Cardinality cardinality) {
        this(typeName, name, cardinality, false);
    }

    /**
     * @param typeName    - The name of an entityDef type
     * @param name        - The name of the new attribute that the entity instance will pick up.
     * @param cardinality - whether the end is SINGLE (1) or SET (many)
     * @param isContainer - whether the end is a container or not
     */
    public AtlasRelationshipEndDef(String typeName, String name, AtlasStructDef.AtlasAttributeDef.Cardinality cardinality, boolean isContainer) {
        this(typeName, name, cardinality, isContainer, false);
    }

    /**
     * @param typeName          - The name of an entityDef type
     * @param name              - The name of the new attribute that the entity instance will pick up.
     * @param cardinality       - whether the end is SINGLE (1) or SET (many)
     * @param isContainer       - whether the end is a container or not
     * @param isLegacyAttribute - whether this is a legacy attribute
     */
    public AtlasRelationshipEndDef(String typeName, String name, AtlasStructDef.AtlasAttributeDef.Cardinality cardinality, boolean isContainer, boolean isLegacyAttribute) {
        this(typeName, name, cardinality, isContainer, isLegacyAttribute, null);
    }

    /**
     * @param typeName          - The name of an entityDef type
     * @param name              - The name of the new attribute that the entity instance will pick up.
     * @param cardinality       - whether the end is SINGLE (1) or SET (many)
     * @param isContainer       - whether the end is a container or not
     * @param isLegacyAttribute - whether this is a legacy attribute
     * @param description       - The description of this end of the relationship.
     */
    public AtlasRelationshipEndDef(String typeName, String name, AtlasStructDef.AtlasAttributeDef.Cardinality cardinality, boolean isContainer, boolean isLegacyAttribute, String description) {
        setType(typeName);
        setName(name);
        setCardinality(cardinality);
        setIsContainer(isContainer);
        setIsLegacyAttribute(isLegacyAttribute);
        setDescription(description);
    }

    /**
     * Construct using an existing AtlasRelationshipEndDef
     *
     * @param other
     */
    public AtlasRelationshipEndDef(AtlasRelationshipEndDef other) {
        if (other != null) {
            setType(other.getType());
            setName(other.getName());
            setIsContainer(other.getIsContainer());
            setCardinality(other.getCardinality());
            setIsLegacyAttribute(other.isLegacyAttribute);
            setDescription(other.description);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsContainer() {
        return isContainer;
    }

    /**
     * set whether this end is a container or not.
     *
     * @param isContainer
     */
    public void setIsContainer(boolean isContainer) {
        this.isContainer = isContainer;
    }

    /**
     * @return the cardinality
     */
    public AtlasStructDef.AtlasAttributeDef.Cardinality getCardinality() {
        return cardinality;
    }

    /**
     * set the cardinality SINGLE or SET on the end.
     *
     * @param cardinality
     */
    public void setCardinality(AtlasStructDef.AtlasAttributeDef.Cardinality cardinality) {
        this.cardinality = cardinality;
    }

    public boolean getIsLegacyAttribute() {
        return isLegacyAttribute;
    }

    public void setIsLegacyAttribute(boolean legacyAttribute) {
        isLegacyAttribute = legacyAttribute;
    }

    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("AtlasRelationshipEndDef{");
        sb.append("type='").append(type).append('\'');
        sb.append(", name==>'").append(name).append('\'');
        sb.append(", description==>'").append(description).append('\'');
        sb.append(", isContainer==>'").append(isContainer).append('\'');
        sb.append(", cardinality==>'").append(cardinality).append('\'');
        sb.append(", isLegacyAttribute==>'").append(isLegacyAttribute).append('\'');
        sb.append('}');

        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AtlasRelationshipEndDef that = (AtlasRelationshipEndDef) o;

        return Objects.equals(type, that.type) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                isContainer == that.isContainer &&
                cardinality == that.cardinality &&
                isLegacyAttribute == that.isLegacyAttribute;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, getName(), description, isContainer, cardinality, isLegacyAttribute);
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
