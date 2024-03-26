package dev.catthingy.mixin;

import dev.catthingy.CatThingyBalance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.world.entity.projectile.FireworkRocketEntity")
public class ElytraFireworkNerfMixin {
    @Redirect(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", ordinal = 0))
    private void setVelocity(LivingEntity target, Vec3 newVelocity) {
        Vec3 lookDir = target.getLookAngle();
        double d = 0.5;
        double e = 0.1;
        Vec3 vel = target.getDeltaMovement();
        target
                .setDeltaMovement(
                        vel.add(
                                lookDir.x * e + (lookDir.x * d - vel.x) * 0.5, lookDir.y * e + (lookDir.y * d - vel.y) * 0.5, lookDir.z * e + (lookDir.z * d - vel.z) * 0.5
                        )
                );

        CatThingyBalance.LOGGER.info(String.valueOf(target.getDeltaMovement().length() * 20.0));
    }
}

