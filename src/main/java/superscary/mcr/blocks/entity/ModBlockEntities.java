package superscary.mcr.blocks.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import superscary.mcr.McRMod;
import superscary.mcr.blocks.McRBlockReg;

public class ModBlockEntities
{

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, McRMod.MODID);

    public static final RegistryObject<BlockEntityType<InfuserBlockEntity>> INFUSER = BLOCK_ENTITIES.register("infuser", () -> BlockEntityType.Builder.of(InfuserBlockEntity::new, McRBlockReg.INFUSER.get()).build(null));
    public static final RegistryObject<BlockEntityType<ElectricFurnaceBlockEntity>> ELECTRIC_FURNACE = BLOCK_ENTITIES.register("electric_furnace", () -> BlockEntityType.Builder.of(ElectricFurnaceBlockEntity::new, McRBlockReg.ELECTRIC_FURNACE.get()).build(null));
    public static final RegistryObject<BlockEntityType<CoalGeneratorEntity>> COAL_GENERATOR = BLOCK_ENTITIES.register("coal_generator", () -> BlockEntityType.Builder.of(CoalGeneratorEntity::new, McRBlockReg.COAL_GENERATOR.get()).build(null));
    public static final RegistryObject<BlockEntityType<CompressorEntity>> COMPRESSOR = BLOCK_ENTITIES.register("compressor", () -> BlockEntityType.Builder.of(CompressorEntity::new, McRBlockReg.COMPRESSOR.get()).build(null));
    public static final RegistryObject<BlockEntityType<ExtruderEntity>> EXTRUDER = BLOCK_ENTITIES.register("extruder", () -> BlockEntityType.Builder.of(ExtruderEntity::new, McRBlockReg.EXTRUDER.get()).build(null));
    public static final RegistryObject<BlockEntityType<EmptyMachineFrameEntity>> EMPTY_MACHINE_FRAME = BLOCK_ENTITIES.register("empty_machine_frame", () -> BlockEntityType.Builder.of(EmptyMachineFrameEntity::new, McRBlockReg.EMPTY_MACHINE_FRAME.get()).build(null));
    public static final RegistryObject<BlockEntityType<ChemicalMixerEntity>> CHEMICAL_MIXER = BLOCK_ENTITIES.register("chemical_mixer", () -> BlockEntityType.Builder.of(ChemicalMixerEntity::new, McRBlockReg.CHEMICAL_MIXER.get()).build(null));
    public static final RegistryObject<BlockEntityType<BarrelEntity>> BARREL = BLOCK_ENTITIES.register("barrel", () -> BlockEntityType.Builder.of(BarrelEntity::new, McRBlockReg.BARREL.get()).build(null));

    public static void register (IEventBus eventBus)
    {
        BLOCK_ENTITIES.register(eventBus);
    }

}
