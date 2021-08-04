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

package org.eclipse.dataspaceconnector.ids.spi.policy;

import org.eclipse.dataspaceconnector.policy.model.Duty;
import org.eclipse.dataspaceconnector.policy.model.Operator;

/**
 * Evaluates a constraint attached to a duty.
 */
@FunctionalInterface
public interface IdsRequestDutyFunction {

    /**
     * Performs the evaluation.
     *
     * @param operator   the operation
     * @param rightValue the right-side expression for the constraint
     * @param duty       the duty associated with the constraint
     * @param context    the policy context
     */
    boolean evaluate(Operator operator, String rightValue, Duty duty, IdsRequestPolicyContext context);

}
