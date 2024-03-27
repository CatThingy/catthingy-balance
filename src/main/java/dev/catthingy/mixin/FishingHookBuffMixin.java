package dev.catthingy.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingHook.class)
public class FishingHookBuffMixin {
    @Inject(method="retrieve", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    void adjustDamage(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        FishingHook self = (FishingHook) ((Object)this);
        Entity hooked = self.getHookedIn();
        if (hooked != null) {
            cir.setReturnValue(1);
        }
    }
}
