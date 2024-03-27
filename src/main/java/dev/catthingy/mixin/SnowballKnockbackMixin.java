package dev.catthingy.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Snowball;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Snowball.class)
public class SnowballKnockbackMixin {
    @Redirect(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    boolean hurt(Entity instance, DamageSource source, float amount) {
        if (instance.hurt(source, amount + Float.MIN_VALUE)) {
            if (amount == 0.0F && instance instanceof LivingEntity entity) {
                Entity self = (Entity) ((Object) this);

                double d = self.getX() - entity.getX();
                double e = self.getZ() - entity.getZ();
                while (d * d + e * e < 1.0E-4) {
                    d = (Math.random() - Math.random()) * 0.01;
                    e = (Math.random() - Math.random()) * 0.01;
                }

                entity.knockback(0.4F, d, e);
                entity.indicateDamage(d, e);
            }
            return true;
        }
        return false;
    }
}
