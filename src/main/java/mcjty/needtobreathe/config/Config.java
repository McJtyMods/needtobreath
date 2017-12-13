package mcjty.needtobreathe.config;

import net.minecraftforge.common.config.Configuration;

public class Config {

    private static final String CATEGORY_GENERAL = "general";
    private static final String CATEGORY_MACHINES = "machines";

    public static int PURIFIER_MAXRF = 50000;
    public static int PURIFIER_RFINPUTPERTICK = 500;
    public static int PURIFIER_RFPERTICK = 50;
    public static int PURIFIER_TICKSPERCOAL = 30*20;
    public static int PURIFIER_MAXCOALTICKS = PURIFIER_TICKSPERCOAL * 18;


    public static void readConfig(Configuration cfg) {
        cfg.load();

        initGeneralSettings(cfg);
        initMachineSettings(cfg);
    }

    private static void initGeneralSettings(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General settings");
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
