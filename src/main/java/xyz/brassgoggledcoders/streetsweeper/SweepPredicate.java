package xyz.brassgoggledcoders.streetsweeper;

import java.util.function.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;

public class SweepPredicate implements Predicate<Entity> {
    // Return true if the entity should be removable
    @Override
    public boolean test(Entity entity) {
        // Never remove players
        if(entity instanceof EntityPlayer) {
            return false;
        }
        // Check properties first
        else if(entity.getIsInvulnerable() || entity.isImmuneToFire()) {
            return SweeperConfig.removalOptions.removeInvulnerable;
        }
        else if(!entity.getCustomNameTag().isEmpty()) {
            return SweeperConfig.removalOptions.removeNamed;
        }
        else if(entity instanceof EntityTameable && ((EntityTameable) entity).isTamed()) {
            return SweeperConfig.removalOptions.removePets;
        }
        // Then check if is a specific type
        else if(entity instanceof EntityVillager) {
            return SweeperConfig.removalOptions.removeVillagers;
        }
        else if(entity instanceof EntityWither || entity instanceof EntityDragon) {
            return SweeperConfig.removalOptions.removeBosses;
        }
        else if(entity instanceof EntityItem || entity instanceof EntityXPOrb) {
            return SweeperConfig.removalOptions.removeItems;
        }
        // Then check if it is a generic type
        else if(entity instanceof EntityLivingBase) {
            if(entity instanceof IMob) {
                return SweeperConfig.removalOptions.removeMonsters;
            }
            else if(entity instanceof IAnimals) {
                return SweeperConfig.removalOptions.removeAnimals;
            }
            return SweeperConfig.removalOptions.removeLiving;
        }
        else {
            return true;
        }
    }

}
