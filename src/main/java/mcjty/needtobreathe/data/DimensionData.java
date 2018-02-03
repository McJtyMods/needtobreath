package mcjty.needtobreathe.data;

import mcjty.needtobreathe.config.Config;
import mcjty.needtobreathe.config.PotionEffectConfig;
import mcjty.needtobreathe.items.InformationGlasses;
import mcjty.needtobreathe.items.ProtectiveHelmet;
import mcjty.needtobreathe.network.NTBMessages;
import mcjty.needtobreathe.network.PacketSendCleanAirToClient;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class DimensionData {


    public static final int MAXTICKS = 10;
    public static final int MAXEFFECTSTICKS = 5;
    public static final int EFFECT_DURATION = MAXTICKS * MAXEFFECTSTICKS * 2;

    private int counter = MAXTICKS;
    private int effectCounter = MAXEFFECTSTICKS;

    private final Map<SubChunkPos, ChunkData> cleanAir = new HashMap<>();       // 0 = no clean air, 255 = 100% clean



    private static int g_seed = 123456789;
    public static int fastrand128() {
        g_seed = (214013 * g_seed + 2531011);
        return (g_seed >> 16) & 0x7F;
    }

    // @todo not efficient!
    private Map<Long, Byte> getCleanAirAsLongByteMap() {
        Map<Long, Byte> map = new HashMap<>();
        for (Map.Entry<SubChunkPos, ChunkData> entry : cleanAir.entrySet()) {
            SubChunkPos chunkPos = entry.getKey();
            ChunkData data = entry.getValue();
            byte[] a = data.getData();
            for (int idx = 0 ; idx < 4096 ; idx++) {
                byte b = a[idx];
                if (b != 0) {
                    map.put(chunkPos.toPos(idx).toLong(), b);
                }
            }

        }
        return map;
    }

    /**
     * Get the minimum poison level for this position and adjacent positions
     */
    public int getPoison(BlockPos p) {
        int minPoison = 255;
        for (int dx = -1 ; dx <= 1 ; dx++) {
            for (int dy = -1 ; dy <= 1 ; dy++) {
                for (int dz = -1 ; dz <= 1 ; dz++) {
                    int poison = getPoisonInternal(p.getX()+dx, p.getY()+dy, p.getZ()+dz);
                    if (poison < minPoison) {
                        minPoison = poison;
                        if (minPoison == 0) {
                            return 0;
                        }
                    }
                }
            }
        }
        return Math.max(minPoison-Config.POISON_THRESSHOLD, 0);
    }

    private int getPoisonInternal(int x, int y, int z) {
        SubChunkPos chunkPos = SubChunkPos.fromPos(x, y, z);
        if (cleanAir.containsKey(chunkPos)) {
            ChunkData data = cleanAir.get(chunkPos);
            return data.getPoison(x, y, z);
        } else {
            return 255;
        }
    }

    private int getAirInternal(int x, int y, int z) {
        SubChunkPos chunkPos = SubChunkPos.fromPos(x, y, z);
        if (cleanAir.containsKey(chunkPos)) {
            ChunkData data = cleanAir.get(chunkPos);
            return data.getAir(x, y, z);
        } else {
            return 0;
        }
    }

    public boolean isValid(World world, BlockPos p) {
        IBlockState state = world.getBlockState(p);
        Block block = state.getBlock();

        if (Config.getBlocksBlocking().contains(block.getRegistryName())) {
            // Special case for doors
            if (block instanceof BlockDoor) {
                return state.getValue(BlockDoor.OPEN);
            }
            if (block instanceof BlockTrapDoor) {
                return state.getValue(BlockTrapDoor.OPEN);
            }

            return false;
        }
        if (Config.getBlocksNonBlocking().contains(block.getRegistryName())) {
            return true;
        }

        if (block.isAir(state, world, p)) {
            return true;
        } else {
            AxisAlignedBB box = state.getCollisionBoundingBox(world, p);
            if (box == null) {
                return true;
            }
            return !block.isOpaqueCube(state);
        }
    }


    public int fillCleanAir(BlockPos p) {
        return fillCleanAir(p.getX(), p.getY(), p.getZ());
    }

    public int fillCleanAir(int x, int y, int z) {
        SubChunkPos chunkPos = SubChunkPos.fromPos(x, y, z);
        int air;
        ChunkData data;
        if (cleanAir.containsKey(chunkPos)) {
            data = cleanAir.get(chunkPos);
            air = data.getAir(x, y, z);
        } else {
            data = new ChunkData();
            cleanAir.put(chunkPos, data);
            air = 0;
        }
        data.putAir(x, y, z, 255);
        return 255-air;
    }


    public void worldTick(World world) {
        counter--;
        if (counter <= 0) {
            counter = MAXTICKS;

            effectCounter--;
            if (effectCounter <= 0) {
                effectCounter = MAXEFFECTSTICKS;
                handleEffects(world);
            }

            tick(world);

            PacketSendCleanAirToClient message = new PacketSendCleanAirToClient(getCleanAirAsLongByteMap());
            for (EntityPlayer player : world.playerEntities) {
                ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (!helmet.isEmpty() && helmet.getItem() instanceof InformationGlasses) {
                    NTBMessages.INSTANCE.sendTo(message, (EntityPlayerMP) player);
                }
            }
        }
        CleanAirManager.getManager().save();
    }

    private void handleEffects(World world) {
        List<Pair<Integer, Entity>> affectedEntities = new ArrayList<>();
        for (Entity entity : world.loadedEntityList) {
            if (entity instanceof EntityLivingBase) {
                int poison = getPoison(entity.getPosition().up());
                if (poison > 20) {
                    affectedEntities.add(Pair.of(poison, entity));
                }
            }
        }

        for (Pair<Integer, Entity> pair : affectedEntities) {
            Entity entity = pair.getRight();
            Integer poison = pair.getLeft();

            PotionEffectConfig[] potionConfigs;

            if (entity instanceof EntityPlayer) {
                potionConfigs = Config.getPlayerEffects();
                EntityPlayer player = (EntityPlayer) entity;
                ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (!helmet.isEmpty() && helmet.getItem() instanceof ProtectiveHelmet) {
                    poison = (int) (poison * Config.PROTECTIVE_HELMET_FACTOR);
                }
            } else if (entity instanceof IMob) {
                potionConfigs = Config.getHostileEffects();
            } else {
                potionConfigs = Config.getPassiveEffects();
            }

            if (potionConfigs.length > 0) {
                for (PotionEffectConfig config : potionConfigs) {
                    if (poison >= config.getPoisonThresshold()) {
                        ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(config.getPotion(), EFFECT_DURATION, config.getAmplitude()));
                    }
                }
            }
        }
    }

    private static int[] distList = new int[7];
    private static byte[][] distListData = new byte[7][];


    private void tickSubChunk(World world, SubChunkPos chunkPos, ChunkData data) {
        byte[] a = data.getData();
        for (int dx = 1 ; dx < 15 ; dx++) {
            for (int dy = 1 ; dy < 15 ; dy++) {
                for (int dz = 1 ; dz < 15 ; dz++) {
                    int idx = ChunkData.index(dx, dy, dz);
                    int air = a[idx] & 0xff;
                    if (fastrand128() < Config.POISON_CRAWL_SPEED) {
                        air--;
                    }
                    BlockPos p = chunkPos.toPos(dx, dy, dz);
                    if (air < 5 || !isValid(world, p)) {
                        a[idx] = 0;
                    } else {
                        // Evenly distribute all air to the adjacent spots (and this one)
                        int totalAir = air;
                        int distListCnt = 0;
                        for (EnumFacing facing : EnumFacing.VALUES) {
                            BlockPos adjacent = p.offset(facing);
                            if (isValid(world, adjacent)) {
                                int idxAdjacent = ChunkData.offset(idx, facing);
                                totalAir += (int) a[idxAdjacent];
                                distList[distListCnt++] = idxAdjacent;
                            }
                        }

                        if (distListCnt > 0) {
                            // We distribute 'air' to all legal adjacent spaces (and this one)
                            air = totalAir / (distListCnt+1);
                            for (int i = 0 ; i < distListCnt ; i++) {
                                totalAir -= air;
                                a[distList[i]] = (byte) air;
                            }
                        }
                        a[idx] = (byte) totalAir;
                    }
                }
            }
        }
    }

    private ChunkData getChunkData(BlockPos pos) {
        SubChunkPos chunkPos = SubChunkPos.fromPos(pos);
        if (!cleanAir.containsKey(chunkPos)) {
            ChunkData data = new ChunkData();
            cleanAir.put(chunkPos, data);
        }
        return cleanAir.get(chunkPos);
    }

    private void tickSubChunkBorders(World world, SubChunkPos chunkPos, ChunkData data) {
        byte[] a = data.getData();
        for (int dx = 0 ; dx < 16 ; dx++) {
            for (int dy = 0 ; dy < 16 ; dy++) {
                for (int dz = 0; dz < 16; dz++) {
                    if (dx == 0 || dy == 0 || dz == 0 || dx == 15 || dy == 15 || dz == 15) {
                        int idx = ChunkData.index(dx, dy, dz);
                        int air = a[idx] & 0xff;
                        if (fastrand128() < Config.POISON_CRAWL_SPEED) {
                            air--;
                        }
                        BlockPos p = chunkPos.toPos(dx, dy, dz);
                        if (air < 5 || !isValid(world, p)) {
                            a[idx] = 0;
                        } else {
                            // Evenly distribute all air to the adjacent spots (and this one)
                            int totalAir = air;
                            int distListCnt = 0;
                            for (EnumFacing facing : EnumFacing.VALUES) {
                                BlockPos adjacent = p.offset(facing);
                                if (isValid(world, adjacent)) {
                                    byte[] d = a;
                                    int idxAdjacent = ChunkData.offsetWithCheck(idx, facing);
                                    if (idxAdjacent < 0) {
                                        idxAdjacent = -idxAdjacent;
                                        d = getChunkData(adjacent).getData();
                                        distListData[distListCnt] = d;
                                    } else {
                                        distListData[distListCnt] = a;
                                    }
                                    int adjacentAir = d[idxAdjacent];
                                    totalAir += adjacentAir;
                                    distList[distListCnt++] = idxAdjacent;
                                }
                            }

                            if (distListCnt > 0) {
                                // We distribute 'air' to all legal adjacent spaces (and this one)
                                air = totalAir / (distListCnt + 1);
                                for (int i = 0; i < distListCnt; i++) {
                                    totalAir -= air;
                                    distListData[i][distList[i]] = (byte) air;
                                }
                            }
                            a[idx] = (byte) totalAir;
                        }
                    }
                }
            }
        }
    }

    public void tick(World world) {
        Set<Map.Entry<SubChunkPos, ChunkData>> copy = new HashSet<>(cleanAir.entrySet());
        for (Map.Entry<SubChunkPos, ChunkData> entry : copy) {
            SubChunkPos chunkPos = entry.getKey();
            ChunkData data = entry.getValue();

            // First do the internals of each subchunk
            tickSubChunk(world, chunkPos, data);

            // Now the borders
            tickSubChunkBorders(world, chunkPos, data);
        }
    }


    public void readFromNBT(NBTTagCompound nbt) {
        cleanAir.clear();
        NBTTagList list = nbt.getTagList("list", Constants.NBT.TAG_COMPOUND);
        for (int i = 0 ; i < list.tagCount() ; i++) {
            NBTTagCompound subchunkNBT = list.getCompoundTagAt(i);
            SubChunkPos chunkPos = new SubChunkPos(subchunkNBT.getInteger("x"), subchunkNBT.getInteger("y"), subchunkNBT.getInteger("z"));
            byte[] data = subchunkNBT.getByteArray("data");
            cleanAir.put(chunkPos, new ChunkData(data));
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList();

        for (Map.Entry<SubChunkPos, ChunkData> entry : cleanAir.entrySet()) {
            NBTTagCompound subchunkNBT = new NBTTagCompound();
            subchunkNBT.setInteger("x", entry.getKey().getCx());
            subchunkNBT.setInteger("y", entry.getKey().getCy());
            subchunkNBT.setInteger("z", entry.getKey().getCz());
            subchunkNBT.setByteArray("data", entry.getValue().getData());
            list.appendTag(subchunkNBT);
        }
        compound.setTag("list", list);

        return compound;
    }
}
