package superscary.mcr.revelations.recipe.type;

import net.minecraft.world.item.crafting.RecipeType;
import superscary.mcr.revelations.recipe.ChemicalMixerRecipe;

public class ChemicalMixerRecipeType implements RecipeType<ChemicalMixerRecipe>
{

    private ChemicalMixerRecipeType ()
    {

    }

    public static final ChemicalMixerRecipeType INSTANCE = new ChemicalMixerRecipeType();
    public static final String ID = "chemical_mixer";

}
