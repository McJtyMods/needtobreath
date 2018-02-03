package mcjty.needtobreathe.data;

import mcjty.needtobreathe.config.Config;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class CleanAirManager extends WorldSavedData {

    public static final String NAME = "NeedToBreatheData";

    private static CleanAirManager instance = null;

    private Map<Integer, DimensionData> dimensionDataMap = new HashMap<>();



    public CleanAirManager(String name) {
        super(name);
    }


    public void save() {
        WorldServer world = DimensionManager.getWorld(0);
        world.setData(NAME, this);
        markDirty();
    }

    @Nullable
    public DimensionData getDimensionData(int dimension) {
        if (Config.hasPoison(dimension)) {
            if (!dimensionDataMap.containsKey(dimension)) {
                dimensionDataMap.put(dimension, new DimensionData());
            }
            return dimensionDataMap.get(dimension);
        } else {
            return null;
        }
    }

    public static void clearInstance() {
        if (instance != null) {
            instance.dimensionDataMap.clear();
            instance = null;
        }
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
        NBTTagList list = nbt.getTagList("dimlist", Constants.NBT.TAG_COMPOUND);
        for (int i = 0 ; i < list.tagCount() ; i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            int dimensionId = tag.getInteger("dimension");
            DimensionData data = new DimensionData();
            data.readFromNBT(tag);
            dimensionDataMap.put(dimensionId, data);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList();
        for (Map.Entry<Integer, DimensionData> entry : dimensionDataMap.entrySet()) {
            int dimensionId = entry.getKey();
            DimensionData data = entry.getValue();
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("dimension", dimensionId);
            data.writeToNBT(tag);
            list.appendTag(tag);
        }
        compound.setTag("dimlist", list);
        return compound;
    }
}
