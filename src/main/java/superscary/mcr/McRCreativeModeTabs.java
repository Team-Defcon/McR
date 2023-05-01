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

    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event)
    {
        MCR_ITEMS = event.registerCreativeModeTab(new ResourceLocation(McRMod.MODID, "mcr"),
                builder -> builder.icon(() -> new ItemStack(McRBlockReg.INFUSER.get()))
                        .title(Component.translatable("creativemodetab.mcr")));
    }

}
