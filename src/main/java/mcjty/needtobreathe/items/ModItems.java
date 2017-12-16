package mcjty.needtobreathe.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

    public static ProtectiveHelmet protectiveHelmet;
    public static InformationGlasses informationGlasses;

    public static void init() {
        protectiveHelmet = new ProtectiveHelmet();
        informationGlasses = new InformationGlasses();
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        ModelLoader.setCustomModelResourceLocation(protectiveHelmet, 0, new ModelResourceLocation(protectiveHelmet.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(informationGlasses, 0, new ModelResourceLocation(informationGlasses.getRegistryName(), "inventory"));
    }
}
