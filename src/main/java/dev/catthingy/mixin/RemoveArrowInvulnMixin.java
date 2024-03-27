package dev.catthingy.mixin;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class RemoveArrowInvulnMixin {
    @Redirect(method = "hurt", at=@At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V", ordinal = 1))
    void invulnHurt(LivingEntity instance, DamageSource damageSource, float damageAmount) {
        if (instance.invulnerableTime > 10.0 && damageSource.is(DamageTypeTags.BYPASSES_COOLDOWN)) {
            ((LivingEntityAccessor)instance).callActuallyHurt(damageSource, damageAmount * 0.5F);
        }
    }
}
@Mixin(LivingEntity.class)
interface LivingEntityAccessor {
    @Invoker
    void callActuallyHurt(DamageSource damageSource, float damageAmount);
}