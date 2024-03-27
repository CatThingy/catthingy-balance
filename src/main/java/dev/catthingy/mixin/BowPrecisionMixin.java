package dev.catthingy.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BowItem.class)
public class BowPrecisionMixin {
    @Inject(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    void bowPrecision(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged, CallbackInfo ci, @Local AbstractArrow arrow, @Local float f) {
        int chargeTime = ((BowItem) ((Object) this)).getUseDuration(stack) - timeCharged;
        arrow.setCritArrow(chargeTime >= 20 && chargeTime < 25);
        if (chargeTime >= 20 && chargeTime < 22) {
            arrow.setBaseDamage(arrow.getBaseDamage() * 1.5);
        }
    }
}
