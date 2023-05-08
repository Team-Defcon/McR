package superscary.mcr.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidHandlerBlockEntity;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import superscary.mcr.blocks.machine.InfuserBlock;
import superscary.mcr.gui.menu.InfuserMenu;
import superscary.mcr.network.ModMessages;
import superscary.mcr.network.packet.EnergySyncS2CPacket;
import superscary.mcr.network.packet.FluidSyncS2CPacket;
import superscary.mcr.network.packet.ItemStackSyncS2CPacket;
import superscary.mcr.recipe.InfuserRecipe;
import superscary.mcr.toolkit.ModEnergyStorage;

import java.util.Map;
import java.util.Optional;

public class BarrelEntity extends FluidHandlerBlockEntity
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

    //TODO: Figure out why nbt is not being saved.
    @Override
    protected void saveAdditional (CompoundTag nbt)
    {
        nbt = FLUID_TANK.writeToNBT(nbt);
        super.saveAdditional(nbt);
    }

    @Override
    public void load (CompoundTag nbt)
    {
        FLUID_TANK.readFromNBT(nbt);
        super.load(nbt);
    }

    public BarrelEntity getBlockEntity ()
    {
        return this;
    }

    public static void tick (Level level, BlockPos pos, BlockState state, BarrelEntity e)
    {
        ModMessages.sendToClients(new FluidSyncS2CPacket(e.getFluidStack(), pos));
    }

    public void fillTankWithFluid (FluidStack stack)
    {
        FLUID_TANK.fill(stack, IFluidHandler.FluidAction.EXECUTE);
    }

}
