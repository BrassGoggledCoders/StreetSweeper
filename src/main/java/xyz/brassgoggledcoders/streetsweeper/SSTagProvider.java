package xyz.brassgoggledcoders.streetsweeper;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.EntityTypeTagsProvider;
import net.minecraft.data.IDataProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SSTagProvider extends EntityTypeTagsProvider {
    public SSTagProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, StreetSweeper.MODID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        this.getOrCreateBuilder(StreetSweeper.SWEEPABLES).add(EntityType.ITEM, EntityType.EXPERIENCE_ORB);
    }
}
