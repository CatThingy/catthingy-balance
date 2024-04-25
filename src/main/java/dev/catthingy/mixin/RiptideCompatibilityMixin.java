package dev.catthingy.mixin;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.TridentRiptideEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentRiptideEnchantment.class)
public class RiptideCompatibilityMixin extends Enchantment{

    public RiptideCompatibilityMixin(EnchantmentDefinition enchantmentDefinition) {
        super(enchantmentDefinition);
    }

    @Inject(method = "checkCompatibility", at = @At("RETURN"), cancellable = true)
    public void checkCompatibility(Enchantment other, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(super.checkCompatibility(other));
    }
}
