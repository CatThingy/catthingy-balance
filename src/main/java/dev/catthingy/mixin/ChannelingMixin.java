package dev.catthingy.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.TridentChannelingEnchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TridentChannelingEnchantment.class)
public class ChannelingMixin extends Enchantment {

    protected ChannelingMixin(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] applicableSlots) {
        super(rarity, category, applicableSlots);
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
