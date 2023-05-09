package superscary.mcr.revelations.recipe.type;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import superscary.mcr.revelations.Revelations;
import superscary.mcr.revelations.recipe.*;

public class ModRecipeTypes
{

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Revelations.MODID);

    public static final RegistryObject<RecipeType<ForgeRecipe>> FORGE = RECIPE_TYPES.register("forge", () -> ForgeRecipeType.INSTANCE);
    public static final RegistryObject<RecipeType<ChemicalMixerRecipe>> CHEMICAL_MIXER = RECIPE_TYPES.register("chemical_mixer", () -> ChemicalMixerRecipeType.INSTANCE);
    public static final RegistryObject<RecipeType<CompressorRecipe>> COMPRESSOR = RECIPE_TYPES.register("compress", () -> CompressorRecipeType.INSTANCE);
    public static final RegistryObject<RecipeType<ElectricFurnaceRecipe>> ELECTRIC_FURNACE = RECIPE_TYPES.register("smelting", () -> ElectricFurnaceRecipeType.INSTANCE);
    public static final RegistryObject<RecipeType<ExtruderRecipe>> EXTRUDER = RECIPE_TYPES.register("extrude", () -> ExtruderRecipeType.INSTANCE);
    public static final RegistryObject<RecipeType<InfuserRecipe>> INFUSER = RECIPE_TYPES.register("infusing", () -> InfuserRecipeType.INSTANCE);

    public static void register (IEventBus eventBus)
    {
        RECIPE_TYPES.register(eventBus);
    }

}
