package mcjty.needtobreathe.items;

import mcjty.needtobreathe.NeedToBreathe;
import mcjty.needtobreathe.api.IAirCanister;
import mcjty.needtobreathe.api.IProtectiveHelmet;
import mcjty.needtobreathe.config.Config;
import mcjty.needtobreathe.data.DimensionData;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class HazmatSuit extends ItemArmor implements IProtectiveHelmet, IAirCanister {

    public HazmatSuit(EntityEquipmentSlot slot) {
        super(ArmorMaterial.LEATHER, 0, slot);
        setRegistryName("hazmatsuit_" + slot.getName());
        setUnlocalizedName(NeedToBreathe.MODID + ".hazmatsuit_" + slot.getName());
        setCreativeTab(NeedToBreathe.setup.getTab());
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

    @Override
    public boolean isActive(EntityPlayer player) {
        return hasFullArmor(player);
    }

    @Override
    public int getReducedPoison(EntityPlayer player, int poison) {
        ItemStack chestplate = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        int air = ModItems.hazmatSuitChest.getAir(chestplate);
        if (air < 50) {    // @todo config
            if (DimensionData.fastrand128() < poison) {
                ModItems.hazmatSuitChest.setAir(chestplate, air - 1);
            }
            return (int) (poison * Config.PROTECTIVE_HELMET_FACTOR);
        } else if (air > 0) {
            if (DimensionData.fastrand128() < poison) {
                ModItems.hazmatSuitChest.setAir(chestplate, air - 1);
            }
            return 0;
        }
        return poison;
    }

    public static boolean hasFullArmor(EntityPlayer player) {
        if (player == null) {
            return false;
        }
        if (!(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof HazmatSuit)) {
            return false;
        }
        if (!(player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() instanceof HazmatSuit)) {
            return false;
        }
        if (!(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof HazmatSuit)) {
            return false;
        }
        if (!(player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() instanceof HazmatSuit)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isActive(ItemStack stack) {
        return stack.getItem() == ModItems.hazmatSuitChest;
    }

    @Override
    public int getAir(ItemStack stack) {
        if (stack.hasTagCompound()) {
            return stack.getTagCompound().getInteger("air");
        }
        return 0;
    }

    @Override
    public int getMaxAir(ItemStack stack) {
        return Config.HAZMATSUIT_MAXAIR;
    }

    @Override
    public void setAir(ItemStack stack, int air) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger("air", air);
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag b) {
        super.addInformation(stack, world, list, b);
        if (getEquipmentSlot() == EntityEquipmentSlot.CHEST) {
            list.add("Hazmat suit chestplate");
            list.add("This has to be filled in the air");
            list.add("compressor before it works!");
            list.add(TextFormatting.GREEN+"Air: " + getAir(stack) + " / " + getMaxAir(stack));
        } else {
            list.add("Hazmat suit part");
            list.add("The haxmat has to be complete");
            list.add("before it can work!");
        }
        if (hasFullArmor(NeedToBreathe.proxy.getClientPlayer())) {
            list.add(TextFormatting.YELLOW + "Suit is complete!");
        } else {
            list.add(TextFormatting.RED + "Suit is incomplete!");
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        if (getEquipmentSlot() == EntityEquipmentSlot.CHEST) {
            return true;
        }
        return super.showDurabilityBar(stack);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (getEquipmentSlot() == EntityEquipmentSlot.CHEST) {
            int max = getMaxAir(stack);
            return (max - getAir(stack)) / (double) max;
        }
        return super.getDurabilityForDisplay(stack);
    }


}
