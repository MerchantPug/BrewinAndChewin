package umpaz.brewinandchewin.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import umpaz.brewinandchewin.common.registry.BnCBlocks;
import umpaz.brewinandchewin.common.registry.BnCEffects;
import umpaz.brewinandchewin.common.registry.BnCItems;
import umpaz.brewinandchewin.common.utility.BnCTextUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nullable;
import java.util.List;

public class BoozeItem extends Item {
    private final Fluid fluid;
    private final int potency;
    private final int duration;

    public BoozeItem(Fluid fluid, int potency, int duration, Properties properties) {
        super(properties);
        this.fluid = fluid;
        this.potency = potency;
        this.duration = duration;
    }

    public Fluid getFluid() {
        return this.fluid;
    }

    public InteractionResultHolder<ItemStack> use( Level level, Player player, InteractionHand hand ) {
        ItemStack heldStack = player.getItemInHand(hand);
       if ( heldStack.isEdible() ) {
          if ( player.canEat(heldStack.getFoodProperties(player).canAlwaysEat()) ) {
             player.startUsingItem(hand);
             return InteractionResultHolder.consume(heldStack);
          }
          else {
             return InteractionResultHolder.fail(heldStack);
          }
       }
       else {
          return ItemUtils.startUsingInstantly(level, player, hand);
       }
    }


    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
        if (!level.isClientSide) {
            this.affectConsumer(consumer);
        }
        ItemStack containerStack = stack.getCraftingRemainingItem();
        Player player;
        if (stack.isEdible()) {
            super.finishUsingItem(stack, level, consumer);
        } else {
            player = consumer instanceof Player ? (Player)consumer : null;
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)player, stack);
            }
            if (player != null) {
                player.awardStat(Stats.ITEM_USED.get(this));
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
        }
        if (stack.isEmpty()) {
            return containerStack;
        } else {
            if (consumer instanceof Player) {
                player = (Player)consumer;
                if (!((Player)consumer).getAbilities().instabuild && !player.getInventory().add(containerStack)) {
                    player.drop(containerStack, false);
                }
            }
            return stack;
        }
    }

    //Tipsy Stuff
    public void affectConsumer(LivingEntity consumer) {
       if (consumer.hasEffect(BnCEffects.TIPSY.get())) {
            MobEffectInstance effect = consumer.getEffect(BnCEffects.TIPSY.get());
          consumer.addEffect(new MobEffectInstance(BnCEffects.TIPSY.get(), effect.getDuration() + ( duration * 1200 ), Math.min(effect.getAmplifier() + potency, 9), false, false, true), consumer);
        } else {
           consumer.addEffect(new MobEffectInstance(BnCEffects.TIPSY.get(), duration * 1200, potency - 1, false, false, true), consumer);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        MutableComponent textTipsy = BnCTextUtils.getTranslation("tooltip.tipsy"+potency, duration);
        tooltip.add(textTipsy.withStyle(ChatFormatting.RED));
        TextUtils.addFoodEffectTooltip(stack, tooltip, 1.0F);
        if (stack.is(BnCItems.DREAD_NOG.get())) {
            MutableComponent textEmpty = BnCTextUtils.getTranslation("tooltip." + this);
            tooltip.add(textEmpty.withStyle(ChatFormatting.RED));
        }
    }
}
