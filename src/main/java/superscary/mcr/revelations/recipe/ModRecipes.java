package superscary.mcr.revelations.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import superscary.mcr.revelations.Revelations;

public class ModRecipes
{

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Revelations.MODID);

    public static final RegistryObject<RecipeSerializer<InfuserRecipe>> INFUSING_SERIALIZER = SERIALIZERS.register("infusing", () -> InfuserRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<ElectricFurnaceRecipe>> ELECTRIC_FURNACE_SERIALIZER = SERIALIZERS.register("smelting", () -> ElectricFurnaceRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<CompressorRecipe>> COMPRESSOR_RECIPE = SERIALIZERS.register("compress", () -> CompressorRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<ExtruderRecipe>> EXTRUDER_RECIPE = SERIALIZERS.register("extrude", () -> ExtruderRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<ChemicalMixerRecipe>> CHEMICAL_MIXER_RECIPE = SERIALIZERS.register("chemical_mixer", () -> ChemicalMixerRecipe.Serializer.INSTANCE);

    public static void register (IEventBus eventBus)
    {
        SERIALIZERS.register(eventBus);
    }

}
