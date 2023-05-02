package superscary.mcr.gui;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import superscary.mcr.McRMod;

public class ModMenuTypes
{

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, McRMod.MODID);

    public static final RegistryObject<MenuType<InfuserMenu>> INFUSER_MENU = registerMenuType(InfuserMenu::new, "infuser_menu");
    public static final RegistryObject<MenuType<ElectricFurnaceMenu>> ELECTRIC_FURNACE_MENU = registerMenuType(ElectricFurnaceMenu::new, "electric_furnace_menu");
    public static final RegistryObject<MenuType<CoalGeneratorMenu>> COAL_GENERATOR_MENU = registerMenuType(CoalGeneratorMenu::new, "coal_generator_menu");


    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType (IContainerFactory<T> factory, String name)
    {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register (IEventBus eventBus)
    {
        MENUS.register(eventBus);
    }

}
