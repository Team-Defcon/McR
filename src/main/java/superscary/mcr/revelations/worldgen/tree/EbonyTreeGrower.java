package superscary.mcr.revelations.worldgen.tree;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;
import superscary.mcr.revelations.worldgen.ModConfiguredFeatures;

public class EbonyTreeGrower extends AbstractTreeGrower
{


    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature (RandomSource p_222910_, boolean p_222911_)
    {
        return ModConfiguredFeatures.EBONY_KEY;
    }
}