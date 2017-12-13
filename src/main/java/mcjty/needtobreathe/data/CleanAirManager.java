package mcjty.needtobreathe.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

            // We distribute 'air' to all adjacent spaces (and this one)
            int toDistribute = air / 10;        // We move 1 tenth of the air to adjacent tiles (if possible)
            if (toDistribute > 0) {
                for (EnumFacing facing : EnumFacing.VALUES) {
                    long adjacent = LongPos.offset(pos, facing);
                    if (isValid(world, adjacent)) {
                        int adjacentAir = getAir(adjacent);
                        int dist = Math.min(toDistribute, 255 - adjacentAir);
                        air -= dist;
                        adjacentAir += dist;
                        cleanAir.put(adjacent, (byte) adjacentAir);
                    }
                }
            } else {
                air--;
            }
            if (air < 5) {
                cleanAir.remove(pos);
            } else {
                cleanAir.put(pos, (byte) air);
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
