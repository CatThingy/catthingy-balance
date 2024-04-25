package dev.catthingy;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CustomItemTags {
    public static final TagKey<Item> BOW_CROSSBOW_ENCHANTABLE = TagKey.create(Registries.ITEM, new ResourceLocation("catthingy", "bow_crossbow_enchantable"));

    public static void init() {

    }
}
