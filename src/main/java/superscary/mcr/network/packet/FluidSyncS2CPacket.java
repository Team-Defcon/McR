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

import java.util.function.Supplier;

public class FluidSyncS2CPacket
{
    private final FluidStack inputFluidStack;
    private final FluidStack outputFluidStack;
    private final BlockPos pos;

    public FluidSyncS2CPacket (FluidStack fluidStack, BlockPos pos)
    {
        this.inputFluidStack = fluidStack;
        this.outputFluidStack = null;
        this.pos = pos;
    }

    public FluidSyncS2CPacket (FluidStack inputFluidStack, FluidStack outputFluidStack, BlockPos pos)
    {
        this.inputFluidStack = inputFluidStack;
        this.outputFluidStack = outputFluidStack;
        this.pos = pos;
    }

    public FluidSyncS2CPacket (FriendlyByteBuf buf)
    {
        this.inputFluidStack = buf.readFluidStack();
        this.outputFluidStack = buf.readFluidStack();
        this.pos = buf.readBlockPos();
    }

    public void toBytes (FriendlyByteBuf buf)
    {
        buf.writeFluidStack(inputFluidStack);
        buf.writeFluidStack(outputFluidStack);
        buf.writeBlockPos(pos);
    }

    public boolean handle (Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() ->
        {
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof InfuserBlockEntity blockEntity)
            {
                blockEntity.setFluid(this.inputFluidStack);

                if (Minecraft.getInstance().player.containerMenu instanceof InfuserMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(pos))
                {
                    menu.setFluid(this.inputFluidStack);
                }
            }

            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof ChemicalMixerEntity blockEntity)
            {
                blockEntity.setInputFluid(this.inputFluidStack);
                blockEntity.setOutputFluidStack(this.outputFluidStack);

                if (Minecraft.getInstance().player.containerMenu instanceof ChemicalMixerMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(pos))
                {
                    menu.setInputFluid(this.inputFluidStack);
                    menu.setOutputFluidStack(this.outputFluidStack);
                }
            }

        });
        return true;
    }
}