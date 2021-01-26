package xyz.brassgoggledcoders.streetsweeper;

import net.minecraft.data.*;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.tags.EntityTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeItemTagsProvider;

public class SSTagsProvider {
    public static class SSItemTagsProvider extends ForgeItemTagsProvider {
        public SSItemTagsProvider(DataGenerator gen, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
            super(gen, blockTagProvider, existingFileHelper);
        }
        @Override
        public void registerTags() {
            this.getOrCreateBuilder(StreetSweeper.DONT_SWEEP);
        }
    }
    public static class SSEntityTagsProvider extends EntityTypeTagsProvider  {
        public SSEntityTagsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, StreetSweeper.MODID, existingFileHelper);
        }

        @Override
        protected void registerTags() {
            this.getOrCreateBuilder(StreetSweeper.SWEEPABLES).add(EntityType.ITEM, EntityType.EXPERIENCE_ORB);
        }
    }
}
