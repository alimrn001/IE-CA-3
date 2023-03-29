package com.baloot.IE_CA_3.Baloot.Provider;

import com.baloot.IE_CA_3.Baloot.Exceptions.ProviderNotExistsException;

import java.util.HashMap;
import java.util.Map;

public class ProvidersManager {

    private final Map<Integer, Provider> balootProviders = new HashMap<>();

    public boolean providerExists(int providerId) {
        return balootProviders.containsKey(providerId);
    }

    public void addProvider(Provider provider) throws Exception {
        if(providerExists(provider.getId())) {
            balootProviders.get(provider.getId()).setName(provider.getName());
            balootProviders.get(provider.getId()).setRegistryDate(provider.getRegistryDate().toString());
        }
        else
            balootProviders.put(provider.getId(), provider);
    }

    public Provider getBalootProvider(int providerId) throws Exception {
        if(!providerExists(providerId))
            throw new ProviderNotExistsException();
        return balootProviders.get(providerId);
    }

    public Map<Integer, Provider> getBalootProviders() {
        return this.balootProviders;
    }

    public String getProviderNameById(int providerId) {
        return balootProviders.get(providerId).getName();
    }

}
