package dev.catthingy;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SmiteEnchantment extends DamageEnchantment {
    public SmiteEnchantment(EnchantmentDefinition enchantmentDefinition) {
        super(enchantmentDefinition, Optional.empty());
    }

    @Override
    public float getDamageBonus(int i, @Nullable EntityType<?> entityType) {
        return 0.0F;
    }

    @Override
    public void doPostAttack(LivingEntity attacker, Entity target, int level) {
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
                    (int) (10.0F * multiplier),
                    serverLevel.random.nextDouble() - 0.5, serverLevel.random.nextDouble() - 0.5, serverLevel.random.nextDouble() - 0.5,
                    0.1
            );
        }
    }
}
