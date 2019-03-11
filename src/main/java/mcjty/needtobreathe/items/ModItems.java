package mcjty.needtobreathe.items;

import mcjty.needtobreathe.NeedToBreathe;
import mcjty.needtobreathe.compat.BaubleTools;
import mcjty.needtobreathe.setup.CommonSetup;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

    public static final String NTB_PROTECTIVE_TAG = "ntbProtectiveFactor";

    public static ProtectiveHelmet protectiveHelmet;
    public static ProtectiveHelmet2 protectiveHelmet2;
    public static InformationGlasses informationGlasses;
    public static HazmatSuit hazmatSuitHelmet;
    public static HazmatSuit hazmatSuitChest;
    public static HazmatSuit hazmatSuitLegs;
    public static HazmatSuit hazmatSuitBoots;
    public static Item insulatedLeather;
    public static Item protectiveBauble;

    public static void init() {
        protectiveHelmet = new ProtectiveHelmet();
        protectiveHelmet2 = new ProtectiveHelmet2();
        informationGlasses = new InformationGlasses();
        hazmatSuitBoots = new HazmatSuit(EntityEquipmentSlot.FEET);
        hazmatSuitChest = new HazmatSuit(EntityEquipmentSlot.CHEST);
        hazmatSuitHelmet = new HazmatSuit(EntityEquipmentSlot.HEAD);
        hazmatSuitLegs = new HazmatSuit(EntityEquipmentSlot.LEGS);
        insulatedLeather = new Item()
                .setRegistryName("insulated_leather")
                .setUnlocalizedName(NeedToBreathe.MODID + ".insulatedleather")
                .setCreativeTab(NeedToBreathe.setup.getTab());

        if (CommonSetup.baubles) {
            protectiveBauble = BaubleTools.initProtectionBauble();
        }
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        ModelLoader.setCustomModelResourceLocation(protectiveHelmet, 0, new ModelResourceLocation(protectiveHelmet.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(protectiveHelmet2, 0, new ModelResourceLocation(protectiveHelmet2.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(informationGlasses, 0, new ModelResourceLocation(informationGlasses.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(hazmatSuitBoots, 0, new ModelResourceLocation(hazmatSuitBoots.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(hazmatSuitChest, 0, new ModelResourceLocation(hazmatSuitChest.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(hazmatSuitHelmet, 0, new ModelResourceLocation(hazmatSuitHelmet.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(hazmatSuitLegs, 0, new ModelResourceLocation(hazmatSuitLegs.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(insulatedLeather, 0, new ModelResourceLocation(insulatedLeather.getRegistryName(), "inventory"));

        if (CommonSetup.baubles) {
            BaubleTools.initBaubleModel(protectiveBauble);
        }
    }

    public static boolean hasProbeInBauble(EntityPlayer player) {
        if (CommonSetup.baubles) {
            return BaubleTools.hasProtectiveBauble(player);
        } else {
            return false;
        }
    }

}
