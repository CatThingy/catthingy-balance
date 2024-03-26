package dev.catthingy.mixin;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(targets = "net.minecraft.world.item.enchantment.ProtectionEnchantment")
public class ProtectionNerfMixin {

    /**
     * @author me (:
     * @reason can't mixin into branches
     */
    @Overwrite
    public int getDamageProtection(int level, DamageSource source) {
        ProtectionEnchantment self = (ProtectionEnchantment) (Object) this;
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return 0;
        } else if (self.type == ProtectionEnchantment.Type.FIRE && source.is(DamageTypeTags.IS_FIRE)) {
            return level * 2;
        } else if (self.type == ProtectionEnchantment.Type.FALL && source.is(DamageTypeTags.IS_FALL)) {
            return level * 3;
        } else if (self.type == ProtectionEnchantment.Type.EXPLOSION && source.is(DamageTypeTags.IS_EXPLOSION)) {
            return level * 2;
        } else if (self.type == ProtectionEnchantment.Type.PROJECTILE && source.is(DamageTypeTags.IS_PROJECTILE)) {
            return level * 2;
        } else if (self.type == ProtectionEnchantment.Type.ALL
                && !(source.is(DamageTypeTags.IS_PROJECTILE)
                    || source.is(DamageTypeTags.IS_EXPLOSION)
                    || source.is(DamageTypeTags.IS_FIRE)
                    || source.is(DamageTypeTags.IS_LIGHTNING)
                    || source.is(DamageTypeTags.BYPASSES_ARMOR))
        ) {
            return level * 2;
        } else {
            return 0;
        }
    }
}
