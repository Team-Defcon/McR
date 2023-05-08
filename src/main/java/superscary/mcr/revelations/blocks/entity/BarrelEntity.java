package superscary.mcr.revelations.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import superscary.mcr.revelations.network.ModMessages;
import superscary.mcr.revelations.network.packet.FluidSyncS2CPacket;

public class BarrelEntity extends MachineBaseEntity
{

    public static final int TANK_CAPACITY = 64000;



    private final FluidTank FLUID_TANK = new FluidTank(TANK_CAPACITY)
    {
        @Override
        protected void onContentsChanged ()
        {
            setChanged();
            if (!level.isClientSide())
            {
                ModMessages.sendToClients(new FluidSyncS2CPacket(this.fluid, worldPosition));
            }
        }

        @Override
        public boolean isFluidValid (FluidStack stack)
        {
            if (FLUID_TANK.getFluidAmount() == 0)
            {
                return true;
            }
            else
            {
                return FLUID_TANK.getFluid().getFluid().equals(stack.getFluid());
            }
        }
    };

    public void setFluid (FluidStack stack)
    {
        this.FLUID_TANK.setFluid(stack);
    }

    public FluidStack getFluidStack()
    {
        return this.FLUID_TANK.getFluid();
    }

    public FluidTank getFluidTank ()
    {
        return FLUID_TANK;
    }

    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();
    public BarrelEntity (BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.BARREL.get(), pos, state);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability (@NotNull Capability<T> cap, @Nullable Direction side)
    {

        if (cap == ForgeCapabilities.FLUID_HANDLER)
        {
            return lazyFluidHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad ()
    {
        super.onLoad();
        lazyFluidHandler = LazyOptional.of(() -> FLUID_TANK);
    }

    @Override
    public void invalidateCaps ()
    {
        super.invalidateCaps();
        lazyFluidHandler.invalidate();
    }

    @Override
    public void saveAdditional (@NotNull CompoundTag nbt)
    {
        nbt = FLUID_TANK.writeToNBT(nbt);
        super.saveAdditional(nbt);
    }

    @Override
    public void load (@NotNull CompoundTag nbt)
    {
        FLUID_TANK.readFromNBT(nbt);
        super.load(nbt);
    }

    public BarrelEntity getBlockEntity ()
    {
        return this;
    }

    public static void tick (Level pLevel, BlockPos pPos, BlockState pState, BarrelEntity pEntity)
    {
        if (pLevel.isClientSide) return;
        ModMessages.sendToClients(new FluidSyncS2CPacket(pEntity.FLUID_TANK.getFluid(), pEntity.getBlockPos()));
        setChanged(pLevel, pPos, pState);
    }

    public void fillTankWithFluid (FluidStack stack)
    {
        FLUID_TANK.fill(stack, IFluidHandler.FluidAction.EXECUTE);
    }

}
