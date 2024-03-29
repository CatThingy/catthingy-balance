package dev.catthingy.mixin;

import dev.catthingy.CustomDamageTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageEnchantment.class)
public class SmiteReworkMixin extends Enchantment {
    protected SmiteReworkMixin(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] applicableSlots) {
        super(rarity, category, applicableSlots);
    }

    @Inject(method = "getDamageBonus", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    public void revertSmite(int level, MobType type, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(0.0F);
    }

    @Override
    public void doPostAttack(LivingEntity attacker, Entity target, int level) {
        if (((DamageEnchantment) ((Object) this)).type == DamageEnchantment.UNDEAD) {
            float multiplier = 1.0f;
            if (attacker instanceof Player player) {
                multiplier = player.getAttackStrengthScale(0.5F);
                multiplier *= multiplier;
            }
            target.hurt(CustomDamageTypes.of(target.level(), CustomDamageTypes.SMITE), multiplier * (0.3F + (float) Math.max(0, level - 1) * 0.15F));
            if (target.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SCRAPE,
                        target.getX(),
                        target.getY() + 1,
                        target.getZ(),
                        (int)(10.0F * multiplier),
                        serverLevel.random.nextDouble() - 0.5, serverLevel.random.nextDouble() - 0.5, serverLevel.random.nextDouble() - 0.5,
                        0.1
                );
            }
        }
    }
}
