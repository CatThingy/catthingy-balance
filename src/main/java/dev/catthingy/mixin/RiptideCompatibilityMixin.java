package dev.catthingy.mixin;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.TridentRiptideEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentRiptideEnchantment.class)
public class RiptideCompatibilityMixin extends Enchantment{

    protected RiptideCompatibilityMixin(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] applicableSlots) {
        super(rarity, category, applicableSlots);
    }

    @Inject(method = "checkCompatibility", at = @At("RETURN"), cancellable = true)
    public void checkCompatibility(Enchantment other, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(super.checkCompatibility(other));
    }
}
