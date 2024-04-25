package dev.catthingy;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;

public class MagicProtectionEnchantment extends Enchantment {
    public MagicProtectionEnchantment(EnchantmentDefinition enchantmentDefinition) {
        super(enchantmentDefinition);
    }

    @Override
    public boolean checkCompatibility(Enchantment other) {
        if (other instanceof ProtectionEnchantment protectionEnchantment) {
            return protectionEnchantment.type == ProtectionEnchantment.Type.FALL;
        } else {
            return super.checkCompatibility(other);
        }
    }

    public int getDamageProtection(int level, DamageSource source) {
        if (source.is(CustomDamageTypes.SMITE) || source.is(DamageTypes.MAGIC) || source.is(DamageTypes.INDIRECT_MAGIC) || source.is(DamageTypes.DRAGON_BREATH)) {
            return 2 * level;
        } else {
            return 0;
        }
    }
}