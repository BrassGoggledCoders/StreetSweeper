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
        removalOptions = new RemovalOptions(configBuilder.comment("Various options for filtering what entities may be removed by StreetSweeper")
                .push("removalOptions"));
        spec = configBuilder.build();
    }

    public static class RemovalOptions {
        public final BooleanValue removeLiving;
        public final BooleanValue removeBosses;
        public final BooleanValue removeItems;
        public final BooleanValue removeInvulnerable;
        public final BooleanValue removeNamed;
        public final BooleanValue removeMonsters;
        public final BooleanValue removeAnimals;
        public final BooleanValue removePets;
        public final BooleanValue removeVillagers;

        public RemovalOptions(ForgeConfigSpec.Builder configBuilder) {
            removeLiving = configBuilder.comment("Remove anything that's considered living BUT that is not covered by the monster or animal configs. (To allow maximum flexibility)")
                    .define("removeLiving", false);
            removeBosses = configBuilder.define("removeBosses", false);
            removeItems = configBuilder.comment("This includes XP orbs")
                    .define("removeItems", true);
            removeInvulnerable = configBuilder.comment("Tinker's Tools are  considered invulnerable")
                    .define("removeInvulnerable", false);
            removeNamed = configBuilder.define("removeNamed", false);
            removeMonsters = configBuilder.comment("Remove entities considered hostile")
                    .define("removeMonsters", true);
            removeAnimals = configBuilder.define("removeAnimals", false);
            removePets = configBuilder.define("removePets", false);
            removeVillagers = configBuilder.comment("Tamed Wolves/Cats")
                    .define("removeVillagers", false);
        }
    }
}
