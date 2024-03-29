package dev.catthingy.mixin;

import dev.catthingy.CatThingyBalance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FireworkRocketEntity.class)
public class FireworkDamageReworkMixin {
    @Shadow
    private @Nullable LivingEntity attachedToEntity;

    @Shadow
    @Final
    private static EntityDataAccessor<ItemStack> DATA_ID_FIREWORKS_ITEM;

    /**
     * @author me (:
     * @reason Complete rework of damage system
     */
    @Overwrite
    private void dealExplosionDamage() {
        FireworkRocketEntity self = (FireworkRocketEntity) ((Object) this);
        boolean hasDamage = false;
        float closeDamage = 2.0F;
        float mediumDamage = 2.0F;
        float farDamage = 1.0F;
        ItemStack itemStack = ((EntityAccessor) self).getEntityData().get(DATA_ID_FIREWORKS_ITEM);
        CompoundTag compoundTag = itemStack.isEmpty() ? null : itemStack.getTagElement("Fireworks");
        ListTag listTag = compoundTag != null ? compoundTag.getList("Explosions", 10) : null;
        if (listTag != null && !listTag.isEmpty()) {
            for (Tag value : listTag) {
                CompoundTag tag = (CompoundTag) value;
                switch (tag.getByte("Type")) {
                    case 1:
                        CatThingyBalance.LOGGER.info("LARGE");
                        farDamage += 2.0F;
                        break;
                    case 2:
                        CatThingyBalance.LOGGER.info("STAR");
                        closeDamage += 3.0F;
                        break;
                    default:
                        CatThingyBalance.LOGGER.info("DEFAULT");
                        mediumDamage += 2.0F;
                }

            }
            hasDamage = true;
        }

        if (hasDamage) {
            if (this.attachedToEntity != null) {
                this.attachedToEntity.hurt(self.damageSources().fireworks(self, self.getOwner()), closeDamage);
            }

            Vec3 vec3 = self.position();

            for (LivingEntity livingEntity : self.level().getEntitiesOfClass(LivingEntity.class, self.getBoundingBox().inflate(7.5))) {
                double sqrDistance = self.distanceToSqr(livingEntity);
                if (livingEntity != this.attachedToEntity && !(sqrDistance > 56.25)) {
                    boolean bl = false;

                    for (int i = 0; i < 2; ++i) {
                        Vec3 vec32 = new Vec3(livingEntity.getX(), livingEntity.getY(0.5 * (double) i), livingEntity.getZ());
                        HitResult hitResult = self.level().clip(new ClipContext(vec3, vec32, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, self));
                        if (hitResult.getType() == HitResult.Type.MISS) {
                            bl = true;
                            break;
                        }
                    }

                    if (bl) {
                        float distance = self.distanceTo(livingEntity);
                        float damage = farDamage * (float) Math.sqrt((7.5 - (double) distance) / 7.5);
                        if (sqrDistance <= 25.0) {
                            damage += mediumDamage * (float) Math.sqrt((5.0 - (double) distance) / 5.0);
                        } if (sqrDistance <= 4.0) {
                            damage += closeDamage;
                        }
                        livingEntity.hurt(self.damageSources().fireworks(self, self.getOwner()), damage);
                    }
                }
            }
        }
    }
}
