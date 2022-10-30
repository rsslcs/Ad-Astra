package earth.terrarium.ad_astra.client.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import earth.terrarium.ad_astra.AdAstra;
import earth.terrarium.ad_astra.blocks.machines.entity.OxygenDistributorBlockEntity;
import earth.terrarium.ad_astra.client.screens.utils.ButtonType;
import earth.terrarium.ad_astra.client.screens.utils.CustomButton;
import earth.terrarium.ad_astra.client.screens.utils.PlanetSelectionScreen.TooltipType;
import earth.terrarium.ad_astra.client.screens.utils.ScreenUtils;
import earth.terrarium.ad_astra.data.ButtonColour;
import earth.terrarium.ad_astra.networking.NetworkHandling;
import earth.terrarium.ad_astra.networking.packets.client.ToggleDistributorPacket;
import earth.terrarium.ad_astra.screen.handler.OxygenDistributorScreenHandler;
import earth.terrarium.ad_astra.util.ModResourceLocation;
import earth.terrarium.ad_astra.util.OxygenUtils;
import earth.terrarium.botarium.api.fluid.FluidHooks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import java.awt.*;
import java.util.Arrays;

@Environment(EnvType.CLIENT)
public class OxygenDistributorScreen extends AbstractMachineScreen<OxygenDistributorBlockEntity, OxygenDistributorScreenHandler> {

    public static final int INPUT_TANK_LEFT = 50;
    public static final int INPUT_TANK_TOP = 80;
    public static final int OUTPUT_TANK_LEFT = 114;
    public static final int OUTPUT_TANK_TOP = 80;
    public static final int ENERGY_LEFT = 147;
    public static final int ENERGY_TOP = 82;
    private static final ResourceLocation TEXTURE = new ModResourceLocation("textures/gui/screens/oxygen_distributor.png");
    private static final ResourceLocation WARNING_SIGN = new ModResourceLocation("textures/gui/overlay/warning_sign.png");
    final Component SHOW_TEXT = ScreenUtils.createText("show");
    final Component HIDE_TEXT = ScreenUtils.createText("hide");
    CustomButton visibleButton;
    private boolean displayConversionEnergyCost = false;

    public OxygenDistributorScreen(OxygenDistributorScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title, TEXTURE);
        this.imageWidth = 177;
        this.imageHeight = 244;
        this.inventoryLabelY = this.imageHeight - 92;
        this.titleLabelY = 67;

    }

    @Override
    protected void renderBg(PoseStack matrices, float delta, int mouseX, int mouseY) {

        super.renderBg(matrices, delta, mouseX, mouseY);

        GuiUtil.drawEnergy(matrices, this.leftPos + ENERGY_LEFT, this.topPos + ENERGY_TOP, this.menu.getEnergyAmount(), this.machine.getMaxCapacity());
        GuiUtil.drawFluidTank(matrices, this.leftPos + INPUT_TANK_LEFT, this.topPos + INPUT_TANK_TOP, this.machine.getInputTankCapacity(), this.menu.getFluids().get(0));
        GuiUtil.drawFluidTank(matrices, this.leftPos + OUTPUT_TANK_LEFT, this.topPos + OUTPUT_TANK_TOP, this.machine.getOutputTankCapacity(), this.menu.getFluids().get(1));

        visibleButton.setMessage(this.machine.shouldShowOxygen() ? HIDE_TEXT : SHOW_TEXT);

        // Render a warning sign if there is an oxygen leak detected.
        boolean oxygenLeak = OxygenUtils.getOxygenBlocksCount(this.machine.getLevel(), this.machine.getBlockPos()) >= AdAstra.CONFIG.oxygenDistributor.maxBlockChecks;
        if (oxygenLeak) {
            ScreenUtils.addTexture(matrices, this.width / 2 - 85, this.height / 2 - 137, 14, 15, WARNING_SIGN);
        }
        if (OxygenUtils.getOxygenBlocksCount(this.machine.getLevel(), this.machine.getBlockPos()) <= 0 && this.machine.getEnergyStorage().getStoredEnergy() > 0 && this.menu.getFluids().get(1).getFluidAmount() > 0) {
            ScreenUtils.addTexture(matrices, this.width / 2 - 67, this.height / 2 - 137, 14, 15, WARNING_SIGN);
        }
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);

        if (GuiUtil.isHovering(this.getEnergyBounds(), mouseX, mouseY)) {
            GuiUtil.drawEnergyTooltip(this, matrices, this.menu.getEnergyAmount(), this.machine.getMaxCapacity(), mouseX, mouseY);
        }

        if (GuiUtil.isHovering(this.getInputTankBounds(), mouseX, mouseY)) {
            GuiUtil.drawTankTooltip(this, matrices, this.menu.getFluids().get(0), this.machine.getInputTankCapacity(), mouseX, mouseY);
        }

        if (GuiUtil.isHovering(this.getOutputTankBounds(), mouseX, mouseY)) {
            GuiUtil.drawTankTooltip(this, matrices, this.menu.getFluids().get(1), this.machine.getInputTankCapacity(), mouseX, mouseY);
        }

        int oxygenBlocksCount = OxygenUtils.getOxygenBlocksCount(this.machine.getLevel(), this.machine.getBlockPos());
        boolean oxygenLeak = OxygenUtils.getOxygenBlocksCount(this.machine.getLevel(), this.machine.getBlockPos()) >= AdAstra.CONFIG.oxygenDistributor.maxBlockChecks;
        if (oxygenLeak) {
            if (GuiUtil.isHovering(getOxygenLeakWarningSignBounds(), mouseX, mouseY)) {
                this.renderComponentTooltip(matrices, Arrays.asList(Component.translatable("gauge_text.ad_astra.oxygen_leak_warning[0]"), Component.translatable("gauge_text.ad_astra.oxygen_leak_warning[1]"), Component.translatable("gauge_text.ad_astra.oxygen_leak_warning[2]"), Component.translatable("gauge_text.ad_astra.oxygen_leak_warning[3]"), Component.translatable("gauge_text.ad_astra.oxygen_leak_warning[4]")), mouseX, mouseY);
            }
        }

        if (oxygenBlocksCount <= 0 && this.machine.getEnergyStorage().getStoredEnergy() > 0 && this.menu.getFluids().get(1).getFluidAmount() > 0) {
            if (GuiUtil.isHovering(getBlockedWarningSignBounds(), mouseX, mouseY)) {
                this.renderComponentTooltip(matrices, Arrays.asList(Component.translatable("gauge_text.ad_astra.blocked_warning[0]"), Component.translatable("gauge_text.ad_astra.blocked_warning[1]"), Component.translatable("gauge_text.ad_astra.blocked_warning[2]")), mouseX, mouseY);
            }
        }
    }

    @Override
    protected void renderLabels(PoseStack matrices, int mouseX, int mouseY) {
        super.renderLabels(matrices, mouseX, mouseY);
        long oxygenBlocksCount = OxygenUtils.getOxygenBlocksCount(this.machine.getLevel(), this.machine.getBlockPos());
        matrices.pushPose();
        matrices.scale(0.9f, 0.9f, 0.9f);
        Component oxygenBlockText = Component.translatable("gauge_text.ad_astra.oxygen_blocks");
        Component oxygenBlockAmount = Component.nullToEmpty(oxygenBlocksCount + " / " + AdAstra.CONFIG.oxygenDistributor.maxBlockChecks);

        int offset = 25;
        this.font.draw(matrices, oxygenBlockText, 11, offset + 11, 0x68d975);
        this.font.draw(matrices, oxygenBlockAmount, 11, offset + 24, 0x68d975);

        long energyUsagePerTick = this.machine.getEnergyToConsume(oxygenBlocksCount, true);

        if (displayConversionEnergyCost) {
            energyUsagePerTick += this.machine.getEnergyPerTick();
            this.displayConversionEnergyCost = false;
        } else if (this.menu.getFluids().get(0).getFluidAmount() > 0 && this.menu.getFluids().get(1).getFluidAmount() < this.machine.getOutputTankCapacity()) {
            energyUsagePerTick += this.machine.getEnergyPerTick();
            displayConversionEnergyCost = true;
        }

        long oxygenUsagePerTick = this.machine.getFluidToExtract(oxygenBlocksCount, true);

        float oxygenUsageRounded = FluidHooks.toMillibuckets(oxygenUsagePerTick);
        oxygenUsageRounded = (float) (Math.round(oxygenUsageRounded * 1000.0) / 1000.0);

        // Energy per tick text
        this.font.draw(matrices, Component.translatable("gauge_text.ad_astra.energy_per_tick", energyUsagePerTick), 11, offset - 17, 0x68d975);
        // Oxygen usage per tick text
        this.font.draw(matrices, Component.translatable("gauge_text.ad_astra.fluid_per_tick", oxygenUsageRounded), 11, offset - 5, 0x68d975);
        matrices.popPose();
    }

    @Override
    protected void init() {
        super.init();
        OxygenDistributorBlockEntity oxygenDistributor = this.machine;
        visibleButton = new CustomButton(this.width / 2 + 10, this.height / 2 - 83, oxygenDistributor.shouldShowOxygen() ? HIDE_TEXT : SHOW_TEXT, ButtonType.STEEL, ButtonColour.WHITE, TooltipType.NONE, null, pressed -> {
            NetworkHandling.CHANNEL.sendToServer(new ToggleDistributorPacket(this.machine.getBlockPos()));
            this.machine.setShowOxygen(!oxygenDistributor.shouldShowOxygen());
        });
        visibleButton.doMask = false;
        this.addRenderableWidget(visibleButton);
    }

    public Rectangle getInputTankBounds() {
        return GuiUtil.getFluidTankBounds(this.leftPos + INPUT_TANK_LEFT, this.topPos + INPUT_TANK_TOP);
    }

    public Rectangle getOutputTankBounds() {
        return GuiUtil.getFluidTankBounds(this.leftPos + OUTPUT_TANK_LEFT, this.topPos + OUTPUT_TANK_TOP);
    }

    public Rectangle getOxygenLeakWarningSignBounds() {
        return new Rectangle(this.width / 2 - 85, this.height / 2 - 137, 14, 15);
    }

    public Rectangle getBlockedWarningSignBounds() {
        return new Rectangle(this.width / 2 - 67, this.height / 2 - 137, 14, 15);
    }

    public Rectangle getEnergyBounds() {
        return GuiUtil.getEnergyBounds(this.leftPos + ENERGY_LEFT, this.topPos + ENERGY_TOP);
    }

    @Override
    public int getTextColour() {
        return 0x2C282E;
    }
}