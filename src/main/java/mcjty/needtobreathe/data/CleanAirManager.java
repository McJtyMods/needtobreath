package mcjty.needtobreathe.data;

import mcjty.lib.worlddata.AbstractWorldData;
import mcjty.needtobreathe.config.ConfigSetup;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class CleanAirManager extends AbstractWorldData<CleanAirManager> {

    private static final String NAME = "NeedToBreatheData";
    private Map<Integer, DimensionData> dimensionDataMap = new HashMap<>();

    public CleanAirManager(String name) {
        super(name);
    }

    @Override
    public void clear() {
        dimensionDataMap.clear();
    }

    @Nullable
    public DimensionData getDimensionData(int dimension) {
        if (ConfigSetup.hasPoison(dimension)) {
            if (!dimensionDataMap.containsKey(dimension)) {
                dimensionDataMap.put(dimension, new DimensionData());
            }
            return dimensionDataMap.get(dimension);
        } else {
            return null;
        }
    }

    @Nonnull
    public static CleanAirManager getManager() {
        return getData(CleanAirManager.class, NAME);
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
