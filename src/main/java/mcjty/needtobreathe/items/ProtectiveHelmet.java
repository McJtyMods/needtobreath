package mcjty.needtobreathe.items;

import mcjty.needtobreathe.NeedToBreathe;
import mcjty.needtobreathe.api.IProtectiveHelmet;
import mcjty.needtobreathe.config.ConfigSetup;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ProtectiveHelmet extends ItemArmor implements IProtectiveHelmet {

    public ProtectiveHelmet() {
        super(ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.HEAD);
        setRegistryName("protectivehelmet");
        setUnlocalizedName(NeedToBreathe.MODID + ".protectivehelmet");
        setCreativeTab(NeedToBreathe.setup.getTab());
    }

    @Override
    public void addInformation(ItemStack itemStack, World player, List<String> list, ITooltipFlag advancedToolTip) {
        super.addInformation(itemStack, player, list, advancedToolTip);
        list.add("If you wear this helmet you will get,");
        list.add("some protection against the poisonous");
        list.add("atmosphere");
        list.add(TextFormatting.YELLOW + "Poison reduction: " + TextFormatting.BLUE + (int) (100 - (ConfigSetup.PROTECTIVE_BAUBLE_FACTOR * 100)) + "%");
    }
    
    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return NeedToBreathe.MODID + ":textures/armor/helmet_1.png";
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        return ProtectiveHelmetModel.getModel(entityLiving, itemStack);
    }

    @Override
    public boolean isActive(EntityPlayer player) {
        return true;
    }

    @Override
    public int getReducedPoison(EntityPlayer player, int poison) {
        return (int) (poison * ConfigSetup.PROTECTIVE_HELMET_FACTOR);
    }
}
