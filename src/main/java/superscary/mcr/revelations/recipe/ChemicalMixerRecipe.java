package superscary.mcr.revelations.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import superscary.mcr.revelations.recipe.type.ModRecipeTypes;

public class ChemicalMixerRecipe implements Recipe<SimpleContainer>
{

    private final ResourceLocation id;
    private final NonNullList<Ingredient> recipeItems;
    public final FluidStack inputFluidStack;
    public final FluidStack outputFluidStack;

    public ChemicalMixerRecipe (ResourceLocation id, NonNullList<Ingredient> recipeItems, FluidStack inputFluidStack, FluidStack outputFluidStack)
    {
        this.id = id;
        this.recipeItems = recipeItems;
        this.inputFluidStack = inputFluidStack;
        this.outputFluidStack = new FluidStack(outputFluidStack, inputFluidStack.getAmount());
    }

    @Override
    public boolean matches (@NotNull SimpleContainer pContainer, Level pLevel)
    {
        if (pLevel.isClientSide)
        {
            return false;
        }

        return inputFluidStack.isFluidEqual(outputFluidStack);
    }

    @Override
    public boolean isSpecial ()
    {
        return true;
    }

    public FluidStack getFluid ()
    {
        return inputFluidStack;
    }

    public FluidStack getOutputFluid ()
    {
        return outputFluidStack;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients ()
    {
        return recipeItems;
    }

    @Override
    public ItemStack assemble (@NotNull SimpleContainer container, @NotNull RegistryAccess access)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions (int pWidth, int pHeight)
    {
        return true;
    }

    @Override
    public ItemStack getResultItem (RegistryAccess access)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ResourceLocation getId ()
    {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer ()
    {
        return ModRecipes.CHEMICAL_MIXER_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType ()
    {
        return ModRecipeTypes.CHEMICAL_MIXER.get();
    }

}
