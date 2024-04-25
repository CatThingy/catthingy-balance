package dev.catthingy;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;

public class FireAspectEnchantment extends Enchantment {
    public FireAspectEnchantment(EnchantmentDefinition enchantmentDefinition) {
        super(enchantmentDefinition);
    }

    @Override
    public void doPostAttack(LivingEntity attacker, Entity target, int level) {
        float multiplier = 1.0f;
        if (attacker instanceof Player player) {
            multiplier = player.getAttackStrengthScale(0.5F);
            if (multiplier < 0.5) {
                multiplier = 0;
            }
            multiplier *= multiplier;
        }

        if (target.wasOnFire) {
            target.hurt(CustomDamageTypes.of(target.level(), CustomDamageTypes.FIRE_ASPECT), multiplier * (0.3F + (float) Math.max(0, level - 1) * 0.2F));
            if (target.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.FLAME,
                        target.getX(),
                        target.getY() + 1,
                        target.getZ(),
                        (int) (10.0F * multiplier),
                        (serverLevel.random.nextDouble() - 0.5) / 3,
                        (serverLevel.random.nextDouble() - 0.5) / 3,
                        (serverLevel.random.nextDouble() - 0.5) / 3,
                        0.1
                );
            }
        }
        target.igniteForTicks((int) (20 * multiplier * level));
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return !(other instanceof DamageEnchantment);
    }
}
