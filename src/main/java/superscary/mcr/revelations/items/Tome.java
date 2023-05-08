package superscary.mcr.revelations.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Tome extends Item
{

    public Tome (Properties pProperties)
    {
        super(pProperties);
    }

    @Override
    public boolean isEnchantable (ItemStack pStack)
    {
        return false;
    }

    @Override
    public boolean isEdible ()
    {
        return false;
    }

    @Override
    public InteractionResultHolder use (Level pLevel, Player pPlayer, InteractionHand pUsedHand)
    {
        if (!pLevel.isClientSide())
        {
            Item item = pPlayer.getUseItem().getItem();
            if (item instanceof Tome)
            {
                // NetworkHooks.openScreen((ServerPlayer) pPlayer, TomeMenu::new);
            }
        }
        else
        {
            throw new IllegalStateException("Container Provider Missing.");
        }

        return InteractionResultHolder.sidedSuccess(this, pLevel.isClientSide());
    }
}
