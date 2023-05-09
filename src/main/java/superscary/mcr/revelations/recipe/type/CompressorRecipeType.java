package superscary.mcr.revelations.recipe.type;

import net.minecraft.world.item.crafting.RecipeType;
import superscary.mcr.revelations.recipe.CompressorRecipe;

public class CompressorRecipeType implements RecipeType<CompressorRecipe>
{

    private CompressorRecipeType ()
    {

    }

    public static final CompressorRecipeType INSTANCE = new CompressorRecipeType();
    public static final String ID = "compress";

}
