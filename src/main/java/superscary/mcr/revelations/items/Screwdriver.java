package superscary.mcr.revelations.items;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import superscary.mcr.revelations.blocks.machine.EntityMachineBase;
import superscary.mcr.revelations.toolkit.KeyboardHelper;

import javax.annotation.Nullable;
import java.util.List;

public class Screwdriver extends Item
{
    public Screwdriver (Item.Properties properties)
    {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag)
    {
        if (KeyboardHelper.isHoldingShift())
        {
            tooltip.add(Component.literal("Tool for working on McR blocks"));
        }
        else
        {
            tooltip.add(Component.literal("Press SHIFT for more"));
        }

        super.appendHoverText(stack, world, tooltip, flag);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use (@NotNull Level world, @NotNull Player player, @NotNull InteractionHand hand)
    {
        BlockHitResult ray = getPlayerPOVHitResult(world, player, ClipContext.Fluid.NONE);
        BlockPos lookPos = ray.getBlockPos();
        Block block = world.getBlockState(lookPos).getBlock();

        if (KeyboardHelper.isHoldingShift() && block instanceof EntityMachineBase base)
        {
            base.rotate(world.getBlockState(lookPos), world, lookPos, Rotation.CLOCKWISE_90);
        }

        return super.use(world, player, hand);

    }

}
