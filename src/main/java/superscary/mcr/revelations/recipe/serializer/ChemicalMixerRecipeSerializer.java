package superscary.mcr.revelations.recipe.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import superscary.mcr.revelations.Revelations;
import superscary.mcr.revelations.recipe.ChemicalMixerRecipe;
import superscary.mcr.revelations.toolkit.FluidJSONUtil;

public class ChemicalMixerRecipeSerializer implements RecipeSerializer<ChemicalMixerRecipe>
{

    public static final ChemicalMixerRecipeSerializer INSTANCE = new ChemicalMixerRecipeSerializer();
    public static final ResourceLocation ID = new ResourceLocation(Revelations.MODID, "chemical_mixer");

    @Override
    public @NotNull ChemicalMixerRecipe fromJson (@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pSerializedRecipe)
    {
        FluidStack output = FluidJSONUtil.readFluid(pSerializedRecipe.get("output").getAsJsonObject());

        JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "catalyst");
        NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
        FluidStack fluid = FluidJSONUtil.readFluid(pSerializedRecipe.get("input").getAsJsonObject());

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