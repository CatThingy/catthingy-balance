package dev.catthingy.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
interface LivingEntityAccessor {
    @Invoker
    void callActuallyHurt(DamageSource damageSource, float damageAmount);
}
