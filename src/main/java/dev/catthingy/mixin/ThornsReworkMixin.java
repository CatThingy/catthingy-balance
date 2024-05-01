package dev.catthingy.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ThornsEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ThornsEnchantment.class)
public class ThornsReworkMixin {
    /**
     * @author me (:
     * @reason reworking what thorns does
     */
    @Overwrite
    public static boolean shouldHit(int level, RandomSource random) {
        return true;
    }

    /**
     * @author me (:
     * @reason reworking what thorns does
     */
    @Overwrite
    public static int getDamage(int level, RandomSource random) {
        return level > 10 ? 4 * (level - 10) : 1;
    }

    @Redirect(method = "doPostHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    boolean properDamage(Entity instance, DamageSource source, float amount, @Local LivingEntity target) {
        amount /= 4.0F;
        return instance.hurt(target.damageSources().thorns(target), amount);
    }

    @Redirect(method = "doPostHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"))
    void noExtraDamage(ItemStack instance, int i, LivingEntity livingEntity, EquipmentSlot equipmentSlot) {
        // This method intentionally left blank.
    }
}
