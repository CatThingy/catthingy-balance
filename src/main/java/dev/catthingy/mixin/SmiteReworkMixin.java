package dev.catthingy.mixin;

import dev.catthingy.SmiteEnchantment;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(Enchantments.class)
public class SmiteReworkMixin {
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target="(Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;Ljava/util/Optional;)Lnet/minecraft/world/item/enchantment/DamageEnchantment;", ordinal = 1))
    private static DamageEnchantment smiteEnchant(Enchantment.EnchantmentDefinition enchantmentDefinition, Optional _optional) {
        return new SmiteEnchantment(
                Enchantment.definition(
                        ItemTags.WEAPON_ENCHANTABLE, ItemTags.SWORD_ENCHANTABLE, 5, 5, Enchantment.dynamicCost(5, 8), Enchantment.dynamicCost(25, 8), 2, EquipmentSlot.MAINHAND
                )
        );
    }
}
