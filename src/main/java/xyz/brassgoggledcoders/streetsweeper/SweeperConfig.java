package xyz.brassgoggledcoders.streetsweeper;


import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class SweeperConfig {
    public final IntValue entityLimit;
    public final BooleanValue automatic;
    public final BooleanValue anyoneMayExecute;
    public final BooleanValue blockNewEntities;

    public final RemovalOptions removalOptions;
    public final ForgeConfigSpec spec;

    public SweeperConfig(ForgeConfigSpec.Builder configBuilder) {
        entityLimit = configBuilder.comment("The cap on entities in the world.",
                "When StreetSweeper runs, it will mercilessly remove entities that exceed this cap, beginning with the oldest",
                "This is per-dimension.")
                .defineInRange("entityLimit", 1000, 0, Integer.MAX_VALUE);
        automatic = configBuilder.comment("Whether or not to try to execute StreetSweeper whenever a world is loaded or saved.",
                "Manual execution is always possible through '/streetsweeper'")
                .define("automatic", true);
        anyoneMayExecute= configBuilder.comment( "When false, only (>Level 2) OPs may execute the sweeper")
                .define("anyoneMayExecute", false);
        blockNewEntities = configBuilder.comment("If this is enabled, StreetSweeper will block new entities from spawning above the cap.",
                "(This is disabled by default because it doesn't respect the standard behaviour of killing oldest entities first)")
                .define("blockNewEntities", false);
        removalOptions = new RemovalOptions(configBuilder.comment("Various options for filtering what entities may be removed by StreetSweeper. " +
                "Anything matching one of these options will *never* be removed.")
                .push("removalOptions"));
        spec = configBuilder.build();
    }

    public static class RemovalOptions {
        public final BooleanValue keepPets, keepBosses, keepInvulnerables, keepNamed;

        public RemovalOptions(ForgeConfigSpec.Builder configBuilder) {
            keepBosses = configBuilder.define("keepBosses", true);
            keepInvulnerables = configBuilder.comment("ex: Tinker's Tools are  considered invulnerable")
                    .define("keepInvulerables", true);
            keepPets = configBuilder.define("keepTamedAnimals", true);
            keepNamed = configBuilder.define("keepNamed", true);
        }
    }
}
