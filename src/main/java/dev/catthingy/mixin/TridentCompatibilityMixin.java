package dev.catthingy.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentItem.class)
public class TridentCompatibilityMixin {

    /**
     * @author me (:
     * @reason too intertwined; must replace to make channeling/loyalty/riptide compatible
     */
    @Overwrite
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        TridentItem self = ((TridentItem) ((Object) this));
        if (livingEntity instanceof Player player) {
            int chargeDuration = self.getUseDuration(stack) - timeCharged;
            if (chargeDuration >= 10) {
                int riptideLevel = EnchantmentHelper.getRiptide(stack);
                if (!level.isClientSide) {
                    stack.hurtAndBreak(1, player, playerx -> playerx.broadcastBreakEvent(livingEntity.getUsedItemHand()));
                    if (riptideLevel == 0 || (riptideLevel > 0 && !player.isInWaterOrRain())) {
                        ThrownTrident thrownTrident = new ThrownTrident(level, player, stack);
                        thrownTrident.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F + (float) riptideLevel * 0.5F, 1.0F);
                        if (player.getAbilities().instabuild) {
                            thrownTrident.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }

                        level.addFreshEntity(thrownTrident);
                        level.playSound(null, thrownTrident, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                        if (!player.getAbilities().instabuild) {
                            player.getInventory().removeItem(stack);
                        }
                        return;
                    }
                }

                player.awardStat(Stats.ITEM_USED.get(self));
                if (riptideLevel > 0 && player.isInWaterOrRain()) {
                    float f = player.getYRot();
                    float g = player.getXRot();
                    float h = -Mth.sin(f * (float) (Math.PI / 180.0)) * Mth.cos(g * (float) (Math.PI / 180.0));
                    float k = -Mth.sin(g * (float) (Math.PI / 180.0));
                    float l = Mth.cos(f * (float) (Math.PI / 180.0)) * Mth.cos(g * (float) (Math.PI / 180.0));
                    float m = Mth.sqrt(h * h + k * k + l * l);
                    float n = 3.0F * ((1.0F + (float) riptideLevel) / 4.0F);
                    h *= n / m;
                    k *= n / m;
                    l *= n / m;
                    player.push(h, k, l);
                    player.startAutoSpinAttack(20);
                    if (player.onGround()) {
                        player.move(MoverType.SELF, new Vec3(0.0, 1.1999999F, 0.0));
                    }

                    SoundEvent soundEvent;
                    if (riptideLevel >= 3) {
                        soundEvent = SoundEvents.TRIDENT_RIPTIDE_3;
                    } else if (riptideLevel == 2) {
                        soundEvent = SoundEvents.TRIDENT_RIPTIDE_2;
                    } else {
                        soundEvent = SoundEvents.TRIDENT_RIPTIDE_1;
                    }

                    level.playSound(null, player, soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            }

        }
    }

    @Inject(method = "use", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    public void use(Level level, Player player, InteractionHand usedHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        player.startUsingItem(usedHand);
        ItemStack itemStack = player.getItemInHand(usedHand);
        cir.setReturnValue(InteractionResultHolder.consume(itemStack));
    }
}
