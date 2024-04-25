package dev.catthingy.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(CrossbowItem.class)
public abstract class CrossbowBuffMixin extends ProjectileWeaponItem {

    public CrossbowBuffMixin(Properties properties) {
        super(properties);
    }

    @Override
    protected void shoot(
            Level level,
            LivingEntity livingEntity,
            InteractionHand interactionHand,
            ItemStack itemStack,
            List<ItemStack> list,
            float velocity,
            float inaccuracy,
            boolean crit,
            @Nullable LivingEntity livingEntity2
    ) {
        for (int l = 0; l < list.size(); ++l) {
            ItemStack itemStack2 = list.get(l);
            if (!itemStack2.isEmpty()) {
                itemStack.hurtAndBreak(this.getDurabilityUse(itemStack2), livingEntity, LivingEntity.getSlotForHand(interactionHand));
                Projectile projectile = this.createProjectile(level, livingEntity, itemStack, itemStack2, crit);
                this.shootProjectile(livingEntity, projectile, l, velocity, inaccuracy + (l == 0.0F ? 0.0F : 7.5F), 0, livingEntity2);
                level.addFreshEntity(projectile);
                if (l != 0 && projectile instanceof AbstractArrow arrow) {
                    arrow.setKnockback(arrow.getKnockback() / 2);
                }
            }
        }
    }
}
