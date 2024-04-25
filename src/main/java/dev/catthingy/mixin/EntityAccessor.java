package dev.catthingy.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
interface EntityAccessor {
    @Invoker
    Vec3 callCalculateViewVector(float xRot, float yRot);
}
