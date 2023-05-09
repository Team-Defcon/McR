package superscary.mcr.revelations.recipe.type;

import net.minecraft.world.item.crafting.RecipeType;
import superscary.mcr.revelations.recipe.ForgeRecipe;

public class ForgeRecipeType implements RecipeType<ForgeRecipe>
{

    private ForgeRecipeType ()
    {}

    public static final ForgeRecipeType INSTANCE = new ForgeRecipeType();
    public static final String ID = "forge";

}
