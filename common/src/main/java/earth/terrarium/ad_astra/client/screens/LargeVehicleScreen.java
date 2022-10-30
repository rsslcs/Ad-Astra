package earth.terrarium.ad_astra.client.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import earth.terrarium.ad_astra.screen.handler.LargeVehicleScreenHandler;
import earth.terrarium.ad_astra.util.ModResourceLocation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import java.awt.*;

@Environment(EnvType.CLIENT)
public class LargeVehicleScreen extends AbstractVehicleScreen<LargeVehicleScreenHandler> {

    public static final int INPUT_TANK_LEFT = 50;
    public static final int INPUT_TANK_TOP = 25;
    private static final ResourceLocation TEXTURE = new ModResourceLocation("textures/gui/screens/vehicle_large.png");

    public LargeVehicleScreen(LargeVehicleScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title, TEXTURE);
        this.imageWidth = 177;
        this.imageHeight = 181;
        this.inventoryLabelY = this.imageHeight - 93;
    }

    @Override
    protected void renderBg(PoseStack matrices, float delta, int mouseX, int mouseY) {

        super.renderBg(matrices, delta, mouseX, mouseY);

        GuiUtil.drawFluidTank(matrices, this.leftPos + INPUT_TANK_LEFT, this.topPos + INPUT_TANK_TOP, this.vehicle.getTankSize(), this.menu.getFluids().get(0));
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);

        if (GuiUtil.isHovering(this.getInputTankBounds(), mouseX, mouseY)) {
            GuiUtil.drawTankTooltip(this, matrices, this.menu.getFluids().get(0).getFluidAmount(), this.vehicle.getTankSize(), this.menu.getFluids().get(0).getFluid(), mouseX, mouseY);
        }
    }

    public Rectangle getInputTankBounds() {
        return GuiUtil.getFluidTankBounds(this.leftPos + INPUT_TANK_LEFT, this.topPos + INPUT_TANK_TOP);
    }

    @Override
    public int getTextColour() {
        return 0x2C282E;
    }
}