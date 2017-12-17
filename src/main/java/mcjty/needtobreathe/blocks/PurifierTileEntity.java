package mcjty.needtobreathe.blocks;

import mcjty.lib.container.DefaultSidedInventory;
import mcjty.lib.container.InventoryHelper;
import mcjty.lib.entity.GenericEnergyReceiverTileEntity;
import mcjty.lib.varia.BlockTools;
import mcjty.needtobreathe.config.Config;
import mcjty.needtobreathe.data.CleanAirManager;
import mcjty.needtobreathe.data.DimensionData;
import mcjty.needtobreathe.data.LongPos;
import mcjty.needtobreathe.network.IIntegerRequester;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class PurifierTileEntity extends GenericEnergyReceiverTileEntity implements ITickable, DefaultSidedInventory, IIntegerRequester {

    private InventoryHelper inventoryHelper = new InventoryHelper(this, PurifierContainer.factory, 1);

    private int coalticks = 0;
    private boolean isWorking = false;

    // Client side only variables
    private int maxCoalTicks = 1;


    public PurifierTileEntity() {
        super(Config.PURIFIER_MAXRF, Config.PURIFIER_RFINPUTPERTICK);
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            boolean oldIsWorking = isWorking;

            checkForCoal();

            int energyStored = getEnergyStored();
            isWorking = energyStored >= Config.PURIFIER_RFPERTICK && coalticks > 0;
            if (isWorking) {
                CleanAirManager manager = CleanAirManager.getManager();
                DimensionData data = manager.getDimensionData(world.provider.getDimension());
                if (data == null) {
                    // This dimension doesn't need a purifier
                    return;
                }
                // Depending on how pure it already is we decrease this faster or slower
                BlockPos p = getPurifyingSpot();
                long pp = p.toLong();

                if (data.isValid(world, pp)) {
                    int workdone = 0;
                    for (int dx = -1 ; dx <= 1 ; dx++) {
                        for (int dy = -1 ; dy <= 1 ; dy++) {
                            for (int dz = -1 ; dz <= 1 ; dz++) {
                                long p2 = LongPos.toLong(p.getX()+dx, p.getY()+dy, p.getZ()+dz);
                                if (data.isValid(world, p2)) {
                                    workdone += data.fillCleanAir(p2);
                                }
                            }
                        }
                    }
                    if (workdone < 4) {
                        // Not much done to do. It is as pure as can be
                    } else if (workdone < 50) {
                        // Fair amount
                        coalticks--;
                    } else if (workdone < 100) {
                        coalticks -= 2;
                    } else if (workdone < 200) {
                        coalticks -= 3;
                    } else {
                        coalticks -= 4;
                    }
                    if (coalticks < 0) {
                        coalticks = 0;
                    }
                    consumeEnergy(Config.PURIFIER_RFPERTICK);
                }
            }

            if (isWorking != oldIsWorking) {
                markDirtyClient();
            }
        }
    }

    private BlockPos getPurifyingSpot() {
        IBlockState state = world.getBlockState(pos);
        int meta = state.getBlock().getMetaFromState(state);
        EnumFacing k = BlockTools.getOrientation(meta);
        return pos.offset(k);
    }

    private void checkForCoal() {
        ItemStack stack = getStackInSlot(PurifierContainer.SLOT_COALINPUT);
        if (stack.isEmpty()) {
            return;
        }
        if (stack.getItem() == Items.COAL) {
            if (coalticks + Config.PURIFIER_TICKSPERCOAL <= Config.PURIFIER_MAXCOALTICKS) {
                coalticks += Config.PURIFIER_TICKSPERCOAL;
                decrStackSize(PurifierContainer.SLOT_COALINPUT, 1);
            }
        } else if (stack.getItem() == Item.getItemFromBlock(Blocks.COAL_BLOCK)) {
            if (coalticks + (Config.PURIFIER_TICKSPERCOAL * 9) <= Config.PURIFIER_MAXCOALTICKS) {
                coalticks += Config.PURIFIER_TICKSPERCOAL * 9;
                decrStackSize(PurifierContainer.SLOT_COALINPUT, 1);
            }
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        boolean oldIsWorking = isWorking;

        super.onDataPacket(net, packet);

        if (world.isRemote) {
            // If needed send a render update.
            if (isWorking != oldIsWorking) {
                world.markBlockRangeForRenderUpdate(getPos(), getPos());
            }
        }
    }

    public boolean isWorking() {
        return isWorking;
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
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        isWorking = tagCompound.getBoolean("working");
    }

    @Override
    public void writeRestorableToNBT(NBTTagCompound tagCompound) {
        super.writeRestorableToNBT(tagCompound);
        writeBufferToNBT(tagCompound, inventoryHelper);
        tagCompound.setInteger("coal", coalticks);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        // For client only:
        tagCompound.setBoolean("working", isWorking);
        return super.writeToNBT(tagCompound);
    }
}
