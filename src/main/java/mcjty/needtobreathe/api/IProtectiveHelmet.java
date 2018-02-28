package mcjty.needtobreathe.api;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Implement this on a helmet if the helmet is part of a protective suit (or standalone)
 */
public interface IProtectiveHelmet {

    /**
     * Return true if the protective effects of the suit is active
     */
    boolean isActive(EntityPlayer player);

    /**
     * Reduce poison
     */
    int getReducedPoison(EntityPlayer player, int poison);
}
