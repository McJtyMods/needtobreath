package mcjty.needtobreathe.blocks;

import mcjty.lib.container.DefaultSidedInventory;
import mcjty.lib.container.InventoryHelper;
import mcjty.lib.tileentity.GenericEnergyReceiverTileEntity;
import mcjty.needtobreathe.api.IAirCanister;
import mcjty.needtobreathe.config.ConfigSetup;
import mcjty.needtobreathe.data.CleanAirManager;
import mcjty.needtobreathe.data.DimensionData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class AirCompressorTileEntity extends GenericEnergyReceiverTileEntity implements ITickable, DefaultSidedInventory {

    private InventoryHelper inventoryHelper = new InventoryHelper(this, AirCompressorContainer.factory, 1);
    private boolean blocked;

    public AirCompressorTileEntity() {
        super(ConfigSetup.AIRCOMPRESSOR_MAXRF, ConfigSetup.AIRCOMPRESSOR_RFINPUTPERTICK);
    }

    @Override
    public InventoryHelper getInventoryHelper() {
        return inventoryHelper;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[] { AirCompressorContainer.SLOT_AIRCANISTER};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
        return isItemValidForSlot(index, stack);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == AirCompressorContainer.SLOT_AIRCANISTER) {
            return stack.getItem() instanceof IAirCanister;
        }
        return true;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public boolean isBlocked() {
        return blocked;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return canPlayerAccess(player);
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            if (blocked) {
                blocked = false;
                markDirtyQuick();
            }
            ItemStack itemToCharge = getStackInSlot(AirCompressorContainer.SLOT_AIRCANISTER);
            if (!itemToCharge.isEmpty() && itemToCharge.getItem() instanceof IAirCanister && ((IAirCanister) itemToCharge.getItem()).isActive(itemToCharge)) {
                IAirCanister canister = (IAirCanister) itemToCharge.getItem();

                int air = canister.getAir(itemToCharge);
                if (air >= canister.getMaxAir(itemToCharge)) {
                    return;
                }
                if (getStoredPower() < ConfigSetup.AIRCOMPRESSOR_RFPERTICK) {
                    return;
                }
                consumeEnergy(ConfigSetup.AIRCOMPRESSOR_RFPERTICK);
                DimensionData data = CleanAirManager.getManager().getDimensionData(world.provider.getDimension());
                if (data == null) {
                    // No poison in this dimension so the machine works ideally
                    canister.setAir(itemToCharge, air+1);
                } else {
                    BlockPos up = getPos().up();
                    if (!DimensionData.isValid(world, world.getBlockState(up), up)) {
                        blocked = true;
                        markDirtyQuick();
                        return;
                    }
                    int poison = data.getPoison(world, up);
                    if (poison > 200) {
                        // Can't work. Too much poison
                    } else if (poison > 100) {
                        // Work but not very well
                        if (world.rand.nextFloat() > .5f) {
                            canister.setAir(itemToCharge, air+1);
                        }
                    } else {
                        canister.setAir(itemToCharge, air+1);
                    }
                }
            }
        }
    }


    @Override
    public void readRestorableFromNBT(NBTTagCompound tagCompound) {
        super.readRestorableFromNBT(tagCompound);
        readBufferFromNBT(tagCompound, inventoryHelper);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        blocked = tagCompound.getBoolean("blocked");
    }

    @Override
    public void writeRestorableToNBT(NBTTagCompound tagCompound) {
        super.writeRestorableToNBT(tagCompound);
        writeBufferToNBT(tagCompound, inventoryHelper);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        // For client only:
        tagCompound.setBoolean("blocked", blocked);
        return super.writeToNBT(tagCompound);
    }
}
