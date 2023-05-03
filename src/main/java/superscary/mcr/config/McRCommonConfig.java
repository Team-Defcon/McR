package superscary.mcr.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;
import org.apache.commons.lang3.tuple.Pair;

public class McRCommonConfig
{

    public static final ForgeConfigSpec CONFIG;
    public static final McRCommonConfig INSTANCE;

    static
    {
        final Pair<McRCommonConfig, ForgeConfigSpec> pair = new Builder().configure(McRCommonConfig::new);
        CONFIG = pair.getRight();
        INSTANCE = pair.getLeft();
    }

    public static McRCommonConfig getInstance()
    {
        return INSTANCE;
    }

    /**
     * Coal Generator
     */
    public final IntValue coalGeneratorFECapacity;
    public final IntValue coalGeneratorGenPerTick;
    public final IntValue coalGeneratorMaxOutput;

    private McRCommonConfig (Builder builder)
    {
        builder.comment("Coal Generator Settings").push("coalGeneratorBlockSettings");
        coalGeneratorFECapacity = builder.comment("The max storage of the Coal Generator").defineInRange("coalGeneratorCapacity", 10_000, 10_000, 1_000_000);
        coalGeneratorGenPerTick = builder.comment("The FE generation for the Coal Generator (FE per tick)").defineInRange("coalGeneratorGenRate", 12, 1, 100_000);
        coalGeneratorMaxOutput = builder.comment("The maximum output of FE per tick").defineInRange("coalGeneratorOutput", 50, 1, 1_000);
        builder.pop();
    }

}
