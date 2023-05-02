package superscary.mcr.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import superscary.mcr.McRMod;
import superscary.mcr.gui.menu.ExtruderMenu;
import superscary.mcr.gui.renderer.EnergyInfoArea;
import superscary.mcr.toolkit.MouseUtil;

import java.util.Optional;

public class ExtruderScreen extends AbstractContainerScreen<ExtruderMenu>
{

    private static final ResourceLocation TEXTURE = new ResourceLocation(McRMod.MODID, "textures/gui/extruder_gui.png");
    private EnergyInfoArea energyInfoArea;

    public ExtruderScreen (ExtruderMenu menu, Inventory inventory, Component component)
    {
        super(menu, inventory, component);
    }

    @Override
    protected void init ()
    {
        super.init();
        assignEnergyInfoArea();
    }

    private void assignEnergyInfoArea ()
    {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        energyInfoArea = new EnergyInfoArea(x + 10 ,y + 9, menu.blockEntity.getEnergyStorage());
    }

    @Override
    protected void renderLabels (@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY)
    {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        renderEnergyAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);
    }

    private void renderEnergyAreaTooltips (PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y)
    {

        if (isMouseAboveArea(pMouseX, pMouseY, x, y, 10, 9, 8, 64))
        {
            renderTooltip(pPoseStack, energyInfoArea.getTooltips(), Optional.empty(), pMouseX - x, pMouseY - y);
        }

    }

    @Override
    protected void renderBg (@NotNull PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(pPoseStack, x, y);
        energyInfoArea.draw(pPoseStack);
    }

    private void renderProgressArrow (PoseStack pPoseStack, int x, int y)
    {
        if (menu.isCrafting())
        {
            blit(pPoseStack, x + 80, y + 35, 176, 0, menu.getScaledProgress(), 24);
        }
    }

    @Override
    public void render (@NotNull PoseStack pPoseStack, int mouseX, int mouseY, float delta)
    {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }

    private boolean isMouseAboveArea (int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height)
    {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }

}
