package xyz.brassgoggledcoders.streetsweeper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;

import java.util.Comparator;

public class PreferItemsComparator implements Comparator<Entity> {
    @Override
    public int compare(Entity first, Entity second) {
        if(first instanceof ItemEntity) {
            return 1;
        }
        else if(second instanceof ItemEntity) {
            return -1;
        }
        return 0;
    }
}
