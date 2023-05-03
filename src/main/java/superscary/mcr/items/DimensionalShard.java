package superscary.mcr.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class DimensionalShard extends Item
{

    public DimensionalShard (Properties pProperties)
    {
        super(pProperties);
    }

    @Override
    public Rarity getRarity (ItemStack pStack)
    {
        return Rarity.EPIC;
    }

    @Override
    public boolean isFoil (ItemStack pStack)
    {
        return true;
    }
}
