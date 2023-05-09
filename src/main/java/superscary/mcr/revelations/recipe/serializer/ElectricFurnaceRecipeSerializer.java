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
import superscary.mcr.revelations.recipe.ElectricFurnaceRecipe;

public class ElectricFurnaceRecipeSerializer implements RecipeSerializer<ElectricFurnaceRecipe>
{

    public static final ElectricFurnaceRecipeSerializer INSTANCE = new ElectricFurnaceRecipeSerializer();
    public static final ResourceLocation ID = new ResourceLocation(Revelations.MODID, "smelting");

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