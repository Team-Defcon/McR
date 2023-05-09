package superscary.mcr.revelations.recipe.type;

import net.minecraft.world.item.crafting.RecipeType;
import superscary.mcr.revelations.recipe.ExtruderRecipe;

public class ExtruderRecipeType implements RecipeType<ExtruderRecipe>
{

    private ExtruderRecipeType ()
    {

    }

    public static final ExtruderRecipeType INSTANCE = new ExtruderRecipeType();
    public static final String ID = "extrude";

}
