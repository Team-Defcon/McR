package superscary.mcr.revelations.recipe.type;

import net.minecraft.world.item.crafting.RecipeType;
import superscary.mcr.revelations.recipe.InfuserRecipe;

public class InfuserRecipeType implements RecipeType<InfuserRecipe>
{

    private InfuserRecipeType ()
    {

    }

    public static final InfuserRecipeType INSTANCE = new InfuserRecipeType();
    public static final String ID = "infusing";

}
