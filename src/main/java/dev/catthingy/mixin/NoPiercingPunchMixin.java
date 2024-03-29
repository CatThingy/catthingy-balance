package dev.catthingy.mixin;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class NoPiercingPunchMixin {
    @Inject(method = "isDamageSourceBlocked", at = @At(value = "RETURN", ordinal = 1))
    void test(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        Entity entity = damageSource.getDirectEntity();
        LivingEntity self = (LivingEntity) ((Object) this);

        if (entity instanceof AbstractArrow abstractArrow && abstractArrow.getPierceLevel() > 0) {
            if (!damageSource.is(DamageTypeTags.BYPASSES_SHIELD) && self.isBlocking()) {
                Vec3 vec3 = damageSource.getSourcePosition();
                if (vec3 != null) {
                    Vec3 vec32 = ((EntityAccessor) self).callCalculateViewVector(0.0F, self.getYHeadRot());
                    Vec3 vec33 = vec3.vectorTo(self.position());
                    vec33 = new Vec3(vec33.x, 0.0, vec33.z).normalize();
                    if (vec33.dot(vec32) < 0.0) {
                        abstractArrow.setKnockback(0);
                        abstractArrow.setPierceLevel((byte) (abstractArrow.getPierceLevel() - 1));
                    }
                }
            }
        }
    }
}