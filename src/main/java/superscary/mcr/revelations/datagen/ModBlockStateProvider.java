package superscary.mcr.revelations.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import superscary.mcr.revelations.Revelations;
import superscary.mcr.revelations.blocks.McRBlockReg;

public class ModBlockStateProvider extends BlockStateProvider
{

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper)
    {
        super(output, Revelations.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
        blockWithItem(McRBlockReg.BLACK_OPAL_BLOCK);
        blockWithItem(McRBlockReg.BLACK_OPAL_ORE);
        blockWithItem(McRBlockReg.DEEPSLATE_BLACK_OPAL_ORE);
        blockWithItem(McRBlockReg.NETHERRACK_BLACK_OPAL_ORE);
        blockWithItem(McRBlockReg.ENDSTONE_BLACK_OPAL_ORE);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject)
    {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

}
