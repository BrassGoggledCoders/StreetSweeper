package xyz.brassgoggledcoders.streetsweeper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Predicate;

public class SweepPredicate implements Predicate<Entity> {
    // Return true if the entity should be removable
    @Override
    public boolean test(Entity entity) {
        final SweeperConfig.RemovalOptions removalOptions = StreetSweeper.instance.sweeperConfig.removalOptions;
        // Never remove players
        if (entity instanceof PlayerEntity) {
            return false;
        }
        else if (removalOptions.keepInvulnerables.get() && entity.isInvulnerable()) {
            return false;
        }
        else if (removalOptions.keepBosses.get() && entity.hasCustomName()) {
            return false;
        }
        else if (removalOptions.keepPets.get() && entity instanceof TameableEntity && ((TameableEntity) entity).isTamed()) {
            return false;
        }
        else {
            return entity.getType().getTags().contains(StreetSweeper.SWEEPABLES.getName());
        }
    }
}