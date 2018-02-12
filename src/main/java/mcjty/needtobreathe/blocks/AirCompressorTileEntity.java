package mcjty.needtobreathe.blocks;

import mcjty.lib.container.DefaultSidedInventory;
import mcjty.lib.container.InventoryHelper;
import mcjty.lib.entity.GenericEnergyReceiverTileEntity;
import mcjty.needtobreathe.config.Config;
import mcjty.needtobreathe.data.CleanAirManager;
import mcjty.needtobreathe.data.DimensionData;
import mcjty.needtobreathe.items.ModItems;
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
        super(Config.AIRCOMPRESSOR_MAXRF, Config.AIRCOMPRESSOR_RFINPUTPERTICK);
    }

    @Override
    public InventoryHelper getInventoryHelper() {
        return inventoryHelper;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[] { AirCompressorContainer.SLOT_CHESTPLATE };
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
        return isItemValidForSlot(index, stack);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == AirCompressorContainer.SLOT_CHESTPLATE) {
            return stack.getItem() == ModItems.hazmatSuitChest;
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
            ItemStack chestplate = getStackInSlot(AirCompressorContainer.SLOT_CHESTPLATE);
            if (!chestplate.isEmpty()) {
                int air = ModItems.hazmatSuitChest.getAir(chestplate);
                if (air >= Config.HAZMATSUIT_MAXAIR) {
                    return;
                }
                if (getEnergyStored() < Config.AIRCOMPRESSOR_RFPERTICK) {
                    return;
                }
                consumeEnergy(Config.AIRCOMPRESSOR_RFPERTICK);
                DimensionData data = CleanAirManager.getManager().getDimensionData(world.provider.getDimension());
                if (data == null) {
                    // No poison in this dimension so the machine works ideally
                    ModItems.hazmatSuitChest.setAir(chestplate, air+1);
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
                            ModItems.hazmatSuitChest.setAir(chestplate, air+1);
                        }
                    } else {
                        ModItems.hazmatSuitChest.setAir(chestplate, air+1);
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
