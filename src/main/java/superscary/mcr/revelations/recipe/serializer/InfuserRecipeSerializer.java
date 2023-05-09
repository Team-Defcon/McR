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
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import superscary.mcr.revelations.Revelations;
import superscary.mcr.revelations.recipe.InfuserRecipe;
import superscary.mcr.revelations.toolkit.FluidJSONUtil;

public class InfuserRecipeSerializer implements RecipeSerializer<InfuserRecipe>
{

    public static final InfuserRecipeSerializer INSTANCE = new InfuserRecipeSerializer();
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