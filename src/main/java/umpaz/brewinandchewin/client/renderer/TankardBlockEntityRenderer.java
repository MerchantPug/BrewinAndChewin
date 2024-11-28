package umpaz.brewinandchewin.client.renderer;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import umpaz.brewinandchewin.common.block.TankardBlock;
import umpaz.brewinandchewin.common.block.entity.TankardBlockEntity;
import umpaz.brewinandchewin.common.registry.BnCBlocks;
import umpaz.brewinandchewin.common.registry.BnCItems;

import java.util.Map;
import java.util.Random;

public class TankardBlockEntityRenderer implements BlockEntityRenderer<TankardBlockEntity> {

   public Map<Item, BlockState> boozeItemtoBlockMap() {
      return Util.make(Maps.newHashMap(), map -> {
         map.put(BnCItems.TANKARD.get(), BnCBlocks.TANKARD_MODEL.get().defaultBlockState());
         map.put(BnCItems.BEER.get(), BnCBlocks.BEER_MODEL.get().defaultBlockState());
         map.put(BnCItems.VODKA.get(), BnCBlocks.VODKA_MODEL.get().defaultBlockState());
         map.put(BnCItems.MEAD.get(), BnCBlocks.MEAD_MODEL.get().defaultBlockState());
         map.put(BnCItems.RICE_WINE.get(), BnCBlocks.RICE_WINE_MODEL.get().defaultBlockState());
         map.put(BnCItems.EGG_GROG.get(), BnCBlocks.EGG_GROG_MODEL.get().defaultBlockState());
         map.put(BnCItems.STRONGROOT_ALE.get(), BnCBlocks.STRONGROOT_ALE_MODEL.get().defaultBlockState());
         map.put(BnCItems.SACCHARINE_RUM.get(), BnCBlocks.SACCHARINE_RUM_MODEL.get().defaultBlockState());
         map.put(BnCItems.PALE_JANE.get(), BnCBlocks.PALE_JANE_MODEL.get().defaultBlockState());
         map.put(BnCItems.SALTY_FOLLY.get(), BnCBlocks.SALTY_FOLLY_MODEL.get().defaultBlockState());
         map.put(BnCItems.STEEL_TOE_STOUT.get(), BnCBlocks.STEEL_TOE_STOUT_MODEL.get().defaultBlockState());
         map.put(BnCItems.GLITTERING_GRENADINE.get(), BnCBlocks.GLITTERING_GRENADINE_MODEL.get().defaultBlockState());
         map.put(BnCItems.BLOODY_MARY.get(), BnCBlocks.BLOODY_MARY_MODEL.get().defaultBlockState());
         map.put(BnCItems.RED_RUM.get(), BnCBlocks.RED_RUM_MODEL.get().defaultBlockState());
         map.put(BnCItems.WITHERING_DROSS.get(), BnCBlocks.WITHERING_DROSS_MODEL.get().defaultBlockState());
         map.put(BnCItems.KOMBUCHA.get(), BnCBlocks.KOMBUCHA_MODEL.get().defaultBlockState());
         map.put(BnCItems.DREAD_NOG.get(), BnCBlocks.DREAD_NOG_MODEL.get().defaultBlockState());
      });
   }

   public TankardBlockEntityRenderer( BlockEntityRendererProvider.Context context ) {
   }


   // there is definitely a better way to do this, but it felt better to do this than what was there
   private void poseUtil( PoseStack poseStack, int fullCount, int curCount, Random seededRandom ) {
      Vector2f translateVec = switch ( fullCount ) {
         default:
         case 1:
            yield new Vector2f();
         case 2:
            if ( curCount == 0 ) {
               yield new Vector2f(-0.20F, -0.15F);
            }
            else {
               yield new Vector2f(0.20F, 0.15F);
            }
         case 3:
            if ( curCount == 0 ) {
               yield new Vector2f(0.05F, 0.25F);
            }
            else if ( curCount == 1 ) {
               yield new Vector2f(-0.25F, -0.15F);
            }
            else {
               yield new Vector2f(0.25F, -0.25F);
            }
         case 4:
            if ( curCount == 0 ) {
               yield new Vector2f(0.20F, 0.25F);
            }
            else if ( curCount == 1 ) {
               yield new Vector2f(-0.25F, 0.20F);
            }
            else if ( curCount == 2 ) {
               yield new Vector2f(0.25F, -0.20F);
            }
            else {
               yield new Vector2f(-0.20F, -0.25F);
            }
      };

      float rotation = switch ( fullCount ) {
         default:
         case 1:
            yield 0;
         case 2:
            if ( curCount == 0 ) {
               yield 190;
            }
            else {
               yield 10;
            }
         case 3:
            if ( curCount == 0 ) {
               yield -20;
            }
            else if ( curCount == 1 ) {
               yield 220;
            }
            else {
               yield 100;
            }
         case 4:
            if ( curCount == 0 ) {
               yield -5;
            }
            else if ( curCount == 1 ) {
               yield 265;
            }
            else if ( curCount == 2 ) {
               yield 85;
            }
            else {
               yield 175;
            }
      };

      poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0, 1, 0, rotation + seededRandom.nextFloat(-10f, 10f)), .5f + translateVec.x(), 0f, .5f + translateVec.y());

      poseStack.translate(translateVec.x(), 0f, translateVec.y());
      poseStack.translate(seededRandom.nextFloat(-0.05f, 0.05f), 0, seededRandom.nextFloat(-0.05f, 0.05f));


   }

   @Override
   public void render( TankardBlockEntity entity, float tickDelta, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay ) {
      int count = (int) entity.getItems().stream().filter(i -> !i.isEmpty()).count();
      poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0, 1, 0, -( 360f / 16f ) * entity.getBlockState().getValue(TankardBlock.ROTATION)), 0.5f, 0, 0.5f);

      Random random = new Random(entity.getBlockPos().asLong());
      for ( int i = 0; i < count; i++ ) {
         BlockState state = boozeItemtoBlockMap().get(entity.getItems().get(i).getItem());
         if ( state != null ) {
            state = state.setValue(TankardBlock.ROTATION, entity.getBlockState().getValue(TankardBlock.ROTATION));
            poseStack.pushPose();

            poseUtil(poseStack, count, i, random);
//            poseStack.popPose();

            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, poseStack, buffer, combinedLight, combinedOverlay, net.minecraftforge.client.model.data.ModelData.EMPTY, null);
            poseStack.popPose();

         }
      }
   }
}

