package mcjty.needtobreathe.config;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

public class PotionEffectConfig {

    private final int poisonThresshold;
    private final Potion potion;
    private final int amplitude;

    public PotionEffectConfig(String desc) {
        String[] split = StringUtils.split(desc, ',');
        if (split.length < 2) {
            throw new RuntimeException("Expected <poison>,<effect[@<amplitude>]>");
        }
        poisonThresshold = Integer.parseInt(split[0]);
        if (split[1].contains("@")) {
            split = StringUtils.split(split[1], '@');
            potion = Potion.REGISTRY.getObject(new ResourceLocation(split[0]));
            amplitude = Integer.parseInt(split[1]);
        } else {
            potion = Potion.REGISTRY.getObject(new ResourceLocation(split[1]));
            amplitude = 0;
        }
        if (potion == null) {
            throw new RuntimeException("Invalid potion descriptor: " + desc);
        }
    }

    public int getPoisonThresshold() {
        return poisonThresshold;
    }

    public Potion getPotion() {
        return potion;
    }

    public int getAmplitude() {
        return amplitude;
    }
}
