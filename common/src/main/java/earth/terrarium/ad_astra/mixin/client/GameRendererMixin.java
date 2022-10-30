package earth.terrarium.ad_astra.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import earth.terrarium.ad_astra.entities.vehicles.RocketEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    // Shake the player camera when inside of a vehicle.
    @Inject(at = @At(value = "HEAD"), method = "bobView", cancellable = true)
    public void adastra_bobView(PoseStack matrices, float tickDelta, CallbackInfo ci) {

        Minecraft client = Minecraft.getInstance();

        if (client.getCameraEntity() instanceof LocalPlayer player) {
            if (player.getVehicle() instanceof RocketEntity entity) {
                if (entity.getPhase() != 3) {
                    if (entity.isFlying()) {
                        ci.cancel();

                        float f = player.walkDist - player.walkDistO;
                        float g = -(player.walkDist + f * tickDelta);
                        float h = Mth.lerp(tickDelta, 0.075f, -0.075f);
                        matrices.translate(Mth.sin(g * (float) Math.PI) * h * 0.5f, -Math.abs(Mth.cos(g * (float) Math.PI) * h), 0.0);
                        matrices.mulPose(Vector3f.ZP.rotationDegrees(Mth.sin(g * (float) Math.PI) * h * 3.0f));
                        matrices.mulPose(Vector3f.XP.rotationDegrees(Math.abs(Mth.cos(g * (float) Math.PI - 0.2f) * h) * 5.0f));
                    }
                }
            }
        }
    }
}