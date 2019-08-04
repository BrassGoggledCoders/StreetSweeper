package xyz.brassgoggledcoders.streetsweeper;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = StreetSweeper.MODID)
public class EventHandlerSS {
    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        if(SweeperConfig.automatic) {
            StreetSweeper.tryExecute(event.getWorld());
        }
    }

    @SubscribeEvent
    public static void onWorldSave(WorldEvent.Save event) {
        if(SweeperConfig.automatic) {
            StreetSweeper.tryExecute(event.getWorld());
        }
    }
}
