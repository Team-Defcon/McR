package superscary.mcr.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import superscary.mcr.McRMod;
import superscary.mcr.blocks.entity.InfuserBlockEntity;
import superscary.mcr.gui.menu.InfuserMenu;
import superscary.mcr.gui.renderer.EnergyInfoArea;
import superscary.mcr.gui.renderer.FluidTankRenderer;
import superscary.mcr.toolkit.MouseUtil;

import java.util.Optional;

public class InfuserScreen extends AbstractContainerScreen<InfuserMenu>
{

    private static final ResourceLocation TEXTURE = new ResourceLocation(McRMod.MODID, "textures/gui/infuser_gui.png");
    private EnergyInfoArea energyInfoArea;
    private FluidTankRenderer renderer;

    public InfuserScreen (InfuserMenu menu, Inventory inventory, Component component)
    {
        super(menu, inventory, component);
    }

    @Override
    protected void init ()
    {
        super.init();
        assignEnergyInfoArea();
        assignFluidRenderer();
    }

    private void assignFluidRenderer ()
    {
        renderer = new FluidTankRenderer(InfuserBlockEntity.TANK_CAPACITY, true, 16, 61);
    }

    private void assignEnergyInfoArea ()
    {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        energyInfoArea = new EnergyInfoArea(x + 156 ,y + 13, menu.blockEntity.getEnergyStorage());
    }

    @Override
    protected void renderLabels (@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY)
    {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        renderEnergyAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);
        renderFluidAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);
    }

    private void renderFluidAreaTooltips (PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y)
    {
        if (isMouseAboveArea(pMouseX, pMouseY, x, y, 55, 15))
        {
            renderTooltip(pPoseStack, renderer.getTooltip(menu.getFluidStack(), TooltipFlag.Default.NORMAL), Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    private void renderEnergyAreaTooltips (PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y)
    {

        if (isMouseAboveArea(pMouseX, pMouseY, x, y, 156, 13, 8, 64))
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

        FormattedCharSequence formattedcharsequence = menu.blockEntity.getDisplayName().getVisualOrderText();
        Font f = Minecraft.getInstance().font;
        Minecraft.getInstance().font.draw(pPoseStack, formattedcharsequence, (float) (x - f.width(formattedcharsequence) / 2) + 88, y + 4, 0x555555);
        renderProgressArrow(pPoseStack, x, y);
        energyInfoArea.draw(pPoseStack);
        renderer.render(pPoseStack, x + 55, y + 15, menu.getFluidStack());
    }

    private void renderProgressArrow (PoseStack pPoseStack, int x, int y)
    {
        if (menu.isCrafting())
        {
            blit(pPoseStack, x + 105, y + 33, 176, 0, 8, menu.getScaledProgress());
        }
    }

    @Override
    public void render (@NotNull PoseStack pPoseStack, int mouseX, int mouseY, float delta)
    {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }

    private boolean isMouseAboveArea (int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY)
    {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, renderer.getWidth(), renderer.getHeight());
    }

    private boolean isMouseAboveArea (int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height)
    {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }

}
