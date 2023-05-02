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

    public static void register (IEventBus eventBus)
    {
        BLOCK_ENTITIES.register(eventBus);
    }

}
