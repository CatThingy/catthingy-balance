package dev.catthingy.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ProjectileWeaponItem.class)
public class MultishotMixin {
    @Redirect(method = "shoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ProjectileWeaponItem;shootProjectile(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/projectile/Projectile;IFFFLnet/minecraft/world/entity/LivingEntity;)V"))
    void adjust_spread(ProjectileWeaponItem instance, LivingEntity shooter, Projectile projectile, int i, float velocity, float inaccuracy, float spread, LivingEntity target) {
        if (i != 0) {
            projectile.setOwner(null);
        }
        ((ProjectileWeaponItemAccessor) instance).callShootProjectile(shooter, projectile, i, velocity, (i == 0 ? inaccuracy : inaccuracy + spread), 0.0f, target);
    }
}

@Mixin(ProjectileWeaponItem.class)
interface ProjectileWeaponItemAccessor {
    @Invoker
    abstract void callShootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target
    );
}

