package mcjty.needtobreathe.blocks;

import mcjty.lib.container.DefaultSidedInventory;
import mcjty.lib.container.InventoryHelper;
import mcjty.lib.entity.GenericEnergyReceiverTileEntity;
import mcjty.needtobreathe.config.Config;
import mcjty.needtobreathe.data.CleanAirManager;
import mcjty.needtobreathe.network.IIntegerRequester;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class PurifierTileEntity extends GenericEnergyReceiverTileEntity implements ITickable, DefaultSidedInventory, IIntegerRequester {

    private InventoryHelper inventoryHelper = new InventoryHelper(this, PurifierContainer.factory, 1);

    private int coalticks = 0;
    private int maxCoalTicks = 1;   // Client side only


    public PurifierTileEntity() {
        super(Config.PURIFIER_MAXRF, Config.PURIFIER_RFINPUTPERTICK);
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            checkForCoal();

            int energyStored = getEnergyStored();
            if (energyStored >= Config.PURIFIER_RFPERTICK && coalticks > 0) {
                markDirtyQuick();
                CleanAirManager manager = CleanAirManager.getManager();
                // Depending on how pure it already is we decrease this faster or slower
                int air = manager.getAir(pos);
                if (air > 254) {
                    // Nothing to do. It is as pure as can be
                } else if (air > 200) {
                    // Fair amount
                    coalticks--;
                } else if (air > 100) {
                    coalticks -= 2;
                } else if (air > 50) {
                    coalticks -= 3;
                } else {
                    coalticks -= 4;
                }
                manager.addCleanAir(pos, 1.0f);
                consumeEnergy(Config.PURIFIER_RFPERTICK);
            }
        }
    }

    private void checkForCoal() {
        ItemStack stack = getStackInSlot(PurifierContainer.SLOT_COALINPUT);
        if (stack.isEmpty()) {
            return;
        }
        if (stack.getItem() == Items.COAL) {
            if (coalticks + Config.PURIFIER_TICKSPERCOAL <= Config.PURIFIER_MAXCOALTICKS) {
                coalticks += Config.PURIFIER_TICKSPERCOAL;
            }
            decrStackSize(PurifierContainer.SLOT_COALINPUT, 1);
        } else if (stack.getItem() == Item.getItemFromBlock(Blocks.COAL_BLOCK)) {
            if (coalticks + (Config.PURIFIER_TICKSPERCOAL * 9) <= Config.PURIFIER_MAXCOALTICKS) {
                coalticks += Config.PURIFIER_TICKSPERCOAL * 9;
            }
            decrStackSize(PurifierContainer.SLOT_COALINPUT, 1);
        }
    }

    @Override
    public int[] get() {
        return new int[] { getEnergyStored(), coalticks, Config.PURIFIER_MAXCOALTICKS };
    }

    @Override
    public void set(int[] integers) {
        // This is only called client side
        storage.modifyEnergyStored(-2000000000);    // Set to 0
        storage.modifyEnergyStored(integers[0]);
        coalticks = integers[1];
        maxCoalTicks = integers[2];
    }

    public int getCoalticks() {
        return coalticks;
    }

    public int getMaxCoalTicks() {
        if (world.isRemote) {
            return maxCoalTicks;
        } else {
            return Config.PURIFIER_MAXCOALTICKS;
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
        coalticks = tagCompound.getInteger("coal");
    }

    @Override
    public void writeRestorableToNBT(NBTTagCompound tagCompound) {
        super.writeRestorableToNBT(tagCompound);
        writeBufferToNBT(tagCompound, inventoryHelper);
        tagCompound.setInteger("coal", coalticks);
    }


}
