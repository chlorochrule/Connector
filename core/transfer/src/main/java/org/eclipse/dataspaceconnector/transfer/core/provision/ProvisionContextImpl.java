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

package org.eclipse.dataspaceconnector.transfer.core.provision;

import org.eclipse.dataspaceconnector.spi.transfer.provision.ProvisionContext;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.ProvisionedDataDestinationResource;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.ProvisionedResource;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.SecretToken;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Provision context backed by a {@link TransferProcessStore}.
 */
public class ProvisionContextImpl implements ProvisionContext {
    private final Consumer<ProvisionedResource> resourceCallback;
    private final BiConsumer<ProvisionedDataDestinationResource, SecretToken> destinationCallback;
    private final BiConsumer<ProvisionedDataDestinationResource, Throwable> deprovisionCallback;
    private final TransferProcessStore processStore;

    public ProvisionContextImpl(TransferProcessStore processStore,
                                Consumer<ProvisionedResource> resourceCallback,
                                BiConsumer<ProvisionedDataDestinationResource, SecretToken> destinationCallback,
                                BiConsumer<ProvisionedDataDestinationResource, Throwable> deprovisionCallback) {
        this.processStore = processStore;
        this.resourceCallback = resourceCallback;
        this.destinationCallback = destinationCallback;
        this.deprovisionCallback = deprovisionCallback;
    }

    @Override
    public void callback(ProvisionedResource resource) {
        resourceCallback.accept(resource);
    }

    @Override
    public void callback(ProvisionedDataDestinationResource resource, SecretToken secretToken) {
        destinationCallback.accept(resource, secretToken);
    }

    @Override
    public void deprovisioned(ProvisionedDataDestinationResource resource, Throwable error) {
        deprovisionCallback.accept(resource, error);
    }

    @Override
    public void create(String processId, String resourceDefinitionId, Object data) {
        processStore.createData(processId, resourceDefinitionId, data);
    }

    @Override
    public void update(String processId, String resourceDefinitionId, Object data) {
        processStore.updateData(processId, resourceDefinitionId, data);
    }

    @Override
    public <T> @Nullable T find(Class<T> type, String processId, String resourceDefinitionId) {
        return processStore.findData(type, processId, resourceDefinitionId);
    }
}
