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

package org.eclipse.dataspaceconnector.system;

import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtension;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;
import net.jodah.failsafe.RetryPolicy;
import okhttp3.OkHttpClient;
import org.easymock.MockType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.*;

class CoreServicesExtensionTest {

    private final CoreServicesExtension extension = new CoreServicesExtension();

    @Test
    void provides() {
        assertThat(extension.provides()).containsExactlyInAnyOrder("dataspaceconnector:http-client", "dataspaceconnector:retry-policy");
    }

    @Test
    void phase() {
        assertThat(extension.phase()).isEqualTo(ServiceExtension.LoadPhase.PRIMORDIAL);
    }

    @Test
    void initialize() {
        ServiceExtensionContext context = mock(MockType.STRICT, ServiceExtensionContext.class);

        expect(context.getMonitor()).andReturn(new Monitor() {
        });

        context.registerService(eq(OkHttpClient.class), isA(OkHttpClient.class));
        expectLastCall().times(1);

        expect(context.getSetting(eq("dataspaceconnector:core.retry.max-retries"), anyString())).andReturn("3");
        expect(context.getSetting(eq("dataspaceconnector.core.retry.backoff.min"), anyString())).andReturn("500");
        expect(context.getSetting(eq("dataspaceconnector.core.retry.backoff.max"), anyString())).andReturn("10000");

        context.registerService(eq(RetryPolicy.class), isA(RetryPolicy.class));
        expectLastCall().times(1);

        replay(context);

        extension.initialize(context);

        verify(context);
    }
}
