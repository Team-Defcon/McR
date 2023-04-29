package superscary.mcr.datagen;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import superscary.mcr.blocks.McRBlockReg;
import superscary.mcr.items.McRItemReg;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider
{


    protected ModBlockLootTables ()
    {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate ()
    {
        dropSelf(McRBlockReg.BLACK_OPAL_BLOCK.get());

        add(McRBlockReg.BLACK_OPAL_ORE.get(),(block -> createOreDrop(McRBlockReg.BLACK_OPAL_ORE.get(), McRItemReg.BLACK_OPAL.get())));
        add(McRBlockReg.DEEPSLATE_BLACK_OPAL_ORE.get(),(block -> createOreDrop(McRBlockReg.DEEPSLATE_BLACK_OPAL_ORE.get(), McRItemReg.BLACK_OPAL.get())));
        add(McRBlockReg.NETHERRACK_BLACK_OPAL_ORE.get(),(block -> createOreDrop(McRBlockReg.NETHERRACK_BLACK_OPAL_ORE.get(), McRItemReg.BLACK_OPAL.get())));
        add(McRBlockReg.ENDSTONE_BLACK_OPAL_ORE.get(),(block -> createOreDrop(McRBlockReg.ENDSTONE_BLACK_OPAL_ORE.get(), McRItemReg.BLACK_OPAL.get())));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks ()
    {
        return McRBlockReg.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }

}
