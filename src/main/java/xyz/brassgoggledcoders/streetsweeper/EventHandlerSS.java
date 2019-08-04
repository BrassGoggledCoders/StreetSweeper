package xyz.brassgoggledcoders.streetsweeper;

import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
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

    @SubscribeEvent
    public static void onSpawned(EntityJoinWorldEvent event) {
        if(SweeperConfig.blockNewEntities && event.getWorld().loadedEntityList.size() >= SweeperConfig.entityLimit) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
        if(SweeperConfig.blockNewEntities
                && event.getPlayer().getEntityWorld().loadedEntityList.size() >= SweeperConfig.entityLimit) {
            event.getPlayer().inventory.addItemStackToInventory(event.getEntityItem().getItem());
            event.getPlayer().sendStatusMessage(new TextComponentString(
                    "The amount of entities in the world is currently above the cap. Your items have been returned to prevent them being deleted"),
                    true);
            event.setCanceled(true);
        }
    }
}
