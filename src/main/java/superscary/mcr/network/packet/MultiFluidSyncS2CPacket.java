package superscary.mcr.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;
import superscary.mcr.blocks.entity.ChemicalMixerEntity;
import superscary.mcr.blocks.entity.InfuserBlockEntity;
import superscary.mcr.gui.menu.ChemicalMixerMenu;
import superscary.mcr.gui.menu.InfuserMenu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class MultiFluidSyncS2CPacket
{
    private final Collection<FluidStack> fluidStacks;
    private final BlockPos pos;

    public MultiFluidSyncS2CPacket (Collection<FluidStack> fluidStacks, BlockPos pos)
    {
        this.fluidStacks = fluidStacks;
        this.pos = pos;
    }

    public MultiFluidSyncS2CPacket (FriendlyByteBuf buf)
    {
        this.fluidStacks = buf.readCollection(ArrayList::new, FriendlyByteBuf::readFluidStack);
        this.pos = buf.readBlockPos();
    }

    public void toBytes (FriendlyByteBuf buf)
    {
        buf.writeCollection(fluidStacks, FriendlyByteBuf::writeFluidStack);
        buf.writeBlockPos(pos);
    }

    public boolean handle (Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() ->
        {
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof ChemicalMixerEntity blockEntity)
            {
                blockEntity.setInputFluid((FluidStack) fluidStacks.toArray()[0]);
                blockEntity.setOutputFluidStack((FluidStack) fluidStacks.toArray()[1]);

                if (Minecraft.getInstance().player.containerMenu instanceof ChemicalMixerMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(pos))
                {
                    menu.setInputFluid((FluidStack) fluidStacks.toArray()[0]);
                    menu.setOutputFluidStack((FluidStack) fluidStacks.toArray()[1]);
                }
            }

        });
        return true;
    }
}