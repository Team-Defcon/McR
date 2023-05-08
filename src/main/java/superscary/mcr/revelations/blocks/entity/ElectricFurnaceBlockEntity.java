package superscary.mcr.revelations.blocks.entity;

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
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import superscary.mcr.revelations.blocks.machine.ElectricFurnaceBlock;
import superscary.mcr.revelations.items.McRItemReg;
import superscary.mcr.revelations.network.ModMessages;
import superscary.mcr.revelations.network.packet.EnergySyncS2CPacket;
import superscary.mcr.revelations.gui.menu.ElectricFurnaceMenu;
import superscary.mcr.revelations.toolkit.ModEnergyStorage;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class ElectricFurnaceBlockEntity extends MachineBaseEntity implements MenuProvider
{
    private final ItemStackHandler itemHandler = new ItemStackHandler(3)
    {
        @Override
        protected void onContentsChanged (int slot)
        {
            setChanged();
        }

        @Override
        public boolean isItemValid (int slot, @NotNull ItemStack stack)
        {
            return switch (slot)
            {
                case 0 -> true;
                case 1, 2 -> false;
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

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
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

    public ElectricFurnaceBlockEntity (BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.ELECTRIC_FURNACE.get(), pos, state);
        this.data = new ContainerData()
        {
            @Override
            public int get (int index)
            {
                return switch (index)
                {
                    case 0 -> ElectricFurnaceBlockEntity.this.progress;
                    case 1 -> ElectricFurnaceBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set (int index, int value)
            {

                switch (index)
                {
                    case 0 -> ElectricFurnaceBlockEntity.this.progress = value;
                    case 1 -> ElectricFurnaceBlockEntity.this.maxProgress = value;
                }

            }

            @Override
            public int getCount ()
            {
                return 3;
            }
        };
    }

    @Override
    public @NotNull Component getDisplayName ()
    {
        return Component.translatable("gui.mcr.electric_furnace");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu (int id, @NotNull Inventory inventory, @NotNull Player player)
    {
        ModMessages.sendToClients(new EnergySyncS2CPacket(this.ENERGY_STORAGE.getEnergyStored(), getBlockPos()));
        return new ElectricFurnaceMenu(id, inventory, this, this.data);
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
                Direction localDir = this.getBlockState().getValue(ElectricFurnaceBlock.FACING);

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

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad ()
    {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
    }

    @Override
    public void invalidateCaps ()
    {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
    }

    @Override
    protected void saveAdditional (CompoundTag nbt)
    {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("efurnace.progress", this.progress);
        nbt.putInt("efurnace.energy", ENERGY_STORAGE.getEnergyStored());

        super.saveAdditional(nbt);
    }

    @Override
    public void load (CompoundTag nbt)
    {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("efurnace.progress");
        ENERGY_STORAGE.setEnergy(nbt.getInt("efurnace.energy"));
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

    public static void tick (Level level, BlockPos pos, BlockState state, ElectricFurnaceBlockEntity pEntity)
    {
        if (level.isClientSide)
        {
            return;
        }

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


    }

    private static void extractEnergy (ElectricFurnaceBlockEntity pEntity)
    {
        pEntity.ENERGY_STORAGE.extractEnergy(ENERGY_REQ, false);
    }

    private static boolean hasEnoughEnergy (ElectricFurnaceBlockEntity pEntity)
    {
        return pEntity.ENERGY_STORAGE.getEnergyStored() >= ENERGY_REQ * pEntity.maxProgress;
    }

    private void resetProgress ()
    {
        this.progress = 0;
    }

    private static void craftItem (ElectricFurnaceBlockEntity pEntity)
    {
        Level level = pEntity.level;

        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());
        for (int i = 0; i < pEntity.itemHandler.getSlots(); i++)
        {
            inventory.setItem(i, pEntity.itemHandler.getStackInSlot(i));
        }

        Optional<SmeltingRecipe> recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, inventory, level);

        if (hasRecipe(pEntity))
        {
            pEntity.itemHandler.extractItem(0, 1, false);
            pEntity.itemHandler.setStackInSlot(1, new ItemStack(recipe.get().getResultItem(RegistryAccess.EMPTY).getItem(), pEntity.itemHandler.getStackInSlot(1).getCount() + recipe.get().getResultItem(RegistryAccess.EMPTY).getCount()));

            Random random = new Random();
            int slagRandom = random.nextInt(1, 11);

            if (pEntity.itemHandler.getStackInSlot(2).getItem().equals(McRItemReg.SLAG.get()) || pEntity.itemHandler.getStackInSlot(2).getItem().equals(ItemStack.EMPTY.getItem()))
            {
                int putAmount = slagRandom;
                if (canAddSlagWithChance(pEntity, putAmount))
                {
                    int currentInSlot = pEntity.itemHandler.getStackInSlot(2).getCount();
                    pEntity.itemHandler.setStackInSlot(2, new ItemStack(McRItemReg.SLAG.get(), 1 + currentInSlot));
                }
            }

            pEntity.resetProgress();
        }

    }

    private static boolean canAddSlagWithChance (ElectricFurnaceBlockEntity pEntity, int putAmount)
    {
        return pEntity.itemHandler.getStackInSlot(2).getCount() + 1 <= 64 && putAmount == 1;
    }

    private static boolean hasRecipe (ElectricFurnaceBlockEntity entity)
    {
        Level level = entity.level;

        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++)
        {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        Optional<SmeltingRecipe> recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, inventory, level);

        return recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory) && canInsertItemIntoOutputSlot(inventory, recipe.get().getResultItem(RegistryAccess.EMPTY));
    }

    private static boolean canInsertItemIntoOutputSlot (SimpleContainer inventory, ItemStack stack)
    {
        return inventory.getItem(1).getItem() == stack.getItem() || inventory.getItem(1).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot (SimpleContainer inventory)
    {
        return inventory.getItem(1).getMaxStackSize() > inventory.getItem(1).getCount();
    }

    public ElectricFurnaceBlockEntity getBlockEntity ()
    {
        return this;
    }
}
