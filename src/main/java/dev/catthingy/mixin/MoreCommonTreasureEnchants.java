package dev.catthingy.mixin;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import net.minecraft.world.item.enchantment.MendingEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Enchantments.class)
public class MoreCommonTreasureEnchants {
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;)Lnet/minecraft/world/item/enchantment/MendingEnchantment;"))
    private static MendingEnchantment mendingEnchantment(Enchantment.EnchantmentDefinition enchantmentDefinition) {
        return new MendingEnchantment(Enchantment.definition(ItemTags.DURABILITY_ENCHANTABLE, 5, 1, Enchantment.dynamicCost(25, 25), Enchantment.dynamicCost(75, 25), 4, EquipmentSlot.values()));
    }

    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;)Lnet/minecraft/world/item/enchantment/FrostWalkerEnchantment;"))
    private static FrostWalkerEnchantment frostWalkerEnchantmentEnchantment(Enchantment.EnchantmentDefinition enchantmentDefinition) {
        return new FrostWalkerEnchantment(Enchantment.definition(ItemTags.FOOT_ARMOR_ENCHANTABLE, 5, 2, Enchantment.dynamicCost(10, 10), Enchantment.dynamicCost(25, 10), 4, EquipmentSlot.FEET));
    }
}
