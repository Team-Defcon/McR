package superscary.mcr.revelations;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import superscary.mcr.revelations.blocks.McRBlockReg;
import superscary.mcr.revelations.blocks.entity.ModBlockEntities;
import superscary.mcr.revelations.blocks.entity.renderer.InfuserBlockEntityRenderer;
import superscary.mcr.revelations.config.McRCommonConfig;
import superscary.mcr.revelations.config.McrClientConfig;
import superscary.mcr.revelations.fluid.ModFluidTypes;
import superscary.mcr.revelations.fluid.ModFluids;
import superscary.mcr.revelations.gui.ModMenuTypes;
import superscary.mcr.revelations.gui.screen.*;
import superscary.mcr.revelations.items.McRItemReg;
import superscary.mcr.revelations.network.ModMessages;
import superscary.mcr.revelations.recipe.ModRecipes;
import superscary.mcr.revelations.recipe.type.ModRecipeTypes;

@Mod(Revelations.MODID)
public class Revelations
{
    public static final String MODID = "mcr";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public Revelations ()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        LOGGER.debug("Loading Configs...");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, McRCommonConfig.CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, McrClientConfig.CONFIG);
        LOGGER.debug("Done.");

        McRItemReg.register(modEventBus);
        McRBlockReg.register(modEventBus);
        ModFluids.register(modEventBus);
        ModFluidTypes.registerOil(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        ModRecipes.register(modEventBus);
        ModRecipeTypes.register(modEventBus);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);
    }

    private void addCreative (CreativeModeTabEvent.BuildContents event)
    {
        if (event.getTab() == McRCreativeModeTabs.MCR_ITEMS)
        {
            event.accept(McRItemReg.TOME);
            event.accept(McRItemReg.BLACK_OPAL);
            event.accept(McRItemReg.RAW_BLACK_OPAL);
            event.accept(McRItemReg.RUBBER);
            event.accept(McRItemReg.STICKY_RESIN);
            event.accept(McRItemReg.SAP);
            event.accept(McRItemReg.RAW_RUBBER);
            event.accept(McRItemReg.STEEL_INGOT);
            event.accept(McRItemReg.RAW_STEEL);
            event.accept(McRItemReg.STEEL_ROD);
            event.accept(McRItemReg.URANIUM_INGOT);
            event.accept(McRItemReg.RAW_URANIUM);
            event.accept(McRItemReg.LEAD_INGOT);
            event.accept(McRItemReg.RAW_LEAD);
            event.accept(McRItemReg.PLASTIC);
            event.accept(McRItemReg.SLAG);
            event.accept(McRItemReg.TIN_INGOT);
            event.accept(McRItemReg.WOODEN_GEAR);
            event.accept(McRItemReg.STONE_GEAR);
            event.accept(McRItemReg.IRON_GEAR);
            event.accept(McRItemReg.TIN_GEAR);
            event.accept(McRItemReg.STEEL_GEAR);
            event.accept(McRItemReg.OIL_BUCKET);
            event.accept(McRItemReg.PETROLEUM_BUCKET);
            event.accept(McRItemReg.CRUDE_OIL_BUCKET);
            event.accept(McRItemReg.SALINE_BUCKET);
            event.accept(McRItemReg.DIMENSIONAL_SHARD);
            event.accept(McRItemReg.SALT);

            event.accept(McRBlockReg.BLACK_OPAL_BLOCK);
            event.accept(McRBlockReg.BLACK_OPAL_ORE);
            event.accept(McRBlockReg.DEEPSLATE_BLACK_OPAL_ORE);
            event.accept(McRBlockReg.ENDSTONE_BLACK_OPAL_ORE);
            event.accept(McRBlockReg.NETHERRACK_BLACK_OPAL_ORE);
            event.accept(McRBlockReg.MACHINE_BASE);
            event.accept(McRBlockReg.RUBBER_LEAVES);
            event.accept(McRBlockReg.RUBBER_LOG);
            event.accept(McRBlockReg.RUBBER_PLANKS);
            event.accept(McRBlockReg.RUBBER_SAPLING);
            event.accept(McRBlockReg.RUBBER_WOOD);
            event.accept(McRBlockReg.STRIPPED_RUBBER_LOG);
            event.accept(McRBlockReg.STRIPPED_RUBBER_WOOD);
            event.accept(McRBlockReg.URANIUM_ORE);
            event.accept(McRBlockReg.LEAD_ORE);
            event.accept(McRBlockReg.SALT_BLOCK);

            event.accept(McRBlockReg.BARREL);
            event.accept(McRBlockReg.EMPTY_MACHINE_FRAME);
            event.accept(McRBlockReg.FORGE);
            event.accept(McRBlockReg.INFUSER);
            event.accept(McRBlockReg.ELECTRIC_FURNACE);
            event.accept(McRBlockReg.COAL_GENERATOR);
            event.accept(McRBlockReg.COMPRESSOR);
            event.accept(McRBlockReg.EXTRUDER);
            event.accept(McRBlockReg.CHEMICAL_MIXER);

            event.accept(McRItemReg.SCREWDRIVER);
            event.accept(McRItemReg.HAMMER);
        }

    }

    private void commonSetup (final FMLCommonSetupEvent event)
    {
        event.enqueueWork(ModMessages::register);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup (FMLClientSetupEvent event)
        {
            ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_OIL.get(), RenderType.solid());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_OIL.get(), RenderType.solid());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_CRUDE_OIL.get(), RenderType.solid());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_CRUDE_OIL.get(), RenderType.solid());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_PETROLEUM.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_PETROLEUM.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SALINE.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SALINE.get(), RenderType.translucent());

            MenuScreens.register(ModMenuTypes.INFUSER_MENU.get(), InfuserScreen::new);
            MenuScreens.register(ModMenuTypes.ELECTRIC_FURNACE_MENU.get(), ElectricFurnaceScreen::new);
            MenuScreens.register(ModMenuTypes.COAL_GENERATOR_MENU.get(), CoalGeneratorScreen::new);
            MenuScreens.register(ModMenuTypes.COMPRESSOR_MENU.get(), CompressorScreen::new);
            MenuScreens.register(ModMenuTypes.EXTRUDER_MENU.get(), ExtruderScreen::new);
            MenuScreens.register(ModMenuTypes.CHEMICAL_MIXER_MENU.get(), ChemicalMixerScreen::new);
            MenuScreens.register(ModMenuTypes.FORGE_MENU.get(), ForgeScreen::new);
        }

        @SubscribeEvent
        public static void registerRenderers (final EntityRenderersEvent.RegisterRenderers event)
        {
            event.registerBlockEntityRenderer(ModBlockEntities.INFUSER.get(), InfuserBlockEntityRenderer::new);
        }
    }

}
