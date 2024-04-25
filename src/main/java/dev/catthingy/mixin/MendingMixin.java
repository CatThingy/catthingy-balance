package dev.catthingy.mixin;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.MendingEnchantment;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MendingEnchantment.class)
public class MendingMixin extends Enchantment {
    public MendingMixin(EnchantmentDefinition enchantmentDefinition) {
        super(enchantmentDefinition);
    }

    @Override
    public boolean isTradeable() {
        return false;
    }
}
