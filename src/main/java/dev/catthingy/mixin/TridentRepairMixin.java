package dev.catthingy.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TridentItem.class)
public abstract class TridentRepairMixin extends Item {
    public TridentRepairMixin(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(Items.PRISMARINE_CRYSTALS) || repairCandidate.is(Items.PRISMARINE_SHARD) || super.isValidRepairItem(stack, repairCandidate);
    }
}
