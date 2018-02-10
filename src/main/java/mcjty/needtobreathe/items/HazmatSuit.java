package mcjty.needtobreathe.items;

import mcjty.needtobreathe.NeedToBreathe;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class HazmatSuit extends ItemArmor {
    public HazmatSuit(EntityEquipmentSlot slot) {
        super(ArmorMaterial.LEATHER, 0, slot);
        setRegistryName("hazmatsuit_" + slot.getName());
        setUnlocalizedName(NeedToBreathe.MODID + ".hazmatsuit_" + slot.getName());
        setCreativeTab(NeedToBreathe.creativeTab);
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return NeedToBreathe.MODID + ":textures/armor/suit.png";
    }

    @Nullable
    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        return HazmatSuitModel.getModel(entityLiving, itemStack);
    }
}
