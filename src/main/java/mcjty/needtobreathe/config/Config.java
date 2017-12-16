package mcjty.needtobreathe.config;

import net.minecraftforge.common.config.Configuration;

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

    public static String[] POTION_EFFECTS_PLAYER = { "30,minecraft:weakness", "60,minecraft:slowness", "150,minecraft:poison", "210,minecraft:wither", "250,minecraft:instant_damage@1000" };
    public static String[] POTION_EFFECTS_PASSIVE = { "30,minecraft:weakness", "60,minecraft:slowness", "150,minecraft:poison" };
    public static String[] POTION_EFFECTS_HOSTILE = { "100,minecraft:regeneration", "200,minecraft:health_boost" };

    private static PotionEffectConfig[] playerEffects = null;
    private static PotionEffectConfig[] passiveEffects = null;
    private static PotionEffectConfig[] hostileEffects = null;

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
    }

    private static void initEffectsSettings(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_EFFECTS, "Effect settings");
        POTION_EFFECTS_PLAYER = cfg.getStringList("potionEffectsPlayer", CATEGORY_EFFECTS, POTION_EFFECTS_PLAYER, "A list of potion effects with every string of the form: 'amount,id[@amplitude]'");
        POTION_EFFECTS_PASSIVE = cfg.getStringList("potionEffectsPassive", CATEGORY_EFFECTS, POTION_EFFECTS_PASSIVE, "A list of potion effects with every string of the form: 'amount,id[@amplitude]'");
        POTION_EFFECTS_HOSTILE = cfg.getStringList("potionEffectsHostile", CATEGORY_EFFECTS, POTION_EFFECTS_HOSTILE, "A list of potion effects with every string of the form: 'amount,id[@amplitude]'");
        PROTECTIVE_HELMET_FACTOR = cfg.getFloat("protectiveHelmetFactor", CATEGORY_MACHINES, PROTECTIVE_HELMET_FACTOR, 0, 1, "How much the protective helmet reduces the poison. 0 means full poison reduction, 1 means no effect");
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
