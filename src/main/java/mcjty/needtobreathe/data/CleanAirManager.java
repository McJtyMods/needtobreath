package mcjty.needtobreathe.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nonnull;
import java.util.*;

public class CleanAirManager extends WorldSavedData {

    public static final String NAME = "NeedToBreathData";

    private static CleanAirManager instance = null;

    private final Map<Long, Byte> cleanAir = new HashMap<>();       // 0 = no clean air, 255 = 100% clean

    public CleanAirManager(String name) {
        super(name);
    }


    public void save(World world) {
        world.setData(NAME, this);
        markDirty();
    }

    public Map<Long, Byte> getCleanAir() {
        return cleanAir;
    }

    public static void clearInstance() {
        if (instance != null) {
            instance.cleanAir.clear();
            instance = null;
        }
    }

    /**
     * Get the minimum poison level for this position and adjacent positions
     */
    public int getPoison(BlockPos p) {
        int minPoison = 255;
        for (int dx = -1 ; dx <= 1 ; dx++) {
            for (int dy = -1 ; dy <= 1 ; dy++) {
                for (int dz = -1 ; dz <= 1 ; dz++) {
                    long p2 = LongPos.toLong(p.getX()+dx, p.getY()+dy, p.getZ()+dz);
                    int poison = getPoison(p2);
                    if (poison < minPoison) {
                        minPoison = poison;
                        if (minPoison == 0) {
                            return 0;
                        }
                    }
                }
            }
        }
        return minPoison;
    }

    public int getPoison(long p) {
        if (cleanAir.containsKey(p)) {
            return 255-(cleanAir.get(p) & 0xff);
        } else {
            return 255;
        }
    }

    public int getAir(BlockPos pos) {
        return getAir(pos.toLong());
    }

    public int getAir(long p) {
        if (cleanAir.containsKey(p)) {
            return cleanAir.get(p) & 0xff;
        } else {
            return 0;
        }
    }

    public boolean isValid(World world, long pos) {
        if (pos == -1L) {
            return false;
        }
        BlockPos p = BlockPos.fromLong(pos);
        return !canCollideWith(world, p);
    }

    private static boolean canCollideWith(World world, BlockPos pos) {
        if (world.isAirBlock(pos)) {
            return false;
        }
        AxisAlignedBB box = world.getBlockState(pos).getCollisionBoundingBox(world, pos);
        return box != null;
    }

    public void tick(World world) {
        Set<Long> positions = new HashSet<>(cleanAir.keySet());
        for (Long pos : positions) {
            int air = getAir(pos);
            air--;
            if (air < 5) {
                cleanAir.remove(pos);
            } else {
                // Evenly distribute all air to the adjacent spots (and this one)
                int totalAir = air;
                List<Long> distList = new ArrayList<>(6);
                for (EnumFacing facing : EnumFacing.VALUES) {
                    long adjacent = LongPos.offset(pos, facing);
                    if (isValid(world, adjacent)) {
                        int adjacentAir = getAir(adjacent);
                        totalAir += adjacentAir;
                        distList.add(adjacent);
                    }
                }

                if (!distList.isEmpty()) {
                    // We distribute 'air' to all legal adjacent spaces (and this one)
                    air = totalAir / (distList.size()+1);
                    for (Long adjacent : distList) {
                        totalAir -= air;
                        cleanAir.put(adjacent, (byte) air);
                    }
                    cleanAir.put(pos, (byte) totalAir);
                }
            }
        }
    }

    public int fillCleanAir(long p) {
        Byte b = cleanAir.get(p);
        cleanAir.put(p, (byte) 255);
        if (b == null) {
            return 255;
        }
        return 255-b;
    }

    @Nonnull
    public static CleanAirManager getManager() {
        WorldServer world = DimensionManager.getWorld(0);
        if (instance != null) {
            return instance;
        }
        instance = (CleanAirManager) world.loadData(CleanAirManager.class, NAME);
        if (instance == null) {
            instance = new CleanAirManager(NAME);
        }
        return instance;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return null;
    }
}
