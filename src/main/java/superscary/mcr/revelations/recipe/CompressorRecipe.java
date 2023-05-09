package superscary.mcr.revelations.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import superscary.mcr.revelations.recipe.type.ModRecipeTypes;

public class CompressorRecipe implements Recipe<SimpleContainer>
{

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;

    public CompressorRecipe (ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems)
    {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches (@NotNull SimpleContainer pContainer, Level pLevel)
    {
        if (pLevel.isClientSide)
        {
            return false;
        }

        return recipeItems.get(0).test(pContainer.getItem(0));
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients ()
    {
        return recipeItems;
    }

    @Override
    public @NotNull ItemStack assemble (@NotNull SimpleContainer container, @NotNull RegistryAccess access)
    {
        return output;
    }

    @Override
    public boolean canCraftInDimensions (int pWidth, int pHeight)
    {
        return true;
    }

    @Override
    public ItemStack getResultItem (RegistryAccess access)
    {
        return output.copy();
    }

    @Override
    public @NotNull ResourceLocation getId ()
    {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer ()
    {
        return ModRecipes.COMPRESSOR_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType ()
    {
        return ModRecipeTypes.COMPRESSOR.get();
    }

}
