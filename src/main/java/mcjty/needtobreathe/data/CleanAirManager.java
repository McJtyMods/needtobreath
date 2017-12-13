package mcjty.needtobreathe.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
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

    private int getPoison(long p) {
        if (cleanAir.containsKey(p)) {
            return 255-(cleanAir.get(p) & 0xff);
        } else {
            return 255;
        }
    }

    public int getAir(BlockPos pos) {
        return getAir(pos.toLong());
    }

    private int getAir(long p) {
        if (cleanAir.containsKey(p)) {
            return cleanAir.get(p) & 0xff;
        } else {
            return 0;
        }
    }

    private boolean isValid(World world, long pos) {
        if (pos == -1L) {
            return false;
        }
        BlockPos p = BlockPos.fromLong(pos);
        return world.isAirBlock(p);
//        return true;
    }

    public void tick(World world) {
        Set<Long> positions = new HashSet<>(cleanAir.keySet());
        for (Long pos : positions) {
            int air = getAir(pos);
            air--;
            if (air < 5) {
                cleanAir.remove(pos);
            } else {
                // Find all adjacent spaces that have less air then this one (and thus more poison)
                List<Long> distList = new ArrayList<>(6);
                for (EnumFacing facing : EnumFacing.VALUES) {
                    long adjacent = LongPos.offset(pos, facing);
                    if (isValid(world, adjacent)) {
                        int adjacentAir = getAir(adjacent);
                        if (adjacentAir < air) {
                            distList.add(adjacent);
                        }
                    }
                }

                if (!distList.isEmpty()) {
                    // We distribute 'air' to all legal adjacent spaces (and this one)
                    int toDistribute = air / (distList.size() + 2);        // We move a part of the air to adjacent tiles (if possible) (but keep most for this position, hence the +2 instead of +1)
                    if (toDistribute > 0) {
                        for (Long adjacent : distList) {
                            int adjacentAir = getAir(adjacent);
                            int dist = Math.min(toDistribute, 255 - adjacentAir);
                            air -= dist;
                            adjacentAir += dist;
                            cleanAir.put(adjacent, (byte) adjacentAir);
                        }
                    }
                    cleanAir.put(pos, (byte) air);
                }
            }
        }
    }

    public void addCleanAir(BlockPos pos, float pct) {
        Long posl = pos.toLong();
        Byte b = cleanAir.get(posl);
        int cleanPct = b == null ? 0 : (b & 0xff);
        cleanPct = (int) (cleanPct + pct * 255);
        if (cleanPct > 255) {
            cleanPct = 255;
        }
        cleanAir.put(posl, (byte) cleanPct);

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
