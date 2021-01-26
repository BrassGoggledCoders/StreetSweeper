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
import net.minecraft.item.ItemStack;

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
        if(entity.getType().getTags().contains(StreetSweeper.SWEEPABLES.getName())) {
            if (removalOptions.keepInvulnerables.get() && entity.isInvulnerable()) {
                return false;
            }
            if (removalOptions.keepBosses.get() && !entity.isNonBoss()) {
                return false;
            }
            if (removalOptions.keepNamed.get() && entity.hasCustomName()) {
                return false;
            }
            if (removalOptions.keepPets.get() && entity instanceof TameableEntity && ((TameableEntity) entity).isTamed()) {
                return false;
            }
            if (entity instanceof ItemEntity) {
                ItemStack item = ((ItemEntity) entity).getItem();
                if (removalOptions.keepNBTItems.get() && item.hasTag()) {
                    return false;
                } else {
                    return !item.getItem().getTags().contains(StreetSweeper.DONT_SWEEP.getName());
                }
            }
            return true;
        }
        return false;
    }
}