package superscary.mcr.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import org.jetbrains.annotations.NotNull;
import superscary.mcr.McRMod;
import superscary.mcr.blocks.McRBlockReg;
import superscary.mcr.items.McRItemReg;
import superscary.mcr.recipe.CompressorRecipe;

public class ElectricFurnaceRecipeCategory implements IRecipeCategory<SmeltingRecipe>
{

    public static final ResourceLocation UID = new ResourceLocation(McRMod.MODID, "smelting");
    public static final ResourceLocation TEXTURE = new ResourceLocation(McRMod.MODID, "textures/gui/electric_furnace_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public ElectricFurnaceRecipeCategory (IGuiHelper helper)
    {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(McRBlockReg.ELECTRIC_FURNACE.get()));
    }

    @Override
    public @NotNull RecipeType<SmeltingRecipe> getRecipeType ()
    {
        return JEIMcRPlugin.E_FURNACE_TYPE;
    }

    @Override
    public @NotNull Component getTitle ()
    {
        return Component.translatable("gui.mcr.electric_furnace");
    }

    @Override
    public @NotNull IDrawable getBackground ()
    {
        return this.background;
    }

    @Override
    public @NotNull IDrawable getIcon ()
    {
        return this.icon;
    }

    @Override
    public void setRecipe (@NotNull IRecipeLayoutBuilder builder, @NotNull SmeltingRecipe recipe, @NotNull IFocusGroup focuses)
    {
        builder.addSlot(RecipeIngredientRole.INPUT, 56, 35).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 35).addItemStack(recipe.getResultItem(RegistryAccess.EMPTY));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 61).addItemStack(new ItemStack(McRItemReg.SLAG.get()));
    }
}
