package mcjty.needtobreathe.blocks;

import mcjty.lib.container.DefaultSidedInventory;
import mcjty.lib.container.InventoryHelper;
import mcjty.lib.entity.GenericEnergyReceiverTileEntity;
import mcjty.needtobreathe.config.Config;
import mcjty.needtobreathe.data.CleanAirManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class PurifierTileEntity extends GenericEnergyReceiverTileEntity implements ITickable, DefaultSidedInventory {

    private InventoryHelper inventoryHelper = new InventoryHelper(this, PurifierContainer.factory, 1);


    public PurifierTileEntity() {
        super(Config.PURIFIER_MAXRF, Config.PURIFIER_RFINPUTPERTICK);
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            int energyStored = getEnergyStored();
            if (energyStored >= Config.PURIFIER_RFPERTICK) {
                CleanAirManager.getManager().addCleanAir(pos, 1.0f);
                consumeEnergy(Config.PURIFIER_RFPERTICK);
            }
        }
    }

    @Override
    public InventoryHelper getInventoryHelper() {
        return inventoryHelper;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[] { PurifierContainer.SLOT_COALINPUT };
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
        return isItemValidForSlot(index, stack);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == PurifierContainer.SLOT_COALINPUT) {
            return stack.getItem() == Items.COAL || stack.getItem() == Item.getItemFromBlock(Blocks.COAL_BLOCK);
        }
        return true;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
    }


    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return canPlayerAccess(player);
    }

    @Override
    public void readRestorableFromNBT(NBTTagCompound tagCompound) {
        super.readRestorableFromNBT(tagCompound);
        readBufferFromNBT(tagCompound, inventoryHelper);
    }

    @Override
    public void writeRestorableToNBT(NBTTagCompound tagCompound) {
        super.writeRestorableToNBT(tagCompound);
        writeBufferToNBT(tagCompound, inventoryHelper);
    }


}
