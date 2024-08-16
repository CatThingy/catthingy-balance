package dev.catthingy.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.Properties.class)
public class DurabilityMixin {
    @Inject(method = "durability", at = @At("HEAD"))
    void test(int _param, CallbackInfoReturnable<Item.Properties> cir, @Local(argsOnly = true) LocalIntRef maxDamage) {
        maxDamage.set((int) (maxDamage.get() * 1.5));
    }
}
