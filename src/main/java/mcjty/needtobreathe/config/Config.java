package mcjty.needtobreathe.config;

import mcjty.lib.varia.Logging;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

public class Config {

    private static final String CATEGORY_GENERAL = "general";
    private static final String CATEGORY_MACHINES = "machines";
    private static final String CATEGORY_EFFECTS = "effects";

    public static int PURIFIER_MAXRF = 50000;
    public static int PURIFIER_RFINPUTPERTICK = 500;
    public static int PURIFIER_RFPERTICK = 50;
    public static int PURIFIER_TICKSPERCOAL = 30*20;
    public static int PURIFIER_MAXCOALTICKS = PURIFIER_TICKSPERCOAL * 18;

    public static float PROTECTIVE_HELMET_FACTOR = 0.4f;

    public static int PLANT_GROWTH_POISON_DENY = 150;
    public static int PLANT_GROWTH_POISON_SLOW = 70;
    public static float PLANT_GROWTH_SLOWDOWN_FACTOR = .7f;

    public static String[] POTION_EFFECTS_PLAYER = { "40,minecraft:weakness", "60,minecraft:slowness", "150,minecraft:poison", "210,minecraft:wither", "250,minecraft:instant_damage@1000" };
    public static String[] POTION_EFFECTS_PASSIVE = { "40,minecraft:weakness", "60,minecraft:slowness", "150,minecraft:poison" };
    public static String[] POTION_EFFECTS_HOSTILE = { "100,minecraft:regeneration", "200,minecraft:health_boost" };

    private static String[] BLOCKS_BLOCKING = {
            "minecraft:iron_door",
            "minecraft:iron_trapdoor"
    };
    private static String[] BLOCKS_NONBLOCKING = {
            "minecraft:wooden_door",
            "minecraft:spruce_door",
            "minecraft:birch_door",
            "minecraft:jungle_door",
            "minecraft:acacia_door",
            "minecraft:dark_oak_door",
            "minecraft:gravel",
            "minecraft:sand",
            "minecraft:ladder",
            "minecraft:trapdoor",
            "minecraft:hay_block"
    };

    public static String[] DIMENSIONS_WITH_POISON = { "-1" };
    public static String[] DIMENSIONS_WITHOUT_POISON = { };

    private static PotionEffectConfig[] playerEffects = null;
    private static PotionEffectConfig[] passiveEffects = null;
    private static PotionEffectConfig[] hostileEffects = null;

    private static boolean allHavePoison = false;
    private static Set<Integer> dimensionsWithPoison = null;
    private static Set<Integer> dimensionsWithoutPoison = null;

    private static Set<ResourceLocation> blocksBlocking = null;
    private static Set<ResourceLocation> blocksNonBlocking = null;

    public static boolean hasPoison(int dimensionId) {
        if (dimensionsWithPoison == null) {
            dimensionsWithPoison = new HashSet<>();
            for (String s : DIMENSIONS_WITH_POISON) {
                s = s.toLowerCase();
                if ("all".equals(s)) {
                    allHavePoison = true;
                    break;
                }
                dimensionsWithPoison.add(Integer.parseInt(s));
            }
            dimensionsWithoutPoison = new HashSet<>();
            for (String s : DIMENSIONS_WITHOUT_POISON) {
                dimensionsWithoutPoison.add(Integer.parseInt(s));
            }
        }
        if (allHavePoison) {
            return !dimensionsWithoutPoison.contains(dimensionId);
        } else {
            return dimensionsWithPoison.contains(dimensionId) && !dimensionsWithoutPoison.contains(dimensionId);
        }
    }


    public static Set<ResourceLocation> getBlocksBlocking() {
        if (blocksBlocking == null) {
            blocksBlocking = new HashSet<>();
            for (String s : BLOCKS_BLOCKING) {
                ResourceLocation id = new ResourceLocation(s);
                if (ForgeRegistries.BLOCKS.containsKey(id)) {
                    blocksBlocking.add(id);
                } else {
                    Logging.getLogger().warn("Block with id '" + s + "' is missing!");
                }
            }
        }
        return blocksBlocking;
    }

    public static Set<ResourceLocation> getBlocksNonBlocking() {
        if (blocksNonBlocking == null) {
            blocksNonBlocking = new HashSet<>();
            for (String s : BLOCKS_NONBLOCKING) {
                ResourceLocation id = new ResourceLocation(s);
                if (ForgeRegistries.BLOCKS.containsKey(id)) {
                    blocksNonBlocking.add(id);
                } else {
                    Logging.getLogger().warn("Block with id '" + s + "' is missing!");
                }
            }
        }
        return blocksNonBlocking;
    }

    public static PotionEffectConfig[] getPlayerEffects() {
        if (playerEffects == null) {
            playerEffects = new PotionEffectConfig[POTION_EFFECTS_PLAYER.length];
            for (int i = 0 ; i < POTION_EFFECTS_PLAYER.length ; i++) {
                playerEffects[i] = new PotionEffectConfig(POTION_EFFECTS_PLAYER[i]);
            }
        }
        return playerEffects;
    }

    public static PotionEffectConfig[] getPassiveEffects() {
        if (passiveEffects == null) {
            passiveEffects = new PotionEffectConfig[POTION_EFFECTS_PASSIVE.length];
            for (int i = 0 ; i < POTION_EFFECTS_PASSIVE.length ; i++) {
                passiveEffects[i] = new PotionEffectConfig(POTION_EFFECTS_PASSIVE[i]);
            }
        }
        return passiveEffects;
    }

    public static PotionEffectConfig[] getHostileEffects() {
        if (hostileEffects == null) {
            hostileEffects = new PotionEffectConfig[POTION_EFFECTS_HOSTILE.length];
            for (int i = 0 ; i < POTION_EFFECTS_HOSTILE.length ; i++) {
                hostileEffects[i] = new PotionEffectConfig(POTION_EFFECTS_HOSTILE[i]);
            }
        }
        return hostileEffects;
    }


    public static void readConfig(Configuration cfg) {
        cfg.load();

        initGeneralSettings(cfg);
        initMachineSettings(cfg);
        initEffectsSettings(cfg);
    }

    private static void initGeneralSettings(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General settings");
        BLOCKS_BLOCKING = cfg.getStringList("blocksBlocking", CATEGORY_GENERAL, BLOCKS_BLOCKING, "List of blocks that block poison");
        BLOCKS_NONBLOCKING = cfg.getStringList("blocksNonBlocking", CATEGORY_GENERAL, BLOCKS_NONBLOCKING, "List of blocks that don't block poison");
    }

    private static void initEffectsSettings(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_EFFECTS, "Effect settings");
        POTION_EFFECTS_PLAYER = cfg.getStringList("potionEffectsPlayer", CATEGORY_EFFECTS, POTION_EFFECTS_PLAYER, "A list of potion effects with every string of the form: 'amount,id[@amplitude]'");
        POTION_EFFECTS_PASSIVE = cfg.getStringList("potionEffectsPassive", CATEGORY_EFFECTS, POTION_EFFECTS_PASSIVE, "A list of potion effects with every string of the form: 'amount,id[@amplitude]'");
        POTION_EFFECTS_HOSTILE = cfg.getStringList("potionEffectsHostile", CATEGORY_EFFECTS, POTION_EFFECTS_HOSTILE, "A list of potion effects with every string of the form: 'amount,id[@amplitude]'");
        PROTECTIVE_HELMET_FACTOR = cfg.getFloat("protectiveHelmetFactor", CATEGORY_EFFECTS, PROTECTIVE_HELMET_FACTOR, 0, 1, "How much the protective helmet reduces the poison. 0 means full poison reduction, 1 means no effect");
        DIMENSIONS_WITH_POISON = cfg.getStringList("dimensionsWithPoison", CATEGORY_EFFECTS, DIMENSIONS_WITH_POISON, "List of dimensions where the air is poisonous. Use 'all' if you want all of them");
        DIMENSIONS_WITHOUT_POISON = cfg.getStringList("dimensionsWithoutPoison", CATEGORY_EFFECTS, DIMENSIONS_WITHOUT_POISON, "List of dimensions where the air is not poisonous. Used when 'dimensionsWithPoison' is equal to 'all'");

        PLANT_GROWTH_POISON_DENY = cfg.getInt("plantGrowthPoisonDeny", CATEGORY_EFFECTS, PLANT_GROWTH_POISON_DENY, 0, 256, "If poison is above this level plants cannot grow (256 to disable)");
        PLANT_GROWTH_POISON_SLOW = cfg.getInt("plantGrowthPoisonSlow", CATEGORY_EFFECTS, PLANT_GROWTH_POISON_SLOW, 0, 256, "If poison is above this level plants grow slower (256 to disable)");
        PLANT_GROWTH_SLOWDOWN_FACTOR = cfg.getFloat("plantGrowthPoisonSlowFactor", CATEGORY_EFFECTS, PLANT_GROWTH_SLOWDOWN_FACTOR, 0, 1, "How much is plant growth made slower. 0 means no slow down, 1 means no growth ever");
    }

    private static void initMachineSettings(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_MACHINES, "Machine settings");
        PURIFIER_MAXRF = cfg.getInt("purifierMaxRF", CATEGORY_MACHINES, PURIFIER_MAXRF, 1, 2000000000, "Maximum RF the purifier machine can keep in its internal buffer");
        PURIFIER_RFPERTICK = cfg.getInt("purifierRFPerTick", CATEGORY_MACHINES, PURIFIER_RFPERTICK, 0, 2000000000, "RF Per tick the purifier needs to run");
        PURIFIER_RFINPUTPERTICK = cfg.getInt("purifierRFInputPerTick", CATEGORY_MACHINES, PURIFIER_RFINPUTPERTICK, 0, 2000000000, "RF Per tick the purifier can input from one side");
        PURIFIER_TICKSPERCOAL = cfg.getInt("purifierTicksPerCoal", CATEGORY_MACHINES, PURIFIER_TICKSPERCOAL, 1, 2000000000, "How many ticks a single piece of coal can purify");
        PURIFIER_MAXCOALTICKS = cfg.getInt("purifierMaxCoalTicks", CATEGORY_MACHINES, PURIFIER_MAXCOALTICKS, 1, 2000000000, "Maximum coal tick capacity. Should be more then 'purifierTicksPerCoal'!");
    }
}
