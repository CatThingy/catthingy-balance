package dev.catthingy.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ThrownTrident.class)
public class ImpalingThrownTridentMixin {

    @ModifyConstant(method = "onHitEntity", constant=@Constant(floatValue = 8.0F))
    float setDamage (float f) {
        ThrownTrident self = (ThrownTrident) ((Object) this);
        ItemStack item = self.getPickupItemStackOrigin();
        int impalingLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.IMPALING, item);

        if (impalingLevel > 0) {
            f += impalingLevel * 0.5F + 0.5F;
        }
        return f;
    }

    @Redirect(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getDamageBonus(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EntityType;)F"))
    float noBonusDamage(ItemStack itemStack, @Nullable EntityType<?> entityType) {
        return 0.0F;
    }
}
