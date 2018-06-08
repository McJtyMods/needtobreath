package mcjty.needtobreathe.items;

import com.google.gson.JsonObject;
import mcjty.needtobreathe.NeedToBreathe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;

public class ProtectiveHelmetRecipeFactory implements IRecipeFactory {

    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {
        ShapelessOreRecipe recipe = ShapelessOreRecipe.factory(context, json);

        float factor = JsonUtils.getFloat(json, "factor");
        return new HelmetRecipe(new ResourceLocation(NeedToBreathe.MODID, "protective_helmet"), recipe.getRecipeOutput(), recipe.getIngredients(), factor);
    }

    public static class HelmetRecipe extends ShapelessOreRecipe {
        private final float factor;

        public HelmetRecipe(ResourceLocation group, ItemStack result, NonNullList<Ingredient> ingredients, float factor) {
            super(group, ingredients, result);
            this.factor = factor;
        }

        @Nonnull
        @Override
        public ItemStack getCraftingResult(@Nonnull InventoryCrafting inventory) {
            ItemStack result = super.getCraftingResult(inventory);
            NBTTagCompound tc = new NBTTagCompound();
            tc.setFloat(ModItems.NTB_PROTECTIVE_TAG, factor);
            result.setTagCompound(tc);
            return result;
        }
    }
}
