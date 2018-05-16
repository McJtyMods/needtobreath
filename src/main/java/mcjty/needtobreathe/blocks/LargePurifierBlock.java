package mcjty.needtobreathe.blocks;

import mcjty.lib.blocks.GenericBlock;
import mcjty.lib.container.EmptyContainer;
import mcjty.needtobreathe.NeedToBreathe;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class LargePurifierBlock extends GenericBlock<LargePurifierTileEntity, EmptyContainer> {

    @Override
    public RotationType getRotationType() {
        return RotationType.NONE;
    }

    public LargePurifierBlock() {
        super(NeedToBreathe.instance, Material.IRON, LargePurifierTileEntity.class, EmptyContainer::new, "large_purifier", true);
        setCreativeTab(NeedToBreathe.creativeTab);
    }

    @Override
    public int getGuiID() {
        return -1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, World player, List<String> list, ITooltipFlag whatIsThis) {
        super.addInformation(itemStack, player, list, whatIsThis);

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            list.add(TextFormatting.WHITE + "This creative-only block cleans");
            list.add(TextFormatting.WHITE + "air in a large area");
            list.add(TextFormatting.WHITE + "It can detect Lost City spheres");
        } else {
            list.add(TextFormatting.WHITE + NeedToBreathe.SHIFT_MESSAGE);
        }
    }
}
