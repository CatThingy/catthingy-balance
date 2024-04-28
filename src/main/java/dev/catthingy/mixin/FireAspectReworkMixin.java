package dev.catthingy.mixin;

import dev.catthingy.FireAspectEnchantment;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Enchantments.class)
public class FireAspectReworkMixin {
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;)Lnet/minecraft/world/item/enchantment/Enchantment;", ordinal = 3))
    private static Enchantment fireAspectEnchant(Enchantment.EnchantmentDefinition enchantmentDefinition) {
        return new FireAspectEnchantment(Enchantment.definition(ItemTags.FIRE_ASPECT_ENCHANTABLE, 5, 5, Enchantment.dynamicCost(5, 8), Enchantment.dynamicCost(25, 8), 2, EquipmentSlot.MAINHAND));
    }
}
