package superscary.mcr.revelations.integration.jei;

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
import org.jetbrains.annotations.NotNull;
import superscary.mcr.revelations.Revelations;
import superscary.mcr.revelations.blocks.McRBlockReg;
import superscary.mcr.revelations.recipe.ExtruderRecipe;

public class ExtrudingRecipeCategory implements IRecipeCategory<ExtruderRecipe>
{

    public static final ResourceLocation UID = new ResourceLocation(Revelations.MODID, "extrude");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Revelations.MODID, "textures/gui/extrude_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public ExtrudingRecipeCategory (IGuiHelper helper)
    {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 80);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(McRBlockReg.EXTRUDER.get()));
    }

    @Override
    public @NotNull RecipeType<ExtruderRecipe> getRecipeType ()
    {
        return JEIMcRPlugin.EXTRUDER_TYPE;
    }

    @Override
    public @NotNull Component getTitle ()
    {
        return Component.translatable("gui.mcr.extruder");
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
    public void setRecipe (@NotNull IRecipeLayoutBuilder builder, @NotNull ExtruderRecipe recipe, @NotNull IFocusGroup focuses)
    {
        builder.addSlot(RecipeIngredientRole.INPUT, 56, 35).addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 35).addItemStack(recipe.getResultItem(RegistryAccess.EMPTY));
    }
}
