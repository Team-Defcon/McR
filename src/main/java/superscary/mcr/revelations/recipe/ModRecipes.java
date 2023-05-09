package superscary.mcr.revelations.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import superscary.mcr.revelations.Revelations;
import superscary.mcr.revelations.recipe.serializer.*;

public class ModRecipes
{

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Revelations.MODID);

    public static final RegistryObject<RecipeSerializer<InfuserRecipe>> INFUSING_SERIALIZER = SERIALIZERS.register("infusing", InfuserRecipeSerializer::new);
    public static final RegistryObject<RecipeSerializer<ElectricFurnaceRecipe>> ELECTRIC_FURNACE_SERIALIZER = SERIALIZERS.register("smelting", ElectricFurnaceRecipeSerializer::new);
    public static final RegistryObject<RecipeSerializer<CompressorRecipe>> COMPRESSOR_SERIALIZER = SERIALIZERS.register("compress", CompressorRecipeSerializer::new);
    public static final RegistryObject<RecipeSerializer<ExtruderRecipe>> EXTRUDER_SERIALIZER = SERIALIZERS.register("extrude", ExtruderRecipeSerializer::new);
    public static final RegistryObject<RecipeSerializer<ChemicalMixerRecipe>> CHEMICAL_MIXER_SERIALIZER = SERIALIZERS.register("chemical_mixer", ChemicalMixerRecipeSerializer::new);
    public static final RegistryObject<RecipeSerializer<ForgeRecipe>> FORGE_SERIALIZER = SERIALIZERS.register("forge", ForgeRecipeSerializer::new);

    public static void register (IEventBus eventBus)
    {
        SERIALIZERS.register(eventBus);
    }

}
