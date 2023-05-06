package superscary.mcr.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import superscary.mcr.McRMod;
import superscary.mcr.toolkit.FluidJSONUtil;

public class ChemicalMixerRecipe implements Recipe<SimpleContainer>
{

    private final ResourceLocation id;
    private final NonNullList<Ingredient> recipeItems;
    private final FluidStack inputFluidStack;
    private final FluidStack outputFluidStack;

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
        return null;
    }

    @Override
    public boolean canCraftInDimensions (int pWidth, int pHeight)
    {
        return true;
    }

    @Override
    public ItemStack getResultItem (RegistryAccess access)
    {
        return null;
    }

    @Override
    public @NotNull ResourceLocation getId ()
    {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer ()
    {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType ()
    {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<ChemicalMixerRecipe>
    {

        private Type ()
        {

        }

        public static final Type INSTANCE = new Type();
        public static final String ID = "chemical_washer";

    }

    public static class Serializer implements RecipeSerializer<ChemicalMixerRecipe>
    {

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(McRMod.MODID, "chemical_washer");

        @Override
        public @NotNull ChemicalMixerRecipe fromJson (@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pSerializedRecipe)
        {
            FluidStack output = FluidJSONUtil.readFluid(pSerializedRecipe.get("output").getAsJsonObject());


            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
            FluidStack fluid = FluidJSONUtil.readFluid(pSerializedRecipe.get("fluid").getAsJsonObject());

            for (int i = 0; i < inputs.size(); i++)
            {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new ChemicalMixerRecipe(pRecipeId, inputs, fluid, output);
        }

        @Override
        public @Nullable ChemicalMixerRecipe fromNetwork (ResourceLocation pRecipeId, FriendlyByteBuf pBuffer)
        {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);
            FluidStack fluid = pBuffer.readFluidStack();

            for (int i = 0; i < inputs.size(); i++)
            {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }

            FluidStack output = pBuffer.readFluidStack();
            return new ChemicalMixerRecipe(pRecipeId, inputs, fluid, output);

        }

        @Override
        public void toNetwork (FriendlyByteBuf pBuffer, ChemicalMixerRecipe pRecipe)
        {
            pBuffer.writeInt(pRecipe.getIngredients().size());
            pBuffer.writeFluidStack(pRecipe.inputFluidStack);

            for (Ingredient ing : pRecipe.getIngredients())
            {
                ing.toNetwork(pBuffer);
            }

            pBuffer.writeFluidStack(pRecipe.outputFluidStack);
        }
    }

}
