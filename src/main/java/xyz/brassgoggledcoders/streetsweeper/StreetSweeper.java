package xyz.brassgoggledcoders.streetsweeper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = StreetSweeper.MODID, name = "StreetSweeper", version = "@VERSION@", serverSideOnly = true)
public class StreetSweeper {

    public static final String MODID = "streetsweeper";
    @Instance(MODID)
    public static StreetSweeper instance;
    public Logger log;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        log = event.getModLog();
    }

    @EventHandler
    public void serverStarted(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandStreetSweeper());
    }

    public static void tryExecute(World world) {
        if(!world.isRemote) {
            if(world.loadedEntityList.size() > SweeperConfig.entityLimit) {
                List<Entity> forRemoval = new ArrayList<>(world.loadedEntityList).stream()
                        .sorted(new EntityAgeComparator()).filter(new SweepPredicate())
                        .limit((world.loadedEntityList.size() - SweeperConfig.entityLimit))
                        .collect(Collectors.toList());
                for(Entity entity : forRemoval) {
                    world.removeEntity(entity);
                }
                FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessage(
                        new TextComponentString("Server swept! Removed " + forRemoval.size() + " entities"));
            }
            else {
                StreetSweeper.instance.log.info("StreetSweeper executed. Nothing to remove.");
            }
        }
    }
}
