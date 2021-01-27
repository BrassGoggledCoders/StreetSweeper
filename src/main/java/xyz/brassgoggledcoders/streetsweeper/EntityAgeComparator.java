package xyz.brassgoggledcoders.streetsweeper;

import net.minecraft.entity.Entity;

import java.util.Comparator;

public class EntityAgeComparator implements Comparator<Entity> {
    @Override
    public int compare(Entity first, Entity second) {
        return Integer.compare(second.ticksExisted, first.ticksExisted);
    }
}
