package mcjty.needtobreathe.api;

import net.minecraft.item.ItemStack;

/**
 * Implement this on an item if the item is supposed to be able to contain air
 */
public interface IAirCanister {

    /**
     * Return true if this stack can hold air. Not all items that implement IAirCanister
     * can actually hold air. For example, for a full hazmat suit only the chestpiece supports
     * air but all are the same item class
     * @param stack to check if we are the right item
     */
    boolean isActive(ItemStack stack);

    /**
     * Current air value
     */
    int getAir(ItemStack stack);

    /**
     * Maximum support air value
     */
    int getMaxAir(ItemStack stack);

    void setAir(ItemStack stack, int air);
}
