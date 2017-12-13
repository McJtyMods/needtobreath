package mcjty.needtobreath.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CleanAirManager extends WorldSavedData {

    public static final String NAME = "NeedToBreathData";
    public static final int INFINITE = 1000;        // Infinite poison value

    private static CleanAirManager instance = null;

    private final Map<Long, Byte> cleanAir = new HashMap<>();

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

    public void tick() {
        Map<LongPos, Byte> newvalues = new HashMap<>();
        Set<Long> positions = new HashSet<>(cleanAir.keySet());
        for (Long pos : positions) {
            int poison = getPoison(pos);
            for (EnumFacing facing : EnumFacing.VALUES) {
                long adjacent = LongPos.offset(pos, facing);
                int adjacentPoison = getPoison(adjacent);
                if (adjacentPoison > poison) {
                    int halfPoison = (adjacentPoison - poison)/2;
                    cleanAir.put(adjacent, (byte) (adjacentPoison - halfPoison));
                    poison = poison + halfPoison;
                }
            }
            if (poison > 252) {
                cleanAir.remove(pos);
            } else {
                cleanAir.put(pos, (byte) poison);
            }
        }
    }

    public void cleanAir(BlockPos pos, float add) {
        Long posl = pos.toLong();
        Byte b = cleanAir.get(posl);
        int cleanPct = b == null ? 0 : (b & 0xff);
        cleanPct = (int) (cleanPct + add * 255);
        if (cleanPct > 255) {
            cleanPct = 255;
        }
        cleanAir.put(posl, (byte) cleanPct);
    }

    @Nonnull
    public static CleanAirManager getManager(World world) {
        if (world.isRemote) {
            throw new RuntimeException("Don't access this client-side!");
        }
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
