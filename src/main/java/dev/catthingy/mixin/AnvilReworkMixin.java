package dev.catthingy.mixin;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(AnvilMenu.class)
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
        int totalCost = 0;
        int baseCost = 0;
        int renameCost = 0;
        int enchantCost = 0;
        int repairCost = 0;
        if (itemStack.isEmpty()) {
            self_2.getResultSlots().setItem(0, ItemStack.EMPTY);
            self.getCost().set(0);
        } else {
            ItemStack itemStack2 = itemStack.copy();
            ItemStack itemStack3 = self_2.getInputSlots().getItem(1);
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack2);
            baseCost += itemStack.getBaseRepairCost() + (itemStack3.isEmpty() ? 0 : itemStack3.getBaseRepairCost());
            self.setRepairItemCountCost(0);
            if (!itemStack3.isEmpty()) {
                boolean isBook = itemStack3.is(Items.ENCHANTED_BOOK) && !EnchantedBookItem.getEnchantments(itemStack3).isEmpty();
                if (itemStack2.isDamageableItem() && itemStack2.getItem().isValidRepairItem(itemStack, itemStack3)) {
                    int repairAmount = Math.min(itemStack2.getDamageValue(), itemStack2.getMaxDamage() / 4);
                    if (repairAmount <= 0) {
                        self_2.getResultSlots().setItem(0, ItemStack.EMPTY);
                        self.getCost().set(0);
                        return;
                    }

                    int repairItem;
                    for(repairItem = 0; repairAmount > 0 && repairItem < itemStack3.getCount(); ++repairItem) {
                        int n = itemStack2.getDamageValue() - repairAmount;
                        itemStack2.setDamageValue(n);
                        ++totalCost;
                        ++repairCost;
                        repairAmount = Math.min(itemStack2.getDamageValue(), itemStack2.getMaxDamage() / 4);
                    }

                    self.setRepairItemCountCost(repairItem);
                } else {
                    if (!isBook && (!itemStack2.is(itemStack3.getItem()) || !itemStack2.isDamageableItem())) {
                        self_2.getResultSlots().setItem(0, ItemStack.EMPTY);
                        self.getCost().set(0);
                        return;
                    }

                    if (itemStack2.isDamageableItem() && !isBook) {
                        int itemDamage = itemStack.getMaxDamage() - itemStack.getDamageValue();
                        int item2Damage = itemStack3.getMaxDamage() - itemStack3.getDamageValue();
                        int repairAmount = item2Damage + itemStack2.getMaxDamage() * 12 / 100;
                        int totalRepair = itemDamage + repairAmount;
                        int trueFinalDamage = itemStack2.getMaxDamage() - totalRepair;
                        if (trueFinalDamage < 0) {
                            trueFinalDamage = 0;
                        }

                        if (trueFinalDamage < itemStack2.getDamageValue()) {
                            itemStack2.setDamageValue(trueFinalDamage);
                            totalCost += 2;
                            repairCost += 2;
                        }
                    }

                    Map<Enchantment, Integer> map2 = EnchantmentHelper.getEnchantments(itemStack3);
                    boolean bl2 = false;
                    boolean bl3 = false;

                    for(Enchantment enchantment : map2.keySet()) {
                        if (enchantment != null) {
                            int q = map.getOrDefault(enchantment, 0);
                            int r = map2.get(enchantment);
                            r = q == r ? r + 1 : Math.max(r, q);
                            boolean bl4 = enchantment.canEnchant(itemStack);
                            if (self_2.getPlayer().getAbilities().instabuild || itemStack.is(Items.ENCHANTED_BOOK)) {
                                bl4 = true;
                            }

                            for(Enchantment enchantment2 : map.keySet()) {
                                if (enchantment2 != enchantment && !enchantment.isCompatibleWith(enchantment2)) {
                                    bl4 = false;
                                    ++totalCost;
                                    ++enchantCost;
                                }
                            }

                            if (!bl4) {
                                bl3 = true;
                            } else {
                                bl2 = true;
                                if (r > enchantment.getMaxLevel()) {
                                    r = enchantment.getMaxLevel();
                                }

                                map.put(enchantment, r);
                                int s = switch (enchantment.getRarity()) {
                                    case COMMON -> 1;
                                    case UNCOMMON -> 2;
                                    case RARE -> 4;
                                    case VERY_RARE -> 8;
                                };

                                if (isBook) {
                                    s = Math.max(1, s / 2);
                                }

                                totalCost += s * r;
                                enchantCost += s * r;
                                if (itemStack.getCount() > 1) {
                                    totalCost = 40;
                                }
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

            if (self.getItemName() != null && !Util.isBlank(self.getItemName())) {
                if (!self.getItemName().equals(itemStack.getHoverName().getString())) {
                    renameCost = 1;
                    totalCost += renameCost;
                    itemStack2.setHoverName(Component.literal(self.getItemName()));
                }
            } else if (itemStack.hasCustomHoverName()) {
                renameCost = 1;
                totalCost += renameCost;
                itemStack2.resetHoverName();
            }

            self.getCost().set(baseCost + totalCost);
            if (totalCost <= 0) {
                itemStack2 = ItemStack.EMPTY;
            }

            if (renameCost == totalCost && renameCost > 0 && self.getCost().get() >= 40) {
                self.getCost().set(39);
            }

            if (self.getCost().get() >= 40 && !self_2.getPlayer().getAbilities().instabuild) {
                itemStack2 = ItemStack.EMPTY;
            }

            if (!itemStack2.isEmpty()) {
                int t = itemStack2.getBaseRepairCost();
                if (!itemStack3.isEmpty() && t < itemStack3.getBaseRepairCost()) {
                    t = itemStack3.getBaseRepairCost();
                }

                if (renameCost != totalCost || renameCost == 0) {
                    t = AnvilMenu.calculateIncreasedRepairCost(t);
                }

                if (enchantCost <= 0) {
                    self.getCost().set((repairCost + renameCost) * (31 - Integer.numberOfLeadingZeros(t)));
                    t = itemStack2.getBaseRepairCost() + renameCost;
                }

                itemStack2.setRepairCost(t);
                EnchantmentHelper.setEnchantments(map, itemStack2);
            }

            self_2.getResultSlots().setItem(0, itemStack2);
            ((AnvilMenu)((Object) this)).broadcastChanges();
        }
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