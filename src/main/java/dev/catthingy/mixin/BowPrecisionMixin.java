package dev.catthingy.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Predicate;

@Mixin(BowItem.class)
public class BowPrecisionMixin extends ProjectileWeaponItem {
    public BowPrecisionMixin(Properties properties) {
        super(properties);
    }

    @Redirect(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BowItem;shoot(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;Ljava/util/List;FFZLnet/minecraft/world/entity/LivingEntity;)V"))
    void bowPrecision(BowItem instance, ServerLevel serverLevel, LivingEntity player, InteractionHand interactionHand, ItemStack stack, List<ItemStack> projectiles, float velocity, float inaccuracy, boolean _crit, LivingEntity _null, @Local(ordinal=1) int chargeTime) {
        //    void bowPrecision(BowItem instance, Level level, LivingEntity player, InteractionHand interactionHand, ItemStack stack, List<ItemStack> projectiles, float velocity, float inaccuracy, boolean _crit, LivingEntity _null, @Local(ordinal=1) int chargeTime) {
        boolean crit = chargeTime >= 20 && chargeTime < 22;
        float velocityMultiplier = 0.999F;
        if (chargeTime >= 20 && chargeTime < 30) {
            inaccuracy = 0.0F;
            velocityMultiplier = 1.0F;
        }
        shoot(serverLevel, player, interactionHand, stack, projectiles, velocity * velocityMultiplier, inaccuracy, crit, _null);
    }

    @Override
    protected void shoot(
            ServerLevel level,
            LivingEntity livingEntity,
            InteractionHand interactionHand,
            ItemStack itemStack,
            List<ItemStack> list,
            float f,
            float g,
            boolean bl,
            @Nullable LivingEntity livingEntity2
    ) {
        float i = list.size() == 1 ? 0.0F : 20.0F / (float)(list.size() - 1);
        float j = (float)((list.size() - 1) % 2) * i / 2.0F;
        float k = 1.0F;

        for(int l = 0; l < list.size(); ++l) {
            ItemStack itemStack2 = list.get(l);
            if (!itemStack2.isEmpty()) {
                float m = j + k * (float)((l + 1) / 2) * i;
                k = -k;
                itemStack.hurtAndBreak(this.getDurabilityUse(itemStack2), livingEntity, LivingEntity.getSlotForHand(interactionHand));
                Projectile projectile = this.createProjectile(level, livingEntity, itemStack, itemStack2, bl);
                this.shootProjectile(livingEntity, projectile, l, f, g, m, livingEntity2);
                if (projectile instanceof AbstractArrow arrow && f == 3.0) {
                    arrow.setBaseDamage(arrow.getBaseDamage() * 1.25F);
                }
                level.addFreshEntity(projectile);
            }
        }
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    @Override
    public void shootProjectile(LivingEntity livingEntity, Projectile projectile, int i, float f, float g, float h, @Nullable LivingEntity livingEntity2) {
        projectile.shootFromRotation(livingEntity, livingEntity.getXRot(), livingEntity.getYRot() + h, 0.0F, f, g);
    }
}
