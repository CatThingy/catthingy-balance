package dev.catthingy;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ChannelingEnchantment extends Enchantment {
    public ChannelingEnchantment(EnchantmentDefinition enchantmentDefinition) {
        super(enchantmentDefinition);
    }

    @Override
    public void doPostAttack(LivingEntity attacker, Entity target, int level) {
        Level gameLevel = target.level();
        if (attacker.isAutoSpinAttack() && gameLevel instanceof ServerLevel && gameLevel.isThundering()) {
            BlockPos blockPos = attacker.blockPosition();
            if (gameLevel.canSeeSky(blockPos)) {
                LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(target.level());
                if (lightningBolt != null) {
                    lightningBolt.moveTo(Vec3.atBottomCenterOf(blockPos));
                    lightningBolt.setCause(attacker instanceof ServerPlayer ? (ServerPlayer) attacker : null);
                    gameLevel.addFreshEntity(lightningBolt);
                    attacker.playSound(SoundEvents.TRIDENT_THUNDER, 5.0F, 1.0F);
                }
            }

        }

    }
}
