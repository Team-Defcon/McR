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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import superscary.mcr.McRMod;

public class ElectricFurnaceRecipe implements Recipe<SimpleContainer>
{

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;

    public ElectricFurnaceRecipe (ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems)
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
    public @NotNull ItemStack assemble (@NotNull SimpleContainer p_44001_, @NotNull RegistryAccess p_267165_)
    {
        return output;
    }

    @Override
    public boolean canCraftInDimensions (int pWidth, int pHeight)
    {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem (@NotNull RegistryAccess access)
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

    public static class Type implements RecipeType<ElectricFurnaceRecipe>
    {

        private Type ()
        {

        }

        public static final Type INSTANCE = new Type();
        public static final String ID = "smelting";

    }

    public static class Serializer implements RecipeSerializer<ElectricFurnaceRecipe>
    {

        public static final ElectricFurnaceRecipe.Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(McRMod.MODID, "smelting");

        @Override
        public @NotNull ElectricFurnaceRecipe fromJson (@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pSerializedRecipe)
        {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "result"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredient");
            NonNullList<Ingredient> inputs = NonNullList.withSize(0, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++)
            {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new ElectricFurnaceRecipe(pRecipeId, output, inputs);
        }

        @Override
        public @Nullable ElectricFurnaceRecipe fromNetwork (@NotNull ResourceLocation pRecipeId, FriendlyByteBuf pBuffer)
        {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++)
            {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }

            ItemStack output = pBuffer.readItem();
            return new ElectricFurnaceRecipe(pRecipeId, output, inputs);

        }

        @Override
        public void toNetwork (FriendlyByteBuf pBuffer, ElectricFurnaceRecipe pRecipe)
        {
            pBuffer.writeInt(pRecipe.getIngredients().size());

            for (Ingredient ing : pRecipe.getIngredients())
            {
                ing.toNetwork(pBuffer);
            }

            pBuffer.writeItemStack(pRecipe.getResultItem(RegistryAccess.EMPTY), false);
        }
    }

}
