package superscary.mcr.revelations.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EmptyMachineFrameEntity extends MachineBaseEntity
{

    public EmptyMachineFrameEntity (BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.EMPTY_MACHINE_FRAME.get(), pos, state);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability (@NotNull Capability<T> cap, @Nullable Direction side)
    {
        return super.getCapability(cap, side);
    }

    public EmptyMachineFrameEntity getBlockEntity ()
    {
        return this;
    }
}

