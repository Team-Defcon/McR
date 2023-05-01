package superscary.mcr.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import superscary.mcr.McRMod;

public class ModRecipes
{

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, McRMod.MODID);

    public static final RegistryObject<RecipeSerializer<InfuserRecipe>> INFUSING_SERIALIZER = SERIALIZERS.register("infusing", () -> InfuserRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<ElectricFurnaceRecipe>> ELECTRIC_FURNACE_SERIALIZER = SERIALIZERS.register("smelting", () -> ElectricFurnaceRecipe.Serializer.INSTANCE);


    public static void register (IEventBus eventBus)
    {
        SERIALIZERS.register(eventBus);
    }

}
