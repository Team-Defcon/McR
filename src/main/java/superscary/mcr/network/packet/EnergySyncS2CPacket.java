package superscary.mcr.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import superscary.mcr.blocks.entity.*;
import superscary.mcr.gui.menu.*;

import java.util.function.Supplier;

public class EnergySyncS2CPacket
{

    private final int energy;
    private final BlockPos pos;

    public EnergySyncS2CPacket (int energy, BlockPos pos)
    {
        this.energy = energy;
        this.pos = pos;
    }

    public EnergySyncS2CPacket (FriendlyByteBuf buf)
    {
        this.energy = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes (FriendlyByteBuf buf)
    {
        buf.writeInt(energy);
        buf.writeBlockPos(pos);
    }

    public boolean handle (Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
           if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof InfuserBlockEntity blockEntity)
           {
               blockEntity.setEnergyLevel(energy);

               if (Minecraft.getInstance().player.containerMenu instanceof InfuserMenu menu && menu.blockEntity.getBlockEntity().getBlockPos().equals(pos))
               {
                   blockEntity.setEnergyLevel(energy);
               }

           }

            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof ElectricFurnaceBlockEntity blockEntity)
            {
                blockEntity.setEnergyLevel(energy);

                if (Minecraft.getInstance().player.containerMenu instanceof ElectricFurnaceMenu menu && menu.blockEntity.getBlockEntity().getBlockPos().equals(pos))
                {
                    blockEntity.setEnergyLevel(energy);
                }

            }

            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof CoalGeneratorEntity blockEntity)
            {
                blockEntity.setEnergyLevel(energy);

                if (Minecraft.getInstance().player.containerMenu instanceof CoalGeneratorMenu menu && menu.blockEntity.getBlockEntity().getBlockPos().equals(pos))
                {
                    blockEntity.setEnergyLevel(energy);
                }

            }

            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof CompressorEntity blockEntity)
            {
                blockEntity.setEnergyLevel(energy);

                if (Minecraft.getInstance().player.containerMenu instanceof CompressorMenu menu && menu.blockEntity.getBlockEntity().getBlockPos().equals(pos))
                {
                    blockEntity.setEnergyLevel(energy);
                }

            }

            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof ExtruderEntity blockEntity)
            {
                blockEntity.setEnergyLevel(energy);

                if (Minecraft.getInstance().player.containerMenu instanceof ExtruderMenu menu && menu.blockEntity.getBlockEntity().getBlockPos().equals(pos))
                {
                    blockEntity.setEnergyLevel(energy);
                }

            }

            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof ChemicalMixerEntity blockEntity)
            {
                blockEntity.setEnergyLevel(energy);

                if (Minecraft.getInstance().player.containerMenu instanceof ChemicalMixerMenu menu && menu.blockEntity.getBlockEntity().getBlockPos().equals(pos))
                {
                    blockEntity.setEnergyLevel(energy);
                }

            }

        });

        return true;
    }

}
