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
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import superscary.mcr.blocks.machine.ChemicalMixerBlock;
import superscary.mcr.gui.menu.ChemicalMixerMenu;
import superscary.mcr.network.ModMessages;
import superscary.mcr.network.packet.EnergySyncS2CPacket;
import superscary.mcr.network.packet.ItemStackSyncS2CPacket;
import superscary.mcr.network.packet.MultiFluidSyncS2CPacket;
import superscary.mcr.recipe.ChemicalMixerRecipe;
import superscary.mcr.toolkit.ModEnergyStorage;

import java.util.*;

public class ChemicalMixerEntity extends BlockEntity implements MenuProvider
{
    public static final int TANK_CAPACITY = 64000;

    private final ItemStackHandler itemHandler = new ItemStackHandler(4)
    {
        @Override
        protected void onContentsChanged (int slot)
        {
            setChanged();
            if (!level.isClientSide())
            {
                ModMessages.sendToClients(new ItemStackSyncS2CPacket(this, worldPosition));
            }
        }

        @Override
        public boolean isItemValid (int slot, @NotNull ItemStack stack)
        {
            return switch (slot)
            {
                case 0 -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
                case 1 -> true;
                case 2 -> false;
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    private final ModEnergyStorage ENERGY_STORAGE = new ModEnergyStorage(60000, 256)
    {
        @Override
        public void onEnergyChanged ()
        {
            setChanged();
            ModMessages.sendToClients(new EnergySyncS2CPacket(this.energy, getBlockPos()));
        }
    };

    private static final int ENERGY_REQ = 32;

    private final FluidTank INPUT_FLUID_TANK = new FluidTank(TANK_CAPACITY)
    {
        @Override
        protected void onContentsChanged ()
        {
            setChanged();
            if (!level.isClientSide())
            {
                ModMessages.sendToClients(new MultiFluidSyncS2CPacket(List.of(getInputFluidStack(), getOutputFluidStack()), worldPosition));
            }
        }

        @Override
        public boolean isFluidValid (FluidStack stack)
        {
            if (INPUT_FLUID_TANK.getFluidAmount() == 0)
            {
                return true;
            }
            else
            {
                return INPUT_FLUID_TANK.getFluid().getFluid().equals(stack.getFluid());
            }
        }
    };

    private final FluidTank OUTPUT_FLUID_TANK = new FluidTank(TANK_CAPACITY)
    {
        @Override
        protected void onContentsChanged ()
        {
            setChanged();
            if (!level.isClientSide())
            {
                ModMessages.sendToClients(new MultiFluidSyncS2CPacket(List.of(getInputFluidStack(), getOutputFluidStack()), worldPosition));
            }
        }

        @Override
        public boolean isFluidValid (FluidStack stack)
        {
            if (OUTPUT_FLUID_TANK.getFluidAmount() == 0)
            {
                return true;
            }
            else
            {
                return OUTPUT_FLUID_TANK.getFluid().getFluid().equals(stack.getFluid());
            }
        }
    };

    public void setInputFluid (FluidStack stack)
    {
        this.INPUT_FLUID_TANK.setFluid(stack);
    }

    public FluidStack getInputFluidStack()
    {
        return this.INPUT_FLUID_TANK.getFluid();
    }

    public void setOutputFluidStack (FluidStack stack)
    {
        this.OUTPUT_FLUID_TANK.setFluid(stack);
    }

    public FluidStack getOutputFluidStack()
    {
        return this.OUTPUT_FLUID_TANK.getFluid();
    }

    public ItemStack getRenderStack ()
    {
        ItemStack stack;
        if (!itemHandler.getStackInSlot(2).isEmpty())
        {
            stack = itemHandler.getStackInSlot(2);
        }
        else stack = itemHandler.getStackInSlot(1);

        return stack;
    }

    public void setHandler (ItemStackHandler itemStackHandler)
    {
        for (int i = 0; i < itemStackHandler.getSlots(); i++)
        {
            itemHandler.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
        }
    }

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyInputFluidHandler = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyOutputFluidHandler = LazyOptional.empty();
    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(Direction.DOWN,      LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 2, (i, s) -> false)),
                    Direction.NORTH,    LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 1, (index, stack) -> itemHandler.isItemValid(1, stack))),
                    Direction.SOUTH,    LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 2, (i, s) -> false)),
                    Direction.EAST,     LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 1, (index, stack) -> itemHandler.isItemValid(1, stack))),
                    Direction.WEST,     LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0 || index == 1, (index, stack) -> itemHandler.isItemValid(0, stack) || itemHandler.isItemValid(1, stack))));

    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    public ChemicalMixerEntity (BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.CHEMICAL_MIXER.get(), pos, state);
        this.data = new ContainerData()
        {
            @Override
            public int get (int index)
            {
                return switch (index)
                {
                    case 0 -> ChemicalMixerEntity.this.progress;
                    case 1 -> ChemicalMixerEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set (int index, int value)
            {

                switch (index)
                {
                    case 0 -> ChemicalMixerEntity.this.progress = value;
                    case 1 -> ChemicalMixerEntity.this.maxProgress = value;
                }

            }

            @Override
            public int getCount ()
            {
                return 2;
            }
        };
    }

    @Override
    public @NotNull Component getDisplayName ()
    {
        return Component.translatable("gui.mcr.chemical_mixer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu (int id, @NotNull Inventory inventory, @NotNull Player player)
    {
        ModMessages.sendToClients(new EnergySyncS2CPacket(this.ENERGY_STORAGE.getEnergyStored(), getBlockPos()));
        ModMessages.sendToClients(new MultiFluidSyncS2CPacket(List.of(getInputFluidStack(), getOutputFluidStack()), getBlockPos()));
        return new ChemicalMixerMenu(id, inventory, this, this.data);
    }

    public IEnergyStorage getEnergyStorage ()
    {
        return ENERGY_STORAGE;
    }

    public void setEnergyLevel (int energy)
    {
        this.ENERGY_STORAGE.setEnergy(energy);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability (@NotNull Capability<T> cap, @Nullable Direction side)
    {

        if (cap == ForgeCapabilities.ENERGY)
        {
            return lazyEnergyHandler.cast();
        }

        if (cap == ForgeCapabilities.ITEM_HANDLER)
        {
            if (side == null)
            {
                return lazyItemHandler.cast();
            }

            if (directionWrappedHandlerMap.containsKey(side))
            {
                Direction localDir = this.getBlockState().getValue(ChemicalMixerBlock.FACING);

                if (side == Direction.UP || side == Direction.DOWN)
                {
                    return directionWrappedHandlerMap.get(side).cast();
                }

                return switch (localDir)
                {
                    default -> directionWrappedHandlerMap.get(side.getOpposite()).cast();
                    case EAST -> directionWrappedHandlerMap.get(side.getClockWise()).cast();
                    case SOUTH -> directionWrappedHandlerMap.get(side).cast();
                    case WEST -> directionWrappedHandlerMap.get(side.getCounterClockWise()).cast();
                };

            }

        }

        if (cap == ForgeCapabilities.FLUID_HANDLER)
        {
            if (side == null)
            {
                return lazyInputFluidHandler.cast();
            }

            if (directionWrappedHandlerMap.containsKey(side))
            {
                Direction localDir = this.getBlockState().getValue(ChemicalMixerBlock.FACING);

                if (side == Direction.UP || side == Direction.DOWN)
                {
                    return directionWrappedHandlerMap.get(side).cast();
                }

                return switch (localDir)
                {
                    default -> directionWrappedHandlerMap.get(side.getOpposite()).cast();
                    case EAST -> lazyOutputFluidHandler.cast();
                    case SOUTH -> directionWrappedHandlerMap.get(side).cast();
                    case WEST -> directionWrappedHandlerMap.get(side.getCounterClockWise()).cast();
                };

            }

        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad ()
    {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
        lazyInputFluidHandler = LazyOptional.of(() -> INPUT_FLUID_TANK);
        lazyOutputFluidHandler = LazyOptional.of(() -> OUTPUT_FLUID_TANK);
    }

    @Override
    public void invalidateCaps ()
    {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
        lazyInputFluidHandler.invalidate();
        lazyOutputFluidHandler.invalidate();
    }

    @Override
    protected void saveAdditional (CompoundTag nbt)
    {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("chemwasher.progress", this.progress);
        nbt.putInt("chemwasher.energy", ENERGY_STORAGE.getEnergyStored());
        nbt = INPUT_FLUID_TANK.writeToNBT(nbt);
        nbt = OUTPUT_FLUID_TANK.writeToNBT(nbt);

        super.saveAdditional(nbt);
    }

    @Override
    public void load (CompoundTag nbt)
    {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("chemwasher.progress");
        ENERGY_STORAGE.setEnergy(nbt.getInt("chemwasher.energy"));
        INPUT_FLUID_TANK.readFromNBT(nbt);
        OUTPUT_FLUID_TANK.readFromNBT(nbt);
    }

    public void drops ()
    {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());

        for (int i = 0; i < itemHandler.getSlots(); i++)
        {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(getLevel(), getBlockPos(), inventory);

    }

    public static void tick (Level level, BlockPos pos, BlockState state, ChemicalMixerEntity pEntity)
    {
        if (level.isClientSide)
        {
            return;
        }

        ModMessages.sendToClients(new ItemStackSyncS2CPacket(pEntity.itemHandler, pEntity.getBlockPos()));

        if (hasRecipe(pEntity) && hasEnoughEnergy(pEntity))
        {
            pEntity.progress++;
            extractEnergy(pEntity);
            setChanged(level, pos, state);

            if (pEntity.progress >= pEntity.maxProgress)
            {
                craftItem(pEntity);
            }
        }
        else
        {
            pEntity.resetProgress();
            setChanged(level, pos, state);
        }

        if (hasFluidItemInSourceSlot(pEntity))
        {
            transferItemFluidToFluidTank(pEntity);
        }

    }

    private static void transferItemFluidToFluidTank (ChemicalMixerEntity pEntity)
    {
        pEntity.itemHandler.getStackInSlot(0).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
            int drainAmount = Math.min(pEntity.INPUT_FLUID_TANK.getSpace(), 1000);

            FluidStack stack = handler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
            if (pEntity.INPUT_FLUID_TANK.isFluidValid(stack))
            {
                stack = handler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
                fillTankWithFluid(pEntity, stack, handler.getContainer());
            }

        });

    }

    private static void fillTankWithFluid (ChemicalMixerEntity pEntity, FluidStack stack, ItemStack container)
    {
        pEntity.INPUT_FLUID_TANK.fill(stack, IFluidHandler.FluidAction.EXECUTE);
        pEntity.itemHandler.extractItem(0, 1, false);
        pEntity.itemHandler.insertItem(0, container, false);
    }

    private static boolean hasFluidItemInSourceSlot (ChemicalMixerEntity pEntity)
    {
        return pEntity.itemHandler.getStackInSlot(0).getCount() > 0;
    }

    private static void extractEnergy (ChemicalMixerEntity pEntity)
    {
        pEntity.ENERGY_STORAGE.extractEnergy(ENERGY_REQ, false);
    }

    private static boolean hasEnoughEnergy (ChemicalMixerEntity pEntity)
    {
        return pEntity.ENERGY_STORAGE.getEnergyStored() >= ENERGY_REQ * pEntity.maxProgress;
    }

    private void resetProgress ()
    {
        this.progress = 0;
    }

    private static void craftItem (ChemicalMixerEntity pEntity)
    {
        Level level = pEntity.level;

        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());
        for (int i = 0; i < pEntity.itemHandler.getSlots(); i++)
        {
            inventory.setItem(i, pEntity.itemHandler.getStackInSlot(i));
        }

        Optional<ChemicalMixerRecipe> recipe = level.getRecipeManager().getRecipeFor(ChemicalMixerRecipe.Type.INSTANCE, inventory, level);

        if (hasRecipe(pEntity))
        {
            pEntity.INPUT_FLUID_TANK.drain(recipe.get().getFluid().getAmount(), IFluidHandler.FluidAction.EXECUTE);
            pEntity.itemHandler.extractItem(1, 1, false);
            if (canFillOutput(pEntity, 1000))
            {
                pEntity.OUTPUT_FLUID_TANK.setFluid(new FluidStack(recipe.get().getOutputFluid(), 1000 + recipe.get().getOutputFluid().getAmount()));
            }

            pEntity.resetProgress();
        }

    }

    private static boolean canFillOutput (ChemicalMixerEntity pEntity, int amount)
    {
        return amount <= pEntity.OUTPUT_FLUID_TANK.getSpace();
    }

    private static boolean hasRecipe (ChemicalMixerEntity entity)
    {
        Level level = entity.level;

        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++)
        {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        Optional<ChemicalMixerRecipe> recipe = level.getRecipeManager().getRecipeFor(ChemicalMixerRecipe.Type.INSTANCE, inventory, level);

        return recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory) && canInsertItemIntoOutputSlot(inventory, recipe.get().getResultItem(RegistryAccess.EMPTY)) && hasCorrectFluidInTank(entity, recipe) && hasCorrectFluidAmountInTank(entity, recipe);

    }

    private static boolean hasCorrectFluidAmountInTank (ChemicalMixerEntity entity, Optional<ChemicalMixerRecipe> recipe)
    {
        return entity.INPUT_FLUID_TANK.getFluidAmount() >= recipe.get().getFluid().getAmount();
    }

    private static boolean hasCorrectFluidInTank (ChemicalMixerEntity entity, Optional<ChemicalMixerRecipe> recipe)
    {
        return recipe.get().getFluid().equals(entity.INPUT_FLUID_TANK.getFluid());
    }

    private static boolean canInsertItemIntoOutputSlot (SimpleContainer inventory, ItemStack stack)
    {
        return inventory.getItem(2).getItem() == stack.getItem() || inventory.getItem(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot (SimpleContainer inventory)
    {
        return inventory.getItem(2).getMaxStackSize() > inventory.getItem(2).getCount();
    }

    public ChemicalMixerEntity getBlockEntity ()
    {
        return this;
    }
}
