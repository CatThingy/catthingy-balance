package dev.catthingy.mixin;

import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Mob.class)
public class MobMixin {

//    @Redirect(method="doHurtTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;igniteForSeconds(I)V", ordinal = 0))
//    void noFire (Entity instance, int seconds) {
//        // This method intentionally left blank.
//    }
}
