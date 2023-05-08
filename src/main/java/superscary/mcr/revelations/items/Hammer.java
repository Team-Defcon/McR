package superscary.mcr.revelations.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import superscary.mcr.revelations.blocks.machine.EntityMachineBase;
import superscary.mcr.revelations.toolkit.KeyboardHelper;

public class Hammer extends Item
{

    public Hammer (Properties properties)
    {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use (@NotNull Level world, @NotNull Player player, @NotNull InteractionHand hand)
    {
        BlockHitResult ray = getPlayerPOVHitResult(world, player, ClipContext.Fluid.NONE);
        BlockPos lookPos = ray.getBlockPos();
        Block block = world.getBlockState(lookPos).getBlock();

        if (KeyboardHelper.isHoldingShift() && block instanceof EntityMachineBase)
        {
            ItemStack stack = player.getItemInHand(hand);
            stack.setDamageValue(stack.getDamageValue() + 1);
            block.playerDestroy(world, player, lookPos, world.getBlockState(lookPos), null, new ItemStack(block.asItem()));
            world.destroyBlock(lookPos, true);
            if (stack.getDamageValue() >= stack.getMaxDamage()) stack.setCount(0);
            return super.use(world, player, hand);
        }
        return super.use(world, player, hand);
    }

}
