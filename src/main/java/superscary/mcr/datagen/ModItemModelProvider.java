package superscary.mcr.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import superscary.mcr.McRMod;
import superscary.mcr.items.McRItemReg;

public class ModItemModelProvider extends ItemModelProvider
{

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper)
    {
        super(output, McRMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels()
    {
        simpleItem(McRItemReg.BLACK_OPAL);
        simpleItem(McRItemReg.RAW_BLACK_OPAL);
        simpleItem(McRItemReg.RUBBER);
        simpleItem(McRItemReg.STICKY_RESIN);
        simpleItem(McRItemReg.RAW_RUBBER);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item)
    {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(McRMod.MODID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item)
    {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/handheld"))
                .texture("layer0", new ResourceLocation(McRMod.MODID, "item/" + item.getId().getPath()));
    }

}
