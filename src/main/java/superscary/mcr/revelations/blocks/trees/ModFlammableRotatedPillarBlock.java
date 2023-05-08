package superscary.mcr.revelations.blocks.trees;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import org.jetbrains.annotations.Nullable;
import superscary.mcr.revelations.blocks.McRBlockReg;

public class ModFlammableRotatedPillarBlock extends RotatedPillarBlock
{

    public ModFlammableRotatedPillarBlock (Properties properties)
    {
        super(properties);
    }

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
        return 5;
    }

    @Override
    public @Nullable BlockState getToolModifiedState (BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate)
    {
        if (context.getItemInHand().getItem() instanceof AxeItem)
        {
            if (state.is(McRBlockReg.RUBBER_LOG.get()))
            {
                return McRBlockReg.STRIPPED_RUBBER_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }

            if (state.is(McRBlockReg.RUBBER_WOOD.get()))
            {
                return McRBlockReg.STRIPPED_RUBBER_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }

        }

        return super.getToolModifiedState(state, context, toolAction, simulate);
    }
}
