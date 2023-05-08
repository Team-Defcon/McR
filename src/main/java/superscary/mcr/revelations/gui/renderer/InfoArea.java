package superscary.mcr.revelations.gui.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.Rect2i;

/**
 * ty BluSunrize
 */
public abstract class InfoArea extends GuiComponent
{

    public final Rect2i area;
    protected InfoArea (Rect2i area)
    {
        this.area = area;
    }

    public abstract void draw (PoseStack transform);

}
