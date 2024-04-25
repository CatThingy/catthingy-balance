package dev.catthingy.mixin;

import dev.catthingy.ChannelingEnchantment;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Enchantments.class)
public class ChannelingMixin {
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;)Lnet/minecraft/world/item/enchantment/Enchantment;", ordinal = 11))
    private static Enchantment channelingEnchant(Enchantment.EnchantmentDefinition enchantmentDefinition) {
        return new ChannelingEnchantment(Enchantment.definition(ItemTags.TRIDENT_ENCHANTABLE, 1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 8, EquipmentSlot.MAINHAND));
    }
}