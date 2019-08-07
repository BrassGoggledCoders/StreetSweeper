package xyz.brassgoggledcoders.streetsweeper;

import net.minecraftforge.common.config.Config;

@Config(modid = StreetSweeper.MODID)
public class SweeperConfig {
    @Config.Comment(value = { "The cap on entities in the world.",
            "When StreetSweeper runs, it will mercilessly remove entities that exceed this cap, beginning with the oldest",
            "This is per-dimension." })
    public static int entityLimit = 1000;
    @Config.Comment(value = { "Whether or not to try to execute StreetSweeper whenever a world is loaded or saved.",
            "Manual execution is always possible through '/streetsweeper'" })
    public static boolean automatic = true;
    @Config.Comment(value = "When false, only (>Level 2) OPs may execute the sweeper")
    public static boolean anyoneMayExecute = false;
    @Config.Comment(value = { "If this is enabled, StreetSweeper will block new entities from spawning above the cap.",
            "(This is disabled by default because it doesn't respect the standard behaviour of killing oldest entities first)" })
    public static boolean blockNewEntities = false;
    @Config.Comment(value = { "Various options for filtering what entities may be removed by StreetSweeper" })
    public static RemovalOptions removalOptions = new RemovalOptions();

    public static class RemovalOptions {
        @Config.Comment(value = {
                "Remove anything that's considered living BUT that is not covered by the monster or animal configs. (To allow maximum flexibility)" })
        public boolean removeLiving = false;
        public boolean removeBosses = false;
        @Config.Comment(value = { "This includes XP orbs" })
        public boolean removeItems = true;
        @Config.Comment(value = { "Tinker's Tools are  considered invulerable" })
        public boolean removeInvulnerable = false;
        public boolean removeNamed = false;
        @Config.Comment(value = { "Remove entities considered hostile" })
        public boolean removeMonsters = true;
        public boolean removeAnimals = false;
        @Config.Comment(value = { "Tamed Wolves/Cats" })
        public boolean removePets = false;
        public boolean removeVillagers = false;
    }
}
