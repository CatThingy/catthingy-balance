package dev.catthingy.mixin;

import dev.catthingy.CustomItemTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.ArrowInfiniteEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Enchantments.class)
public class InfinityMixin {
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;)Lnet/minecraft/world/item/enchantment/ArrowInfiniteEnchantment;"))
    private static ArrowInfiniteEnchantment infinityEnchant(Enchantment.EnchantmentDefinition enchantmentDefinition) {
        return new ArrowInfiniteEnchantment(Enchantment.definition(CustomItemTags.BOW_CROSSBOW_ENCHANTABLE, ItemTags.BOW_ENCHANTABLE, 1, 1, Enchantment.constantCost(20), Enchantment.constantCost(50), 8, EquipmentSlot.MAINHAND));
    }
}