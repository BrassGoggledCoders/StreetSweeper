package xyz.brassgoggledcoders.streetsweeper;

import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = StreetSweeper.MODID)
public class SSEventHandler {
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
                ((ServerWorld) event.getWorld()).getEntities().count() >= StreetSweeper.instance.sweeperConfig.entityLimit.get()) {
            event.setCanceled(true);
        }
    }
}
