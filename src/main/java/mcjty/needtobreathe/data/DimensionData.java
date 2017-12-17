package mcjty.needtobreathe.data;

import mcjty.needtobreathe.config.Config;
import mcjty.needtobreathe.config.PotionEffectConfig;
import mcjty.needtobreathe.items.InformationGlasses;
import mcjty.needtobreathe.items.ProtectiveHelmet;
import mcjty.needtobreathe.network.NTBMessages;
import mcjty.needtobreathe.network.PacketSendCleanAirToClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class DimensionData {


    public static final int MAXTICKS = 10;
    public static final int MAXEFFECTSTICKS = 5;
    public static final int EFFECT_DURATION = MAXTICKS * MAXEFFECTSTICKS * 2;

    private int counter = MAXTICKS;
    private int effectCounter = MAXEFFECTSTICKS;

    private final Map<Long, Byte> cleanAir = new HashMap<>();       // 0 = no clean air, 255 = 100% clean

    public Map<Long, Byte> getCleanAir() {
        return cleanAir;
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


    public int fillCleanAir(long p) {
        Byte b = cleanAir.get(p);
        cleanAir.put(p, (byte) 255);
        if (b == null) {
            return 255;
        }
        return 255-b;
    }


    public void worldTick(World world, CleanAirManager manager) {
        counter--;
        if (counter <= 0) {
            counter = MAXTICKS;
            tick(world);

            effectCounter--;
            if (effectCounter <= 0) {
                effectCounter = MAXEFFECTSTICKS;
                handleEffects(world);
            }

            PacketSendCleanAirToClient message = new PacketSendCleanAirToClient(getCleanAir());
            for (EntityPlayer player : world.playerEntities) {
                ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (!helmet.isEmpty() && helmet.getItem() instanceof InformationGlasses) {
                    NTBMessages.INSTANCE.sendTo(message, (EntityPlayerMP) player);
                }
            }
        }
        manager.save();
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


    public void tick(World world) {
        Set<Long> positions = new HashSet<>(cleanAir.keySet());
        for (Long pos : positions) {
            int air = getAir(pos);
            air--;
            if (air < 5 || !isValid(world, pos)) {
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
                }
                cleanAir.put(pos, (byte) air);
            }
        }
    }


    public void readFromNBT(NBTTagCompound nbt) {
        int[] posarray = nbt.getIntArray("airpos");
        byte[] airarray = nbt.getByteArray("airval");
        cleanAir.clear();
        for (int i = 0 ; i < airarray.length ; i++) {
            int x = posarray[i*3+0];
            int y = posarray[i*3+1];
            int z = posarray[i*3+2];
            cleanAir.put(LongPos.toLong(x, y, z), airarray[i]);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        int posarray[] = new int[cleanAir.size() * 3];
        byte airarray[] = new byte[cleanAir.size()];

        int idx = 0;
        for (Map.Entry<Long, Byte> entry : cleanAir.entrySet()) {
            BlockPos pos = BlockPos.fromLong(entry.getKey());
            airarray[idx] = entry.getValue();
            posarray[idx*3+0] = pos.getX();
            posarray[idx*3+1] = pos.getY();
            posarray[idx*3+2] = pos.getZ();
            idx++;
        }
        compound.setIntArray("airpos", posarray);
        compound.setByteArray("airval", airarray);

        return null;
    }
}
