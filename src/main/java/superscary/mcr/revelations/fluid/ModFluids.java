package superscary.mcr.revelations.fluid;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import superscary.mcr.revelations.Revelations;
import superscary.mcr.revelations.blocks.McRBlockReg;
import superscary.mcr.revelations.items.McRItemReg;

public class ModFluids
{
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Revelations.MODID);

    public static final RegistryObject<FlowingFluid> SOURCE_OIL = FLUIDS.register("oil_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.OIL_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_OIL = FLUIDS.register("flowing_oil",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.OIL_FLUID_PROPERTIES));

    public static final RegistryObject<FlowingFluid> SOURCE_PETROLEUM = FLUIDS.register("petroleum_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.PETROLEUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_PETROLEUM = FLUIDS.register("flowing_petroleum",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PETROLEUM_FLUID_PROPERTIES));

    public static final RegistryObject<FlowingFluid> SOURCE_CRUDE_OIL = FLUIDS.register("crude_oil_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.CRUDE_OIL_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_CRUDE_OIL = FLUIDS.register("flowing_crude_oil",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.CRUDE_OIL_PROPERTIES));

    public static final RegistryObject<FlowingFluid> SOURCE_SALINE = FLUIDS.register("saline_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.SALINE_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_SALINE = FLUIDS.register("flowing_saline",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.SALINE_PROPERTIES));

    /**
     * FLUID PROPERTIES
     */
    public static final ForgeFlowingFluid.Properties OIL_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.OIL_FLUID_TYPE, SOURCE_OIL, FLOWING_OIL).slopeFindDistance(2).levelDecreasePerBlock(2).explosionResistance(0).block(McRBlockReg.OIL_FLUID_BLOCK).bucket(McRItemReg.OIL_BUCKET);

    public static final ForgeFlowingFluid.Properties PETROLEUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.PETROLEUM_FLUID_TYPE, SOURCE_PETROLEUM, FLOWING_PETROLEUM).slopeFindDistance(5).explosionResistance(0).levelDecreasePerBlock(1).block(McRBlockReg.PETROLEUM_FLUID_BLOCK).bucket(McRItemReg.PETROLEUM_BUCKET);

    public static final ForgeFlowingFluid.Properties CRUDE_OIL_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.CRUDE_OIL_FLUID_TYPE, SOURCE_CRUDE_OIL, FLOWING_CRUDE_OIL).slopeFindDistance(2).explosionResistance(0).levelDecreasePerBlock(2).block(McRBlockReg.CRUDE_OIL_FLUID_BLOCK).bucket(McRItemReg.CRUDE_OIL_BUCKET);

    public static final ForgeFlowingFluid.Properties SALINE_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.SALINE_FLUID_TYPE, SOURCE_SALINE, FLOWING_SALINE).slopeFindDistance(2).explosionResistance(0).levelDecreasePerBlock(2).block(McRBlockReg.SALINE_FLUID_BLOCK).bucket(McRItemReg.SALINE_BUCKET);

    public static void register (IEventBus eventBus)
    {
        FLUIDS.register(eventBus);
    }

}
