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

package org.eclipse.dataspaceconnector.policy.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of child constraints. Subclasses define the semantics for when this constraint is satisfied.
 */
public abstract class MultiplicityConstraint extends Constraint {
    protected List<Constraint> constraints = new ArrayList<>();

    public List<Constraint> getConstraints() {
        return constraints;
    }

    protected abstract static class Builder<T extends MultiplicityConstraint, B extends Builder<T, B>> {
        protected T constraint;

        @SuppressWarnings("unchecked")
        public B constraint(Constraint constraint) {
            this.constraint.constraints.add(constraint);
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B constraints(List<Constraint> constraints) {
            constraint.constraints.addAll(constraints);
            return (B) this;
        }

    }

}
