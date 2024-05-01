package dev.catthingy.mixin;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ThornsEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Enchantments.class)
public class ThornsLevelAdjustmentMixin {
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;)Lnet/minecraft/world/item/enchantment/ThornsEnchantment;"))
    private static ThornsEnchantment thornsEnchant(Enchantment.EnchantmentDefinition enchantmentDefinition) {
        return new ThornsEnchantment(
                Enchantment.definition(
                        ItemTags.ARMOR_ENCHANTABLE, ItemTags.CHEST_ARMOR_ENCHANTABLE, 1, 1, Enchantment.dynamicCost(10, 20), Enchantment.dynamicCost(60, 20), 8, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
                )
        );
    }
}
