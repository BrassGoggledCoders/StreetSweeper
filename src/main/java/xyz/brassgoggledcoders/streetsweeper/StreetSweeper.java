package xyz.brassgoggledcoders.streetsweeper;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = StreetSweeper.MODID, name = "StreetSweeper", version = "@VERSION@")
public class StreetSweeper {

    public static final String MODID = "streetsweeper";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }

    @EventHandler
    public void serverStarted(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandStreetSweeper());
    }

    public static void tryExecute(World world) {
        int size = world.loadedEntityList.size();
        if(!world.isRemote && size > SweeperConfig.entityLimit) {
            List<Entity> forRemoval = Lists.newArrayList(world.loadedEntityList).stream()
                    .sorted(new EntityAgeComparator()).filter(new SweepPredicate()).collect(Collectors.toList());
            // We can't rely on the size of the list directly because we're flagging
            // entities for removal next tick
            int entitiesRemoved = 0;
            for(Entity entity : forRemoval) {
                world.removeEntity(entity);
                entitiesRemoved++;
            }
            FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
                    .sendMessage(new TextComponentString("Server swept! Removed " + entitiesRemoved + " entities"));
        }
    }
}
