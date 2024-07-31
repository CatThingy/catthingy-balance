package dev.catthingy.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = AnvilMenu.class, priority = 999)
public abstract class AnvilReworkMixin {
    /**
     * @author me (:
     * @reason very terrible to deal with this
     */
    @Overwrite
    public void createResult() {
        AnvilMenuAccessor self = (AnvilMenuAccessor) this;
        ItemCombinerMenuAccessor self_2 = (ItemCombinerMenuAccessor) this;

        ItemStack itemStack = self_2.getInputSlots().getItem(0);
        self.getCost().set(1);
        int repairCost = 0;
        int enchantCost = 0;
        int totalCost = 0;
        long baseCost = 0L;
        int renameCost = 0;
        if (!itemStack.isEmpty() && EnchantmentHelper.canStoreEnchantments(itemStack)) {
            ItemStack itemStack2 = itemStack.copy();
            ItemStack itemStack3 = self_2.getInputSlots().getItem(1);
            ItemEnchantments.Mutable itemEnchants = new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(itemStack2));
            baseCost += (long) itemStack.getOrDefault(DataComponents.REPAIR_COST, 0)
                    + (long) itemStack3.getOrDefault(DataComponents.REPAIR_COST, 0);
            self.setRepairItemCountCost(0);
            if (!itemStack3.isEmpty()) {
                boolean storedEnchants = itemStack3.has(DataComponents.STORED_ENCHANTMENTS);
                if (itemStack2.isDamageableItem() && itemStack2.getItem().isValidRepairItem(itemStack, itemStack3)) {
                    int repairAmount = Math.min(itemStack2.getDamageValue(), itemStack2.getMaxDamage() / 3);
                    if (repairAmount <= 0) {
                        self_2.getResultSlots().setItem(0, ItemStack.EMPTY);
                        self.getCost().set(0);
                        return;
                    }

                    int repairItem;
                    for (repairItem = 0; repairAmount > 0 && repairItem < itemStack3.getCount(); ++repairItem) {
                        int n = itemStack2.getDamageValue() - repairAmount;
                        itemStack2.setDamageValue(n);
                        ++repairCost;
                        ++totalCost;
                        repairAmount = Math.min(itemStack2.getDamageValue(), itemStack2.getMaxDamage() / 4);
                    }

                    self.setRepairItemCountCost(repairItem);
                } else {
                    boolean canRepair = itemStack2.is(itemStack3.getItem());
                    if (itemStack2.getItem() instanceof TieredItem tier1 && itemStack3.getItem() instanceof TieredItem tier2
                            && tier1.getTier().equals(Tiers.NETHERITE) && tier2.getTier().getRepairIngredient().equals(Ingredient.of(Items.DIAMOND))) {
                        canRepair |= (tier1 instanceof HoeItem && tier2 instanceof HoeItem)
                                || (tier1 instanceof AxeItem && tier2 instanceof AxeItem)
                                || (tier1 instanceof ShovelItem && tier2 instanceof ShovelItem)
                                || (tier1 instanceof SwordItem && tier2 instanceof SwordItem)
                                || (tier1 instanceof PickaxeItem && tier2 instanceof PickaxeItem);
                    }
                    if (itemStack2.getItem() instanceof ArmorItem armor1 && itemStack3.getItem() instanceof ArmorItem armor2
                            && armor1.getMaterial().equals(ArmorMaterials.NETHERITE) && armor2.getMaterial().equals(ArmorMaterials.DIAMOND)) {
                        canRepair |= armor1.getEquipmentSlot().equals(armor2.getEquipmentSlot());
                    }

                    if (!storedEnchants && (!canRepair || !itemStack2.isDamageableItem())) {
                        self_2.getResultSlots().setItem(0, ItemStack.EMPTY);
                        self.getCost().set(0);
                        return;
                    }

                    if (itemStack2.isDamageableItem() && !storedEnchants) {
                        int itemDamage = itemStack.getMaxDamage() - itemStack.getDamageValue();
                        int item2Damage = itemStack3.getMaxDamage() - itemStack3.getDamageValue();
                        int repairAmount = item2Damage + itemStack2.getMaxDamage() * 12 / 100;
                        int totalRepair = itemDamage + repairAmount;
                        int finalDamage = itemStack2.getMaxDamage() - totalRepair;
                        if (finalDamage < 0) {
                            finalDamage = 0;
                        }

                        if (finalDamage < itemStack2.getDamageValue()) {
                            itemStack2.setDamageValue(finalDamage);
                            repairCost += 2;
                            totalCost += 2;
                        }
                    }

                    ItemEnchantments additionalEnchants = EnchantmentHelper.getEnchantmentsForCrafting(itemStack3);
                    boolean bl2 = false;
                    boolean bl3 = false;

                    for (Object2IntMap.Entry<Holder<Enchantment>> entry : additionalEnchants.entrySet()) {
                        Holder<Enchantment> holder = entry.getKey();
                        Enchantment enchantment = holder.value();
                        int q = itemEnchants.getLevel(holder);
                        int r = entry.getIntValue();
                        r = q == r ? r + 1 : Math.max(r, q);
                        boolean canStackEnchant = enchantment.canEnchant(itemStack);
                        if (self_2.getPlayer().hasInfiniteMaterials() || itemStack.is(Items.ENCHANTED_BOOK)) {
                            canStackEnchant = true;
                        }

                        for (Holder<Enchantment> holder2 : itemEnchants.keySet()) {
                            if (!holder2.equals(holder) && !Enchantment.areCompatible(holder, holder2)) {
                                canStackEnchant = false;
                                ++enchantCost;
                                ++totalCost;
                            }
                        }

                        if (!canStackEnchant) {
                            bl3 = true;
                        } else {
                            bl2 = true;
                            if (r > enchantment.getMaxLevel()) {
                                r = enchantment.getMaxLevel();
                            }

                            itemEnchants.set(holder, r);
                            int additionalEnchantmentCost = enchantment.getAnvilCost();
                            if (storedEnchants) {
                                additionalEnchantmentCost = Math.max(1, additionalEnchantmentCost / 2);
                            }

                            totalCost += additionalEnchantmentCost * r;
                            enchantCost += additionalEnchantmentCost * r;
                            if (itemStack.getCount() > 1) {
                                totalCost = 40;
                            }
                        }
                    }

                    if (bl3 && !bl2) {
                        self_2.getResultSlots().setItem(0, ItemStack.EMPTY);
                        self.getCost().set(0);
                        return;
                    }
                }
            }

            if (self.getItemName() != null && !StringUtil.isBlank(self.getItemName())) {
                if (!self.getItemName().equals(itemStack.getHoverName().getString())) {
                    renameCost = 1;
                    totalCost += renameCost;
                    itemStack2.set(DataComponents.CUSTOM_NAME, Component.literal(self.getItemName()));
                }
            } else if (itemStack.has(DataComponents.CUSTOM_NAME)) {
                renameCost = 1;
                totalCost += renameCost;
                itemStack2.remove(DataComponents.CUSTOM_NAME);
            }

            int t = (int) Mth.clamp(baseCost + (long) totalCost, 0L, 2147483647L);
            self.getCost().set(t);
            if (totalCost <= 0) {
                itemStack2 = ItemStack.EMPTY;
            }

            if (renameCost == totalCost && renameCost > 0 && self.getCost().get() >= 40) {
                self.getCost().set(39);
            }

            if (enchantCost > 0 && self.getCost().get() >= 40 && !self_2.getPlayer().hasInfiniteMaterials()) {
                itemStack2 = ItemStack.EMPTY;
            }

            if (!itemStack2.isEmpty()) {
                int k = itemStack2.getOrDefault(DataComponents.REPAIR_COST, 0);
                if (k < itemStack3.getOrDefault(DataComponents.REPAIR_COST, 0)) {
                    k = itemStack3.getOrDefault(DataComponents.REPAIR_COST, 0);
                }

                if (renameCost != totalCost || renameCost == 0) {
                    k = (int) Math.min((long) k * 2L + 1L, 2147483647L);
                }

                if (enchantCost <= 0) {
                    if (repairCost == 0 && renameCost == 0) {
                        itemStack2 = ItemStack.EMPTY;
                    }
                    self.getCost().set(Math.max(1, (repairCost + renameCost) * (31 - Integer.numberOfLeadingZeros(t))));
                    k = itemStack2.getOrDefault(DataComponents.REPAIR_COST, 0) + renameCost;
                }

                itemStack2.set(DataComponents.REPAIR_COST, k);
                EnchantmentHelper.setEnchantments(itemStack2, itemEnchants.toImmutable());
            }

            self_2.getResultSlots().setItem(0, itemStack2);
            ((AnvilMenu) ((Object) (this))).broadcastChanges();
        } else {
            self_2.getResultSlots().setItem(0, ItemStack.EMPTY);
            self.getCost().set(0);
        }

    }

    @Mixin(ItemCombinerMenu.class)
    interface ItemCombinerMenuAccessor {
        @Accessor
        Player getPlayer();

        @Accessor
        Container getInputSlots();

        @Accessor
        ResultContainer getResultSlots();
    }

    @Mixin(AnvilMenu.class)
    interface AnvilMenuAccessor {

        @Accessor
        void setRepairItemCountCost(int repairItemCountCost);

        @Accessor
        String getItemName();

        @Accessor
        DataSlot getCost();
    }
}