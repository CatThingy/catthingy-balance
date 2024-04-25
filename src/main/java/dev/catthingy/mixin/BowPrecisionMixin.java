package dev.catthingy.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(BowItem.class)
public class BowPrecisionMixin {
    @Redirect(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BowItem;shoot(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;Ljava/util/List;FFZLnet/minecraft/world/entity/LivingEntity;)V"))
    void bowPrecision(BowItem instance, Level level, LivingEntity player, InteractionHand interactionHand, ItemStack stack, List<ItemStack> projectiles, float velocity, float inaccuracy, boolean _crit, LivingEntity _null, @Local(name="i") int chargeTime) {
        boolean crit = chargeTime >= 20 && chargeTime < 22;
        if (chargeTime >= 20 && chargeTime < 30) {
            inaccuracy = 0.0F;
        }
        ((ProjectileWeaponItemAccessor) instance).callShoot(level, player, interactionHand, stack, projectiles, velocity, inaccuracy, crit, _null);
    }

    @Mixin(ProjectileWeaponItem.class)
    public interface ProjectileWeaponItemAccessor {
        @Invoker
        void callShoot(
                Level level,
                LivingEntity livingEntity,
                InteractionHand interactionHand,
                ItemStack itemStack,
                List<ItemStack> list,
                float f,
                float g,
                boolean bl,
                @Nullable LivingEntity livingEntity2
        );
    }

}
