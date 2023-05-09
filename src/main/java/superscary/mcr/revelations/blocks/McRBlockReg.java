package superscary.mcr.revelations.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import superscary.mcr.revelations.Revelations;
import superscary.mcr.revelations.blocks.machine.*;
import superscary.mcr.revelations.blocks.trees.ModFlammableRotatedPillarBlock;
import superscary.mcr.revelations.fluid.ModFluids;
import superscary.mcr.revelations.worldgen.tree.EbonyTreeGrower;

import java.util.function.Supplier;

public class McRBlockReg
{

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Revelations.MODID);

    public static final RegistryObject<Block> FORGE = registerBlock("forge",
            () -> new ForgeBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(3f).requiresCorrectToolForDrops()));
    public static Block REFORGER;
    public static Block CALIBRATION_TABLE;
    public static Block RECALIBRATION_TABLE;
    public static Block ANALYZER;

    public static final RegistryObject<Block> BLACK_OPAL_BLOCK = registerBlock("black_opal_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(6F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> BLACK_OPAL_ORE = registerBlock("black_opal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(6F).requiresCorrectToolForDrops(), UniformInt.of(2, 6)));
    public static final RegistryObject<Block> DEEPSLATE_BLACK_OPAL_ORE = registerBlock("deepslate_black_opal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(8F).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops(), UniformInt.of(2, 6)));
    public static final RegistryObject<Block> NETHERRACK_BLACK_OPAL_ORE = registerBlock("netherrack_black_opal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(8F).sound(SoundType.NETHER_ORE).requiresCorrectToolForDrops(), UniformInt.of(2, 6)));
    public static final RegistryObject<Block> ENDSTONE_BLACK_OPAL_ORE = registerBlock("endstone_black_opal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(10F).requiresCorrectToolForDrops(), UniformInt.of(2, 6)));

    public static final RegistryObject<Block> URANIUM_ORE = registerBlock("uranium_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(10F).requiresCorrectToolForDrops(), UniformInt.of(1, 1)));
    public static final RegistryObject<Block> LEAD_ORE = registerBlock("lead_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(10F).requiresCorrectToolForDrops(), UniformInt.of(1, 1)));

    /***
     * <p>
     *
     * MACHINE BLOCKS
     * <p>
     *
     */
    public static final RegistryObject<Block> MACHINE_BASE = registerBlock("machine_base",
            () -> new MachineBase(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(10F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> INFUSER = registerBlock("infuser",
            () -> new InfuserBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(10F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> ELECTRIC_FURNACE = registerBlock("electric_furnace",
            () -> new ElectricFurnaceBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(10f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> COAL_GENERATOR = registerBlock("coal_generator",
            () -> new CoalGeneratorBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(10f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> COMPRESSOR = registerBlock("compressor",
            () -> new CompressorBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(10f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> EXTRUDER = registerBlock("extruder",
            () -> new ExtruderBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(10f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> CHEMICAL_MIXER = registerBlock("chemical_mixer",
            () -> new ChemicalMixerBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(10f).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> EMPTY_MACHINE_FRAME = registerBlock("empty_machine_frame",
            () -> new EmptyMachineFrameBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)
                    .strength(8f).sound(SoundType.METAL).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> BARREL = registerBlock("barrel",
            () -> new BarrelBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(5f)));


    /***
     *
     *
     * GENERATABLE BLOCKS
     *
     *
     */
    public static final RegistryObject<Block> RUBBER_LOG = registerBlock("rubber_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)
                    .strength(5F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> RUBBER_WOOD = registerBlock("rubber_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)
                    .strength(5F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> STRIPPED_RUBBER_LOG = registerBlock("stripped_rubber_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)
                    .strength(5F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> STRIPPED_RUBBER_WOOD = registerBlock("stripped_rubber_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)
                    .strength(5F)));
    public static final RegistryObject<Block> RUBBER_PLANKS = registerBlock("rubber_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)
                    .strength(5F)) {
                @Override
                public boolean isFlammable (BlockState state, BlockGetter level, BlockPos pos, Direction direction)
                {
                    return true;
                }

                @Override
                public int getFlammability (BlockState state, BlockGetter level, BlockPos pos, Direction direction)
                {
                    return 5;
                }

                @Override
                public int getFireSpreadSpeed (BlockState state, BlockGetter level, BlockPos pos, Direction direction)
                {
                    return 20;
                }
            });
    public static final RegistryObject<Block> RUBBER_LEAVES = registerBlock("rubber_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)
                    .strength(5F)) {

                @Override
                public boolean isFlammable (BlockState state, BlockGetter level, BlockPos pos, Direction direction)
                {
                    return true;
                }

                @Override
                public int getFlammability (BlockState state, BlockGetter level, BlockPos pos, Direction direction)
                {
                    return 30;
                }

                @Override
                public int getFireSpreadSpeed (BlockState state, BlockGetter level, BlockPos pos, Direction direction)
                {
                    return 60;
                }
            });
    public static final RegistryObject<Block> RUBBER_SAPLING = registerBlock("rubber_sapling",
            () -> new SaplingBlock(new EbonyTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> SALT_BLOCK = registerBlock("salt_block",
            () -> new SandBlock(0xF3FAFD, BlockBehaviour.Properties.copy(Blocks.SAND).strength(3f)));


    /***
     *
     *
     * FLUIDS
     *
     */
    public static final RegistryObject<LiquidBlock> OIL_FLUID_BLOCK = BLOCKS.register("oil_fluid_block",
            () -> new LiquidBlock(ModFluids.SOURCE_OIL, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<LiquidBlock> PETROLEUM_FLUID_BLOCK = BLOCKS.register("petroleum_fluid_block",
            () -> new LiquidBlock(ModFluids.SOURCE_PETROLEUM, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<LiquidBlock> CRUDE_OIL_FLUID_BLOCK = BLOCKS.register("crude_oil_fluid_block",
            () -> new LiquidBlock(ModFluids.SOURCE_CRUDE_OIL, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<LiquidBlock> SALINE_FLUID_BLOCK = BLOCKS.register("saline_fluid_block",
            () -> new LiquidBlock(ModFluids.SOURCE_SALINE, BlockBehaviour.Properties.copy(Blocks.WATER)));


    private static <T extends Block> RegistryObject<T> registerBlock (String name, Supplier<T> block)
    {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem (String name, RegistryObject<T> block)
    {
        return Revelations.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }

}
