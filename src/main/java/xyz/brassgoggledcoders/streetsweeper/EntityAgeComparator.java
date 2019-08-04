package xyz.brassgoggledcoders.streetsweeper;

import java.util.Comparator;

import net.minecraft.entity.Entity;

public class EntityAgeComparator implements Comparator<Entity> {
    @Override
    public int compare(Entity first, Entity second) {
        return Integer.compare(second.ticksExisted, first.ticksExisted);
    }
}
