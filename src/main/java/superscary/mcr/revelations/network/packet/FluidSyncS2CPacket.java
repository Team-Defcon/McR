package superscary.mcr.revelations.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;
import superscary.mcr.revelations.blocks.entity.BarrelEntity;
import superscary.mcr.revelations.blocks.entity.InfuserBlockEntity;
import superscary.mcr.revelations.gui.menu.InfuserMenu;

import java.util.function.Supplier;

public class FluidSyncS2CPacket
{
    private final FluidStack inputFluidStack;
    private final BlockPos pos;

    public FluidSyncS2CPacket (FluidStack fluidStack, BlockPos pos)
    {
        this.inputFluidStack = fluidStack;
        this.pos = pos;
    }

    public FluidSyncS2CPacket (FriendlyByteBuf buf)
    {
        this.inputFluidStack = buf.readFluidStack();
        this.pos = buf.readBlockPos();
    }

    public void toBytes (FriendlyByteBuf buf)
    {
        buf.writeFluidStack(inputFluidStack);
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

            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof BarrelEntity blockEntity)
            {
                blockEntity.setFluid(this.inputFluidStack);
            }

        });
        return true;
    }
}