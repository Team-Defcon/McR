package superscary.mcr.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;
import superscary.mcr.McRMod;
import superscary.mcr.recipe.InfuserRecipe;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEIMcRPlugin implements IModPlugin
{

    public static RecipeType<InfuserRecipe> INFUSION_TYPE = new RecipeType<>(InfusingRecipeCategory.UID, InfuserRecipe.class);

    @Override
    public @NotNull ResourceLocation getPluginUid ()
    {
        return new ResourceLocation(McRMod.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories (IRecipeCategoryRegistration registration)
    {
        registration.addRecipeCategories(new InfusingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes (IRecipeRegistration registration)
    {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<InfuserRecipe> recipesInfusing = rm.getAllRecipesFor(InfuserRecipe.Type.INSTANCE);
        registration.addRecipes(INFUSION_TYPE, recipesInfusing);
    }

}
