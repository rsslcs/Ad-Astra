package earth.terrarium.ad_astra.common.data.energy.fabric;

import earth.terrarium.ad_astra.AdAstra;
import earth.terrarium.ad_astra.common.data.energy.EnergyNetworkManager;
import earth.terrarium.ad_astra.common.data.energy.EnergyNetworkVisibility;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class EnergyNetworkManagerImpl {
    private static final DefaultedRegistry<EnergyNetworkVisibility> REGISTRY = FabricRegistryBuilder.createDefaulted(
            EnergyNetworkVisibility.class,
            new ResourceLocation(AdAstra.MOD_ID, "energy_network_visibility"),
            EnergyNetworkManager.PRIVATE_NETWORK_ID
    ).buildAndRegister();

    static {
        Registry.register(REGISTRY, EnergyNetworkManager.PRIVATE_NETWORK_ID, EnergyNetworkManager.PRIVATE_NETWORK);
        Registry.register(REGISTRY, EnergyNetworkManager.PUBLIC_NETWORK_ID, EnergyNetworkManager.PUBLIC_NETWORK);
    }

    public static DefaultedRegistry<EnergyNetworkVisibility> getVisibilityRegistry() {
        return REGISTRY;
    }
}