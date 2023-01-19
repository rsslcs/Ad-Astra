package earth.terrarium.ad_astra.common.screen.machine;

import earth.terrarium.ad_astra.common.block.machine.entity.TemperatureRegulatorBlockEntity;
import earth.terrarium.ad_astra.common.registry.ModMenus;
import earth.terrarium.ad_astra.common.screen.AbstractModContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.Slot;

public class TemperatureRegulatorMenu extends AbstractModContainerMenu<TemperatureRegulatorBlockEntity> {

    public TemperatureRegulatorMenu(int id, Inventory inv, TemperatureRegulatorBlockEntity entity) {
        super(ModMenus.TEMPERATURE_REGULATOR.get(), id, inv, entity);
    }

    public TemperatureRegulatorMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, getTileFromBuf(inv.player.level, buf, TemperatureRegulatorBlockEntity.class));
    }

    @Override
    protected int getContainerInputEnd() {
        return 1;
    }

    @Override
    protected int getInventoryStart() {
        return 2;
    }

    @Override
    public int getPlayerInvXOffset() {
        return 0;
    }

    @Override
    public int getPlayerInvYOffset() {
        return 60;
    }

    @Override
    protected void addMenuSlots() {
        addSlot(new Slot(entity, 0, 30, 18));
        addSlot(new FurnaceResultSlot(player, entity, 1, 30, 36));
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
