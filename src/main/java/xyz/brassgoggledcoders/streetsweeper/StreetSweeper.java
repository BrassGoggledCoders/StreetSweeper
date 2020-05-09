package xyz.brassgoggledcoders.streetsweeper;

import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLClientLaunchProvider;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.fml.network.NetworkRegistry;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mod(StreetSweeper.MODID)
public class StreetSweeper {

    public static final String MODID = "streetsweeper";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static StreetSweeper instance;

    public final SweepPredicate sweepPredicate = new SweepPredicate();
    public final EntityAgeComparator entityAgeComparator = new EntityAgeComparator();
    public SweeperConfig sweeperConfig;


    public StreetSweeper() {
        instance = this;
        MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
        sweeperConfig = new SweeperConfig(new ForgeConfigSpec.Builder());
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, sweeperConfig.spec);
        NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "main"), () -> "1", version -> true, version -> true);
        ModList.get().getModContainerById(MODID)
                .ifPresent(mod -> mod.registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
                        ()-> Pair.of(()-> FMLNetworkConstants.IGNORESERVERONLY, (in, net) -> true)));
    }


    public void serverStarting(FMLServerStartingEvent event) {
        event.getCommandDispatcher().register(
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
                List<Entity> forRemoval = new ArrayList<>(serverWorld.entitiesById.values())
                        .stream()
                        .filter(sweepPredicate)
                        .sorted(entityAgeComparator)
                        .limit(entityAmount - sweeperConfig.entityLimit.get())
                        .collect(Collectors.toList());
                for (Entity entity : forRemoval) {
                    serverWorld.removeEntity(entity);
                }
                serverWorld.getServer().getPlayerList().sendMessage(
                        new StringTextComponent("Server swept! Removed " + forRemoval.size() + " entities"));
                return forRemoval.size();
            } else {
                LOGGER.info("StreetSweeper executed. Nothing to remove.");
                return 0;
            }
        }
        return 0;
    }
}
