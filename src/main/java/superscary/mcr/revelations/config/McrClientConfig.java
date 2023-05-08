package superscary.mcr.revelations.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;
import org.apache.commons.lang3.tuple.Pair;

public class McrClientConfig
{

    public static final ForgeConfigSpec CONFIG;
    public static final McrClientConfig INSTANCE;

    static
    {
        final Pair<McrClientConfig, ForgeConfigSpec> pair = new Builder().configure(McrClientConfig::new);
        CONFIG = pair.getRight();
        INSTANCE = pair.getLeft();
    }

    public McrClientConfig getInstance ()
    {
        return INSTANCE;
    }

    public final BooleanValue displayItemOnInfuser;

    public McrClientConfig (Builder builder)
    {
        builder.comment("Rendering Settings").push("render");
        displayItemOnInfuser = builder.comment("Render items on Infuser").define("itemRenderInfuser", true);
        builder.pop();
    }

}
