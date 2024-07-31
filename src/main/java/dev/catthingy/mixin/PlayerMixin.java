package dev.catthingy.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {
    @Redirect(method="attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;resetAttackStrengthTicker()V"))
    void noReset (Player instance) {
        // This method intentionally left blank.
    }
    @Inject(method="attack", at = @At("RETURN"))
    void reset (Entity target, CallbackInfo ci) {
        ((Player) ((Object)this)).resetAttackStrengthTicker();
    }

//    @Redirect(method="attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;igniteForSeconds(I)V"))
//    void noFire (Entity instance, int seconds) {
//        // This method intentionally left blank.
//    }
}
