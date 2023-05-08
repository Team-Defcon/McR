package superscary.mcr.revelations.blocks.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import superscary.mcr.revelations.blocks.entity.InfuserBlockEntity;
import superscary.mcr.revelations.blocks.machine.InfuserBlock;
import superscary.mcr.revelations.config.McrClientConfig;

public class InfuserBlockEntityRenderer implements BlockEntityRenderer<InfuserBlockEntity>
{

    public InfuserBlockEntityRenderer (BlockEntityRendererProvider.Context context)
    {

    }

    @Override
    public void render (InfuserBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay)
    {
        if (!McrClientConfig.INSTANCE.displayItemOnInfuser.get()) return;

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        ItemStack itemStack = pBlockEntity.getRenderStack();
        pPoseStack.pushPose();
        pPoseStack.scale(.5f, .5f, .5f);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(0));

        switch (pBlockEntity.getBlockState().getValue(InfuserBlock.FACING))
        {
            case NORTH -> { //south done
                pPoseStack.translate(0.55f, 1.55f, -0.01f);
                pPoseStack.mulPose(Axis.YP.rotationDegrees(180));
            }
            case EAST -> { //west done
                pPoseStack.translate(2.01f, 1.55f, 0.55f);
                pPoseStack.mulPose(Axis.YP.rotationDegrees(90));
            }
            case SOUTH -> { //north done
                pPoseStack.translate(1.45f, 1.55f, 2.01f);
                pPoseStack.mulPose(Axis.YP.rotationDegrees(0));
            }
            case WEST -> { //east
                pPoseStack.translate(-0.01f, 1.55f, 1.45f);
                pPoseStack.mulPose(Axis.YP.rotationDegrees(270));
            }
        }

        itemRenderer.renderStatic(itemStack, ItemDisplayContext.GUI, getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, pPoseStack, pBufferSource, pBlockEntity.getLevel(), 1);
        pPoseStack.popPose();

    }

    private int getLightLevel (Level level, BlockPos pos)
    {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }

}
