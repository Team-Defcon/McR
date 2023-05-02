package superscary.mcr.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
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
import org.jetbrains.annotations.NotNull;
import superscary.mcr.McRMod;
import superscary.mcr.blocks.McRBlockReg;
import superscary.mcr.blocks.entity.InfuserBlockEntity;
import superscary.mcr.recipe.CompressorRecipe;
import superscary.mcr.recipe.InfuserRecipe;

import java.util.List;

public class CompressingRecipeCategory implements IRecipeCategory<CompressorRecipe>
{

    public static final ResourceLocation UID = new ResourceLocation(McRMod.MODID, "compress");
    public static final ResourceLocation TEXTURE = new ResourceLocation(McRMod.MODID, "textures/gui/compressor_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public CompressingRecipeCategory (IGuiHelper helper)
    {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(McRBlockReg.COMPRESSOR.get()));
    }

    @Override
    public @NotNull RecipeType<CompressorRecipe> getRecipeType ()
    {
        return JEIMcRPlugin.COMPRESSOR_TYPE;
    }

    @Override
    public @NotNull Component getTitle ()
    {
        return Component.translatable("gui.mcr.compressor");
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
    public void setRecipe (@NotNull IRecipeLayoutBuilder builder, @NotNull CompressorRecipe recipe, @NotNull IFocusGroup focuses)
    {
        builder.addSlot(RecipeIngredientRole.INPUT, 56, 35).addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 35).addItemStack(recipe.getResultItem(RegistryAccess.EMPTY));
    }
}
