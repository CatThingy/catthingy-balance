package dev.catthingy.mixin;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Supplier;

@Mixin(Tiers.class)
public abstract class NetheriteRepairMixin {
    @Redirect(method="<clinit>", at = @At(value = "NEW", target="(Ljava/lang/String;ILnet/minecraft/tags/TagKey;IFFILjava/util/function/Supplier;)Lnet/minecraft/world/item/Tiers;", ordinal = 5))
    private static Tiers test(String tagKey, int j, TagKey<Block> f, int g, float k, float supplier, int string, Supplier<Ingredient> i) {
        return init(tagKey, j, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2031, 9.0F, 4.0F, 15, () -> Ingredient.of(Items.DIAMOND));
    }

    @Invoker("<init>")
    private static Tiers init(String name, int id, final TagKey<Block> tagKey, final int j, final float f, final float g, final int k, final Supplier<Ingredient> supplier) {
        throw new UnsupportedOperationException();
    }
}
