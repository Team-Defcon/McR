package superscary.mcr.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import superscary.mcr.blocks.machine.CoalGeneratorBlock;
import superscary.mcr.config.McRCommonConfig;
import superscary.mcr.gui.menu.CoalGeneratorMenu;
import superscary.mcr.network.ModMessages;
import superscary.mcr.network.packet.EnergySyncS2CPacket;
import superscary.mcr.toolkit.ModEnergyStorage;

import java.util.Map;

public class EmptyMachineFrameEntity extends BlockEntity
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

