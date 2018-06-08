package mcjty.needtobreathe.compat;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import mcjty.needtobreathe.items.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaubleTools {

    public static boolean hasProtectiveBauble(EntityPlayer player) {
        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
        if (handler == null) {
            return false;
        }
        ItemStack stackInSlot = handler.getStackInSlot(4);
        if (!stackInSlot.isEmpty() && stackInSlot.getItem() == ModItems.protectiveBauble) {
            return true;
        }
        return false;
    }

    public static Item initProtectionBauble() {
        return new ProtectionBauble();
    }

    @SideOnly(Side.CLIENT)
    public static void initBaubleModel(Item bauble) {
        ((ProtectionBauble) bauble).initModel();
    }

}
