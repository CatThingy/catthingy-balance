package dev.catthingy.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public class CrossbowBuffMixin {
    @Inject(method = "getArrow", at = @At("TAIL"))
    private static void getArrow(Level level, LivingEntity livingEntity, ItemStack crossbowStack, ItemStack ammoStack, CallbackInfoReturnable<AbstractArrow> cir, @Local AbstractArrow abstractArrow) {
        int punchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, crossbowStack);
        if (punchLevel > 0) {
            abstractArrow.setKnockback(punchLevel);
        }
        int flameLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, crossbowStack);
        if (flameLevel > 0) {
            abstractArrow.setSecondsOnFire(100);
        }
        int infinityLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, crossbowStack);
        if (infinityLevel > 0 && ammoStack.is(Items.ARROW)) {
            abstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }
    }

    @Redirect(method = "performShooting", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CrossbowItem;shootProjectile(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;FZFFF)V", ordinal = 1))
    private static void shootMultishot1(Level level, LivingEntity shooter, InteractionHand hand, ItemStack crossbowStack, ItemStack ammoStack, float soundPitch, boolean isCreativeMode, float velocity, float inaccuracy, float projectileAngle, @Local float[] pitches, @Local boolean bl) {
        CrossbowItemAccessor.callShootProjectile(level, shooter, hand, crossbowStack, ammoStack, pitches[1], bl, velocity, inaccuracy * 7.5F, 0.01F);
    }

    @Redirect(method = "performShooting", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CrossbowItem;shootProjectile(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;FZFFF)V", ordinal = 2))
    private static void shootMultishot2(Level level, LivingEntity shooter, InteractionHand hand, ItemStack crossbowStack, ItemStack ammoStack, float soundPitch, boolean isCreativeMode, float velocity, float inaccuracy, float projectileAngle, @Local float[] pitches, @Local boolean bl) {
        CrossbowItemAccessor.callShootProjectile(level, shooter, hand, crossbowStack, ammoStack, pitches[1], bl, velocity, inaccuracy * 7.5F, -0.01F);
    }

    /**
     * @author me (:
     * @reason too dumb to figure out how to make it work
     */
    @Overwrite
    static boolean loadProjectile(LivingEntity shooter, ItemStack crossbowStack, ItemStack ammoStack, boolean hasAmmo, boolean isCreative) {
        if (ammoStack.isEmpty()) {
            return false;
        } else {
            int infinityLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, crossbowStack);
            boolean bl = isCreative && ammoStack.getItem() instanceof ArrowItem;
            ItemStack itemStack;
            if (!bl && !isCreative && !hasAmmo && !(infinityLevel > 0 && ammoStack.is(Items.ARROW))) {
                itemStack = ammoStack.split(1);
                if (ammoStack.isEmpty() && shooter instanceof Player) {
                    ((Player) shooter).getInventory().removeItem(ammoStack);
                }
            } else {
                itemStack = ammoStack.copy();
            }

            CrossbowItemAccessor.callAddChargedProjectile(crossbowStack, itemStack);
            return true;
        }
    }
}

@Mixin(CrossbowItem.class)
interface CrossbowItemAccessor {
    @Invoker
    static void callShootProjectile(Level level, LivingEntity shooter, InteractionHand hand, ItemStack crossbowStack, ItemStack ammoStack, float soundPitch, boolean isCreativeMode, float velocity, float inaccuracy, float projectileAngle) {
        throw new UnsupportedOperationException();
    }

    @Invoker
    static void callAddChargedProjectile(ItemStack crossbowStack, ItemStack ammoStack) {
        throw new UnsupportedOperationException();
    }
}

