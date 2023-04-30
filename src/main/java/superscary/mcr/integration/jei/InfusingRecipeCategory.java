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
import superscary.mcr.recipe.InfuserRecipe;

import java.util.List;

public class InfusingRecipeCategory implements IRecipeCategory<InfuserRecipe>
{

    public static final ResourceLocation UID = new ResourceLocation(McRMod.MODID, "infusing");
    public static final ResourceLocation TEXTURE = new ResourceLocation(McRMod.MODID, "textures/gui/infuser_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public InfusingRecipeCategory (IGuiHelper helper)
    {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(McRBlockReg.INFUSER.get()));
    }

    @Override
    public @NotNull RecipeType<InfuserRecipe> getRecipeType ()
    {
        return JEIMcRPlugin.INFUSION_TYPE;
    }

    @Override
    public @NotNull Component getTitle ()
    {
        return Component.translatable("gui.mcr.infuser");
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
    public void setRecipe (@NotNull IRecipeLayoutBuilder builder, @NotNull InfuserRecipe recipe, @NotNull IFocusGroup focuses)
    {
        builder.addSlot(RecipeIngredientRole.INPUT, 86, 15).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 55, 15).addIngredients(ForgeTypes.FLUID_STACK, List.of(recipe.getFluid()))
                                                                 .setFluidRenderer(InfuserBlockEntity.TANK_CAPACITY, true, 16, 61);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 86, 60).addItemStack(recipe.getResultItem(RegistryAccess.EMPTY));
    }
}
