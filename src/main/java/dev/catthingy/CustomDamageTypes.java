package dev.catthingy;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class CustomDamageTypes {
    public static final ResourceKey<DamageType> SMITE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("catthingy", "smite"));
    public static final ResourceKey<DamageType> FIRE_ASPECT = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("catthingy", "fire_aspect"));

    public static DamageSource of(Level level, ResourceKey<DamageType> key, Entity direct, Entity cause) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key), direct, cause);
    }
}
