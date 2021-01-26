package xyz.brassgoggledcoders.streetsweeper;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.fml.network.NetworkRegistry;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Mod(StreetSweeper.MODID)
public class StreetSweeper {

    public static final String MODID = "streetsweeper";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static StreetSweeper instance;
    public static final ITag.INamedTag<EntityType<?>> SWEEPABLES = EntityTypeTags.getTagById(MODID + ":sweepables");

    public final SweepPredicate sweepPredicate = new SweepPredicate();
    public final EntityAgeComparator entityAgeComparator = new EntityAgeComparator();
    public SweeperConfig sweeperConfig;

    public StreetSweeper() {
        instance = this;
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        MinecraftForge.EVENT_BUS.addListener(this::gatherData);
        sweeperConfig = new SweeperConfig(new ForgeConfigSpec.Builder());
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, sweeperConfig.spec);
        //Server side only
        ModList.get().getModContainerById(MODID)
                .ifPresent(mod -> mod.registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
                        () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (in, net) -> true)));
    }

    public void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("streetsweeper")
                    .requires(commandSource -> StreetSweeper.instance.sweeperConfig.anyoneMayExecute.get() ||
                            commandSource.hasPermissionLevel(2))
                    .executes(context -> StreetSweeper.instance.tryExecute(context.getSource().getWorld()))
        );
    }

    public int tryExecute(IWorld world) {
        if (!world.isRemote() && world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            int entityAmount = serverWorld.entitiesById.size();
            if (entityAmount > sweeperConfig.entityLimit.get()) {
                @SuppressWarnings("UnstableApiUsage")
                Multimap<String, Entity> forRemoval = new ArrayList<>(serverWorld.entitiesById.values())
                        .stream()
                        .filter(sweepPredicate)
                        .sorted(entityAgeComparator)
                        .limit(entityAmount - sweeperConfig.entityLimit.get())
                        .collect(Multimaps.toMultimap(
                                entity -> entity.chunkCoordX + " " + entity.chunkCoordZ,
                                entity -> entity,
                                () -> Multimaps.newSetMultimap(Maps.newHashMap(), Sets::newHashSet)
                        ));
                for (Map.Entry<String, Collection<Entity>> entities : forRemoval.asMap().entrySet()) {
                    entities.getValue().forEach(serverWorld::removeEntity);
                    LOGGER.info("Server swept {} entities at Chunk Pos {}", entities.getValue().size(),
                            entities.getKey());
                }
                //serverWorld.getServer().sendMessage(
                //        new StringTextComponent("Server swept! Removed " + forRemoval.size() + " entities"), UUID.fromString(MODID));
                return forRemoval.size();
            } else {
                LOGGER.info("StreetSweeper executed. Nothing to remove.");
                return 0;
            }
        }
        return 0;
    }

    public void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(new SSTagProvider(event.getGenerator(), event.getExistingFileHelper()));
    }
}
