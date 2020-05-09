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
        // Never remove players
        if (entity instanceof PlayerEntity) {
            return false;
        }
        // Check properties first
        else if (entity.isInvulnerable() || entity.isImmuneToFire()) {
            return StreetSweeper.instance.sweeperConfig.removalOptions.removeInvulnerable.get();
        } else if (entity.hasCustomName()) {
            return StreetSweeper.instance.sweeperConfig.removalOptions.removeNamed.get();
        } else if (entity instanceof TameableEntity && ((TameableEntity) entity).isTamed()) {
            return StreetSweeper.instance.sweeperConfig.removalOptions.removePets.get();
        }
        // Then check if is a specific type
        else if (entity instanceof VillagerEntity) {
            return StreetSweeper.instance.sweeperConfig.removalOptions.removeVillagers.get();
        } else if (!entity.isNonBoss()) {
            return StreetSweeper.instance.sweeperConfig.removalOptions.removeBosses.get();
        } else if (entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity) {
            return StreetSweeper.instance.sweeperConfig.removalOptions.removeItems.get();
        }
        // Then check if it is a generic type
        else if (entity instanceof LivingEntity) {
            if (entity instanceof IMob) {
                return StreetSweeper.instance.sweeperConfig.removalOptions.removeMonsters.get();
            } else if (entity instanceof AnimalEntity) {
                return StreetSweeper.instance.sweeperConfig.removalOptions.removeAnimals.get();
            }
            return StreetSweeper.instance.sweeperConfig.removalOptions.removeLiving.get();
        } else {
            return true;
        }
    }

}
