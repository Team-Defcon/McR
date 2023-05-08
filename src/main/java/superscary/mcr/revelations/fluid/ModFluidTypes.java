package superscary.mcr.revelations.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;
import superscary.mcr.revelations.Revelations;

public class ModFluidTypes
{

    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("block/water_flow");
    public static final ResourceLocation OIL_OVERLAY = new ResourceLocation(Revelations.MODID, "misc/in_soap_water");

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Revelations.MODID);

    public static final RegistryObject<FluidType> OIL_FLUID_TYPE = registerOil("oil_fluid",
            FluidType.Properties.create().lightLevel(0).density(15).viscosity(5));

    public static final RegistryObject<FluidType> PETROLEUM_FLUID_TYPE = registerPetrol("petroleum_fluid",
            FluidType.Properties.create().lightLevel(1).density(15).viscosity(5));

    public static final RegistryObject<FluidType> CRUDE_OIL_FLUID_TYPE = registerCrudeOil("crude_oil_fluid",
            FluidType.Properties.create().lightLevel(1).density(15).viscosity(5));

    public static final RegistryObject<FluidType> SALINE_FLUID_TYPE = registerSaline("saline_fluid",
            FluidType.Properties.create().lightLevel(1).density(3).viscosity(10));

    private static RegistryObject<FluidType> registerOil (String name, FluidType.Properties properties)
    {
        return FLUID_TYPES.register(name, () -> new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, OIL_OVERLAY,
                0xA01C1E1B, new Vector3f(28f / 255f, 30f / 255f, 27f / 255f), properties));
    }

    private static RegistryObject<FluidType> registerPetrol (String name, FluidType.Properties properties)
    {
        return FLUID_TYPES.register(name, () -> new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, OIL_OVERLAY,
                0xA1E6AC27, new Vector3f(230f / 255f, 172f / 255f, 39f / 255f), properties));
    }

    private static RegistryObject<FluidType> registerCrudeOil (String name, FluidType.Properties properties)
    {
        return FLUID_TYPES.register(name, () -> new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, OIL_OVERLAY,
                0xA01C1E1B, new Vector3f(28f / 255f, 30f / 255f, 27f / 255f), properties));
    }

    private static RegistryObject<FluidType> registerSaline (String name, FluidType.Properties properties)
    {
        return FLUID_TYPES.register(name, () -> new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, OIL_OVERLAY,
                0xA1C5E8E7, new Vector3f(197f / 255f, 232f / 255f, 231f / 255f), properties));
    }

    public static void registerOil (IEventBus eventBus)
    {
        FLUID_TYPES.register(eventBus);
    }

}
