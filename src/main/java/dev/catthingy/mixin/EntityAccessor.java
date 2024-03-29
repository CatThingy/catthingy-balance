package dev.catthingy.mixin;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
interface EntityAccessor {
    @Invoker
    boolean callGetSharedFlag(int flag);
    @Invoker
    Vec3 callCalculateViewVector(float xRot, float yRot);

    @Accessor
    SynchedEntityData getEntityData();
}
