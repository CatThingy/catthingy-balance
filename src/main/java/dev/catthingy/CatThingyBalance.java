package dev.catthingy;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatThingyBalance implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("catthingy-balance");
    public static final Enchantment MAGIC_PROT = new MagicProtectionEnchantment(Enchantment.definition(ItemTags.ARMOR_ENCHANTABLE, 7, 4, Enchantment.dynamicCost(1, 11), Enchantment.dynamicCost(12, 11), 1, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET));

    @Override
    public void onInitialize() {
        CustomItemTags.init();
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution
        Registry.register(BuiltInRegistries.ENCHANTMENT, new ResourceLocation("catthingy", "magic_protection"), MAGIC_PROT);
    }
}
