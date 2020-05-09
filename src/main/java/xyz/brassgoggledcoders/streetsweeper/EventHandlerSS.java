package xyz.brassgoggledcoders.streetsweeper;

import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = StreetSweeper.MODID)
public class EventHandlerSS {
    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        if (StreetSweeper.instance.sweeperConfig.automatic.get()) {
            StreetSweeper.instance.tryExecute(event.getWorld());
        }
    }

    @SubscribeEvent
    public static void onWorldSave(WorldEvent.Save event) {
        if (StreetSweeper.instance.sweeperConfig.automatic.get()) {
            StreetSweeper.instance.tryExecute(event.getWorld());
        }
    }

    @SubscribeEvent
    public static void onSpawned(EntityJoinWorldEvent event) {
        if (event.getWorld() instanceof ServerWorld && StreetSweeper.instance.sweeperConfig.blockNewEntities.get() &&
                ((ServerWorld) event.getWorld()).entitiesById.size() >= StreetSweeper.instance.sweeperConfig.entityLimit.get()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
        World world = event.getPlayer().world;
        if (world instanceof ServerWorld && StreetSweeper.instance.sweeperConfig.blockNewEntities.get() &&
                ((ServerWorld) world).entitiesById.size() >= StreetSweeper.instance.sweeperConfig.entityLimit.get()) {
            event.getPlayer().inventory.addItemStackToInventory(event.getEntityItem().getItem());
            event.getPlayer().sendStatusMessage(new StringTextComponent("The amount of entities in the world is " +
                            "currently above the cap. Your items have been returned to prevent them being deleted"),
                    true);
            event.setCanceled(true);
        }
    }
}
