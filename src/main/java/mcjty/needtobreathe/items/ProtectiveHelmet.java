package mcjty.needtobreathe.items;

import mcjty.needtobreathe.NeedToBreathe;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ProtectiveHelmet extends ItemArmor {

    public ProtectiveHelmet() {
        super(ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.HEAD);
        setRegistryName("protectivehelmet");
        setUnlocalizedName(NeedToBreathe.MODID + ".protectivehelmet");
        setCreativeTab(NeedToBreathe.creativeTab);
    }

    @Override
    public void addInformation(ItemStack itemStack, World player, List<String> list, ITooltipFlag advancedToolTip) {
        super.addInformation(itemStack, player, list, advancedToolTip);
        list.add("If you were this helmet you will get,");
        list.add("some protection against the poisonous");
        list.add("atmosphere");
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return NeedToBreathe.MODID+":textures/armor/helmet_1.png";
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        return ProtectiveHelmetModel.getModel(entityLiving, itemStack);
    }


}
