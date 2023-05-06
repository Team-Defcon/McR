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
import superscary.mcr.blocks.entity.ChemicalMixerEntity;
import superscary.mcr.gui.menu.ChemicalMixerMenu;
import superscary.mcr.gui.renderer.EnergyInfoArea;
import superscary.mcr.gui.renderer.FluidTankRenderer;
import superscary.mcr.toolkit.MouseUtil;

import java.util.Optional;

public class ChemicalMixerScreen extends AbstractContainerScreen<ChemicalMixerMenu>
{

    private static final ResourceLocation TEXTURE = new ResourceLocation(McRMod.MODID, "textures/gui/chemical_mixer_gui.png");
    private EnergyInfoArea energyInfoArea;
    private FluidTankRenderer inputRenderer;
    private FluidTankRenderer outputRenderer;

    public ChemicalMixerScreen (ChemicalMixerMenu menu, Inventory inventory, Component component)
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
        inputRenderer = new FluidTankRenderer(ChemicalMixerEntity.TANK_CAPACITY, true, 16, 61);
        outputRenderer = new FluidTankRenderer(ChemicalMixerEntity.TANK_CAPACITY, true, 16, 61);
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
            renderTooltip(pPoseStack, inputRenderer.getTooltip(menu.getInputFluidStack(), TooltipFlag.Default.NORMAL), Optional.empty(), pMouseX - x, pMouseY - y);
        }
        else if (isMouseAboveArea(pMouseX, pMouseY, x, y, 92, 15))
        {
            renderTooltip(pPoseStack, inputRenderer.getTooltip(menu.blockEntity.getOutputFluidStack(), TooltipFlag.Default.NORMAL), Optional.empty(), pMouseX - x, pMouseY - y);
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
        inputRenderer.render(pPoseStack, x + 46, y + 15, menu.getInputFluidStack());
        outputRenderer.render(pPoseStack, x + 92, y + 15, menu.getOutputFluidStack());
    }

    private void renderProgressArrow (PoseStack pPoseStack, int x, int y)
    {
        if (menu.isCrafting())
        {
            blit(pPoseStack, x + 64, y + 49, 176, 0, menu.getScaledProgress(), 8);
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
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, inputRenderer.getWidth(), inputRenderer.getHeight());
    }

    private boolean isMouseAboveArea (int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height)
    {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }


}
