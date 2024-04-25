package dev.catthingy.mixin;

import dev.catthingy.CustomItemTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Enchantments.class)
public class PunchMixin {
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;)Lnet/minecraft/world/item/enchantment/Enchantment;", ordinal=7))
    private static Enchantment punchEnchant(Enchantment.EnchantmentDefinition enchantmentDefinition) {
        return new Enchantment(Enchantment.definition(CustomItemTags.BOW_CROSSBOW_ENCHANTABLE, ItemTags.BOW_ENCHANTABLE, 2, 2, Enchantment.dynamicCost(12, 20), Enchantment.dynamicCost(37, 20), 4, EquipmentSlot.MAINHAND));
    }
}
