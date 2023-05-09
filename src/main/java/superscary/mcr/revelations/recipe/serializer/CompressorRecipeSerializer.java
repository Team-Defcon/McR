package superscary.mcr.revelations.recipe.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import superscary.mcr.revelations.Revelations;
import superscary.mcr.revelations.recipe.CompressorRecipe;

public class CompressorRecipeSerializer implements RecipeSerializer<CompressorRecipe>
{

    public static final CompressorRecipeSerializer INSTANCE = new CompressorRecipeSerializer();
    public static final ResourceLocation ID = new ResourceLocation(Revelations.MODID, "compress");

    @Override
    public @NotNull CompressorRecipe fromJson (@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pSerializedRecipe)
    {
        ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

        JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
        NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

        for (int i = 0; i < inputs.size(); i++)
        {
            inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
        }

        return new CompressorRecipe(pRecipeId, output, inputs);
    }

    @Override
    public @Nullable CompressorRecipe fromNetwork (ResourceLocation pRecipeId, FriendlyByteBuf pBuffer)
    {
        NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

        for (int i = 0; i < inputs.size(); i++)
        {
            inputs.set(i, Ingredient.fromNetwork(pBuffer));
        }

        ItemStack output = pBuffer.readItem();
        return new CompressorRecipe(pRecipeId, output, inputs);

    }

    @Override
    public void toNetwork (FriendlyByteBuf pBuffer, CompressorRecipe pRecipe)
    {
        pBuffer.writeInt(pRecipe.getIngredients().size());

        for (Ingredient ing : pRecipe.getIngredients())
        {
            ing.toNetwork(pBuffer);
        }

        pBuffer.writeItemStack(pRecipe.getResultItem(RegistryAccess.EMPTY), false);
    }
}