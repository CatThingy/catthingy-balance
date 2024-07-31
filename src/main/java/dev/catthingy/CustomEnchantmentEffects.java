package dev.catthingy;

import com.mojang.serialization.MapCodec;
import dev.catthingy.enchantment.effect.ImmolateEffect;
import dev.catthingy.enchantment.effect.SmiteEffect;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;

public class CustomEnchantmentEffects {
    public static MapCodec<ImmolateEffect> IMMOLATE_EFFECT = register("immolate", ImmolateEffect.CODEC);
    public static MapCodec<SmiteEffect> SMITE_EFFECT = register("smite", SmiteEffect.CODEC);

    private static <T extends EnchantmentEntityEffect> MapCodec<T> register(String id, MapCodec<T> codec) {
        return Registry.register(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, ResourceLocation.fromNamespaceAndPath("catthingy", id), codec);
    }

    public static void init () {}
}
