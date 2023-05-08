package superscary.mcr.revelations.blocks.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MachineBase extends Block
{
    public MachineBase (Properties p_49795_)
    {
        super(p_49795_);
    }

    @Override
    public void onBlockExploded (BlockState state, Level level, BlockPos pos, Explosion explosion)
    {
        this.destroy(level, pos, state);
        super.onBlockExploded(state, level, pos, explosion);
    }

    @Override
    public boolean canDropFromExplosion (BlockState state, BlockGetter level, BlockPos pos, Explosion explosion)
    {
        return true;
    }

}
