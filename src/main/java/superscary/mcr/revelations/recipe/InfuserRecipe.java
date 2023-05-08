package superscary.mcr.revelations.recipe;

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
import superscary.mcr.revelations.Revelations;
import superscary.mcr.revelations.toolkit.FluidJSONUtil;

public class InfuserRecipe implements Recipe<SimpleContainer>
{

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final FluidStack fluidStack;

    public InfuserRecipe (ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems, FluidStack fluidStack)
    {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.fluidStack = fluidStack;
    }

    @Override
    public boolean matches (@NotNull SimpleContainer pContainer, Level pLevel)
    {
        if (pLevel.isClientSide)
        {
            return false;
        }

        return recipeItems.get(0).test(pContainer.getItem(1));
    }

    public FluidStack getFluid ()
    {
        return fluidStack;
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
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType ()
    {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<InfuserRecipe>
    {

        private Type ()
        {

        }

        public static final Type INSTANCE = new Type();
        public static final String ID = "infusing";

    }

    public static class Serializer implements RecipeSerializer<InfuserRecipe>
    {

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Revelations.MODID, "infusing");

        @Override
        public @NotNull InfuserRecipe fromJson (@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pSerializedRecipe)
        {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
            FluidStack fluid = FluidJSONUtil.readFluid(pSerializedRecipe.get("fluid").getAsJsonObject());

            for (int i = 0; i < inputs.size(); i++)
            {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new InfuserRecipe(pRecipeId, output, inputs, fluid);
        }

        @Override
        public @Nullable InfuserRecipe fromNetwork (ResourceLocation pRecipeId, FriendlyByteBuf pBuffer)
        {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);
            FluidStack fluid = pBuffer.readFluidStack();

            for (int i = 0; i < inputs.size(); i++)
            {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }

            ItemStack output = pBuffer.readItem();
            return new InfuserRecipe(pRecipeId, output, inputs, fluid);

        }

        @Override
        public void toNetwork (FriendlyByteBuf pBuffer, InfuserRecipe pRecipe)
        {
            pBuffer.writeInt(pRecipe.getIngredients().size());
            pBuffer.writeFluidStack(pRecipe.fluidStack);

            for (Ingredient ing : pRecipe.getIngredients())
            {
                ing.toNetwork(pBuffer);
            }

            pBuffer.writeItemStack(pRecipe.getResultItem(RegistryAccess.EMPTY), false);
        }
    }

}
