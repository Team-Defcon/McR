package superscary.mcr.revelations.recipe.type;

import net.minecraft.world.item.crafting.RecipeType;
import superscary.mcr.revelations.recipe.ElectricFurnaceRecipe;

public class ElectricFurnaceRecipeType implements RecipeType<ElectricFurnaceRecipe>
{

    private ElectricFurnaceRecipeType ()
    {

    }

    public static final ElectricFurnaceRecipeType INSTANCE = new ElectricFurnaceRecipeType();
    public static final String ID = "smelting";

}