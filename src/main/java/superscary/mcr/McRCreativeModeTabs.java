package superscary.mcr;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import superscary.mcr.blocks.McRBlockReg;
import superscary.mcr.items.McRItemReg;

@Mod.EventBusSubscriber(modid = McRMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class McRCreativeModeTabs
{

    public static CreativeModeTab MCR_ITEMS;
    public static CreativeModeTab MCR_BLOCKS;
    public static CreativeModeTab MCR_TOOLS;

    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event)
    {
        MCR_ITEMS = event.registerCreativeModeTab(new ResourceLocation(McRMod.MODID, "mcr_items"),
                builder -> builder.icon(() -> new ItemStack(McRItemReg.RUBBER.get()))
                        .title(Component.translatable("creativemodetab.mcr_items")));
        MCR_BLOCKS = event.registerCreativeModeTab(new ResourceLocation(McRMod.MODID, "mcr_blocks"),
                builder -> builder.icon(() -> new ItemStack(McRBlockReg.BLACK_OPAL_BLOCK.get()))
                        .title(Component.translatable("creativemodetab.mcr_blocks")));
        MCR_TOOLS = event.registerCreativeModeTab(new ResourceLocation(McRMod.MODID, "mcr_tools"),
                builder -> builder.icon(() -> new ItemStack(McRItemReg.SCREWDRIVER.get()))
                        .title(Component.translatable("creativemodetab.mcr_tools")));
    }

}
