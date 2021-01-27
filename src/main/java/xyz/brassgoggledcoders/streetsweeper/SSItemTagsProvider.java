package xyz.brassgoggledcoders.streetsweeper;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeItemTagsProvider;

public class SSItemTagsProvider extends ForgeItemTagsProvider {
    public SSItemTagsProvider(DataGenerator gen, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(gen, blockTagProvider, existingFileHelper);
    }
    @Override
    public void registerTags() {
        this.getOrCreateBuilder(StreetSweeper.DONT_SWEEP);
    }
}
