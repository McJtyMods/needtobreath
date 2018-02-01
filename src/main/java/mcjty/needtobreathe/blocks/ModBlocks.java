package mcjty.needtobreathe.blocks;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {

    public static PurifierBlock purifierBlock;
    public static LargePurifierBlock largePurifierBlock;

    public static void init() {
        purifierBlock = new PurifierBlock();
        largePurifierBlock = new LargePurifierBlock();
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        purifierBlock.initModel();
        largePurifierBlock.initModel();
    }
}
