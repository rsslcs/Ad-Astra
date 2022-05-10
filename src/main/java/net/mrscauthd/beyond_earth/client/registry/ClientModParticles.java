package net.mrscauthd.beyond_earth.client.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.screen.PlayerScreenHandler;
import net.mrscauthd.beyond_earth.client.particles.LargeFlameParticle;
import net.mrscauthd.beyond_earth.client.particles.SmallFlameParticle;
import net.mrscauthd.beyond_earth.registry.ModParticles;
import net.mrscauthd.beyond_earth.util.ModIdentifier;

@Environment(EnvType.CLIENT)
public class ClientModParticles {

    public static void register() {

        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
            registry.register(new ModIdentifier("particle/large_flame"));
            registry.register(new ModIdentifier("particle/large_smoke"));
            registry.register(new ModIdentifier("particle/small_flame"));
            registry.register(new ModIdentifier("particle/small_smoke"));
        }));

        ParticleFactoryRegistry.getInstance().register(ModParticles.LARGE_FLAME, LargeFlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.LARGE_SMOKE, LargeFlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.SMALL_FLAME, SmallFlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.SMALL_SMOKE, SmallFlameParticle.Factory::new);
    }
}