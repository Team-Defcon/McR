package superscary.mcr;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import superscary.mcr.blocks.McRBlockReg;
import superscary.mcr.blocks.entity.ModBlockEntities;
import superscary.mcr.fluid.ModFluidTypes;
import superscary.mcr.fluid.ModFluids;
import superscary.mcr.items.McRItemReg;
import superscary.mcr.network.ModMessages;
import superscary.mcr.recipe.ModRecipes;
import superscary.mcr.screen.InfuserScreen;
import superscary.mcr.screen.ModMenuTypes;

@Mod(McRMod.MODID)
public class McRMod
{
    public static final String MODID = "mcr";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public McRMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        McRItemReg.register(modEventBus);
        McRBlockReg.register(modEventBus);
        ModFluids.register(modEventBus);
        ModFluidTypes.registerOil(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);
    }

    private void addCreative(CreativeModeTabEvent.BuildContents event)
    {
        if (event.getTab() == McRCreativeModeTabs.MCR_ITEMS)
        {
            event.accept(McRItemReg.BLACK_OPAL);
            event.accept(McRItemReg.RAW_BLACK_OPAL);
            event.accept(McRItemReg.RUBBER);
            event.accept(McRItemReg.STICKY_RESIN);
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
            event.accept(McRItemReg.OIL_BUCKET);
            event.accept(McRItemReg.PETROLEUM_BUCKET);
            event.accept(McRItemReg.CRUDE_OIL_BUCKET);
        }

        if (event.getTab() == McRCreativeModeTabs.MCR_BLOCKS)
        {
            event.accept(McRBlockReg.BLACK_OPAL_BLOCK);
            event.accept(McRBlockReg.BLACK_OPAL_ORE);
            event.accept(McRBlockReg.DEEPSLATE_BLACK_OPAL_ORE);
            event.accept(McRBlockReg.ENDSTONE_BLACK_OPAL_ORE);
            event.accept(McRBlockReg.NETHERRACK_BLACK_OPAL_ORE);
            event.accept(McRBlockReg.MACHINE_BASE);
            event.accept(McRBlockReg.EBONY_LEAVES);
            event.accept(McRBlockReg.EBONY_LOG);
            event.accept(McRBlockReg.EBONY_PLANKS);
            event.accept(McRBlockReg.EBONY_SAPLING);
            event.accept(McRBlockReg.EBONY_WOOD);
            event.accept(McRBlockReg.STRIPPED_EBONY_LOG);
            event.accept(McRBlockReg.STRIPPED_EBONY_WOOD);
            event.accept(McRBlockReg.URANIUM_ORE);
            event.accept(McRBlockReg.LEAD_ORE);
            event.accept(McRBlockReg.INFUSER);
        }

        if (event.getTab() == McRCreativeModeTabs.MCR_TOOLS)
        {
            event.accept(McRItemReg.SCREWDRIVER);
            event.accept(McRItemReg.HAMMER);
        }

    }

    private void commonSetup (final FMLCommonSetupEvent event)
    {

        event.enqueueWork(() -> {
            ModMessages.register();
        });

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_OIL.get(), RenderType.solid());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_OIL.get(), RenderType.solid());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_CRUDE_OIL.get(), RenderType.solid());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_CRUDE_OIL.get(), RenderType.solid());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_PETROLEUM.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_PETROLEUM.get(), RenderType.translucent());

            MenuScreens.register(ModMenuTypes.INFUSER_MENU.get(), InfuserScreen::new);

        }
    }
}
