package dev.catthingy.mixin;

import dev.catthingy.CustomDamageTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.FireAspectEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FireAspectEnchantment.class)
public class FireAspectReworkMixin extends Enchantment {
    protected FireAspectReworkMixin(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] applicableSlots) {
        super(rarity, category, applicableSlots);
    }

    @Override
    public void doPostAttack(LivingEntity attacker, Entity target, int level) {
        float multiplier = 1.0f;
        if (attacker instanceof Player player) {
            multiplier = player.getAttackStrengthScale(0.5F);
            multiplier *= multiplier;
        }

        if (((EntityAccessor) target).callGetSharedFlag(0)) {
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
        target.setSecondsOnFire((int) (multiplier * level));
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return !(other instanceof DamageEnchantment);
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() instanceof AxeItem || super.canEnchant(stack);
    }
}

@Mixin(Entity.class)
interface EntityAccessor {
    @Invoker
    boolean callGetSharedFlag(int flag);
}
