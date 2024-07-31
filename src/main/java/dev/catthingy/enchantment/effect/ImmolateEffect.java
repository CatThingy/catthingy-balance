package dev.catthingy.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.catthingy.CustomDamageTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record ImmolateEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<ImmolateEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(ImmolateEffect::amount)
            ).apply(instance, ImmolateEffect::new)
    );

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {
        if (entity instanceof LivingEntity target) {
            boolean apply_effect = true;
            if (target instanceof Player player) {
                apply_effect = player.getAttackStrengthScale(0.5f) == 1.0;
            }
            if (target.isOnFire() && apply_effect) {
                target.hurt(CustomDamageTypes.of(level, CustomDamageTypes.IMMOLATE, item.owner(), item.owner()), amount.calculate(enchantmentLevel));

                level.sendParticles(ParticleTypes.FLAME,
                    target.getX(),
                    target.getY() + 1,
                    target.getZ(),
                    10,
                    (level.random.nextDouble() - 0.5) / 3,
                    (level.random.nextDouble() - 0.5) / 3,
                    (level.random.nextDouble() - 0.5) / 3,
                    0.1
                );
            }
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
