package superscary.mcr.revelations.integration.jei;

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
import superscary.mcr.revelations.Revelations;
import superscary.mcr.revelations.recipe.CompressorRecipe;
import superscary.mcr.revelations.recipe.ExtruderRecipe;
import superscary.mcr.revelations.recipe.InfuserRecipe;
import superscary.mcr.revelations.recipe.type.ModRecipeTypes;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEIMcRPlugin implements IModPlugin
{

    public static RecipeType<InfuserRecipe> INFUSION_TYPE = new RecipeType<>(InfusingRecipeCategory.UID, InfuserRecipe.class);
    public static RecipeType<CompressorRecipe> COMPRESSOR_TYPE = new RecipeType<>(CompressingRecipeCategory.UID, CompressorRecipe.class);
    public static RecipeType<SmeltingRecipe> E_FURNACE_TYPE = new RecipeType<>(ElectricFurnaceRecipeCategory.UID, SmeltingRecipe.class);
    public static RecipeType<ExtruderRecipe> EXTRUDER_TYPE = new RecipeType<>(ExtrudingRecipeCategory.UID, ExtruderRecipe.class);

    public JEIMcRPlugin ()
    {

    }

    @Override
    public @NotNull ResourceLocation getPluginUid ()
    {
        return new ResourceLocation(Revelations.MODID, "mcr");
    }

    @Override
    public void registerCategories (IRecipeCategoryRegistration registration)
    {
        registration.addRecipeCategories(new InfusingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CompressingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ElectricFurnaceRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ExtrudingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes (IRecipeRegistration registration)
    {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<InfuserRecipe> recipesInfusing = rm.getAllRecipesFor(ModRecipeTypes.INFUSER.get());
        registration.addRecipes(INFUSION_TYPE, recipesInfusing);

        List<CompressorRecipe> recipesCompressor = rm.getAllRecipesFor(ModRecipeTypes.COMPRESSOR.get());
        registration.addRecipes(COMPRESSOR_TYPE, recipesCompressor);

        List<SmeltingRecipe> recipesSmelting = rm.getAllRecipesFor(net.minecraft.world.item.crafting.RecipeType.SMELTING);
        registration.addRecipes(E_FURNACE_TYPE, recipesSmelting);

        List<ExtruderRecipe> recipesExtruder = rm.getAllRecipesFor(ModRecipeTypes.EXTRUDER.get());
        registration.addRecipes(EXTRUDER_TYPE, recipesExtruder);

    }

}
