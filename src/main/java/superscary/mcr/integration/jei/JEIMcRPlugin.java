package superscary.mcr.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import org.jetbrains.annotations.NotNull;
import superscary.mcr.McRMod;
import superscary.mcr.recipe.CompressorRecipe;
import superscary.mcr.recipe.InfuserRecipe;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEIMcRPlugin implements IModPlugin
{

    public static RecipeType<InfuserRecipe> INFUSION_TYPE = new RecipeType<>(InfusingRecipeCategory.UID, InfuserRecipe.class);
    public static RecipeType<CompressorRecipe> COMPRESSOR_TYPE = new RecipeType<>(CompressingRecipeCategory.UID, CompressorRecipe.class);
    public static RecipeType<SmeltingRecipe> E_FURNACE_TYPE = new RecipeType<>(ElectricFurnaceRecipeCategory.UID, SmeltingRecipe.class);

    @Override
    public @NotNull ResourceLocation getPluginUid ()
    {
        return new ResourceLocation(McRMod.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories (IRecipeCategoryRegistration registration)
    {
        registration.addRecipeCategories(new InfusingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CompressingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ElectricFurnaceRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes (IRecipeRegistration registration)
    {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<InfuserRecipe> recipesInfusing = rm.getAllRecipesFor(InfuserRecipe.Type.INSTANCE);
        registration.addRecipes(INFUSION_TYPE, recipesInfusing);

        List<CompressorRecipe> recipesCompressor = rm.getAllRecipesFor(CompressorRecipe.Type.INSTANCE);
        registration.addRecipes(COMPRESSOR_TYPE, recipesCompressor);

        List<SmeltingRecipe> recipesSmelting = rm.getAllRecipesFor(net.minecraft.world.item.crafting.RecipeType.SMELTING);
        registration.addRecipes(E_FURNACE_TYPE, recipesSmelting);

    }

}
