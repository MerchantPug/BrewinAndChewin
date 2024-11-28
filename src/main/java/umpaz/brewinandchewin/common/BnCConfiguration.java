package umpaz.brewinandchewin.common;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class BnCConfiguration {
   public static ForgeConfigSpec COMMON_CONFIG;
   public static ForgeConfigSpec CLIENT_CONFIG;

   public static final String CATEGORY_SETTINGS = "settings";
   public static ForgeConfigSpec.IntValue LEVEL_CHAT_SCRAMBLE;
   public static ForgeConfigSpec.IntValue LEVEL_SIGN_SCRAMBLE;
   public static ForgeConfigSpec.IntValue LEVEL_NAME_SCRAMBLE;
   public static ForgeConfigSpec.IntValue LEVEL_INTOXICATED;

   public static final String CATEGORY_RECIPE_BOOK = "recipe_book";
   public static ForgeConfigSpec.BooleanValue ENABLE_RECIPE_BOOK_KEG;

   public static final String CATEGORY_KEG = "keg";
   public static ForgeConfigSpec.IntValue KEG_CAPACITY;
   public static ForgeConfigSpec.IntValue KEG_FREEZING;
   public static ForgeConfigSpec.IntValue KEG_COLD;
   public static ForgeConfigSpec.IntValue KEG_WARM;
   public static ForgeConfigSpec.IntValue KEG_HOT;
   public static ForgeConfigSpec.BooleanValue KEG_BIOME_TEMP;
   public static ForgeConfigSpec.BooleanValue KEG_DIM_TEMP;


   // CLIENT
   public static final String CATEGORY_CLIENT = "client";

   public static ForgeConfigSpec.BooleanValue INTOXICATED_HEALTH_OVERLAY;
   public static ForgeConfigSpec.BooleanValue CHAT_SCRAMBLE;
   public static ForgeConfigSpec.BooleanValue SIGN_SCRAMBLE;
   public static ForgeConfigSpec.BooleanValue NAME_SCRAMBLE;
   public static ForgeConfigSpec.BooleanValue TIPSY_SCREEN_EFFECT;


   static {
      ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

      COMMON_BUILDER.comment("Game Settings").push(CATEGORY_SETTINGS);

      LEVEL_CHAT_SCRAMBLE = COMMON_BUILDER.comment("At what level of Tipsy should the chat scramble?")
              .defineInRange("levelChatScramble", 3, 1, 10);
      LEVEL_SIGN_SCRAMBLE = COMMON_BUILDER.comment("At what level of Tipsy should signs scramble?")
              .defineInRange("levelSignScramble", 3, 1, 10);
      LEVEL_NAME_SCRAMBLE = COMMON_BUILDER.comment("At what level of Tipsy should nametags scramble?")
              .defineInRange("levelNameScramble", 3, 1, 10);
      LEVEL_INTOXICATED = COMMON_BUILDER.comment("At what level of Tipsy should the player get the Intoxicated effect?")
              .defineInRange("levelIntoxicated", 3, 1, 10);

      COMMON_BUILDER.comment("Keg").push(CATEGORY_KEG);
      KEG_CAPACITY = COMMON_BUILDER.comment("How much fluid (in millibuckets) can the Keg hold?")
              .defineInRange("kegCapacity", 3000, 1, 10000);
      KEG_FREEZING = COMMON_BUILDER.comment("How many cold blocks (added to cold blocks) are required for a freezing temperature in the Keg?")
              .defineInRange("kegFreezing", 2, 1, 10);
      KEG_COLD = COMMON_BUILDER.comment("How many cold blocks are required for a cold temperature in the Keg?")
              .defineInRange("kegCold", 1, 1, 10);
      KEG_WARM = COMMON_BUILDER.comment("How many cold blocks are required for a warm temperature in the Keg?")
              .defineInRange("kegWarm", 1, 1, 10);
      KEG_HOT = COMMON_BUILDER.comment("How many cold blocks (added to warm blocks) are required for a hot temperature in the Keg?")
              .defineInRange("kegHot", 2, 1, 10);
      KEG_BIOME_TEMP = COMMON_BUILDER.comment("Should the biome temperature influence the temperature in the Keg?")
              .define("kegBiomeTemp", true);
      KEG_DIM_TEMP = COMMON_BUILDER.comment("Should the dimension temperature influence the temperature in the Keg?")
              .define("kegDimTemp", true);
      COMMON_BUILDER.pop();

      COMMON_BUILDER.comment("Recipe book").push(CATEGORY_RECIPE_BOOK);
      ENABLE_RECIPE_BOOK_KEG = COMMON_BUILDER.comment("Should the Keg have a Recipe Book available on its interface?")
              .define("enableRecipeBookKeg", true);
      COMMON_BUILDER.pop();

      COMMON_BUILDER.pop();

      COMMON_CONFIG = COMMON_BUILDER.build();

      ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

      CLIENT_BUILDER.comment("Client settings").push(CATEGORY_CLIENT);
      INTOXICATED_HEALTH_OVERLAY = CLIENT_BUILDER.comment("Should the health bar have a yellow overlay when the player has the Intoxicated effect?")
              .define("intoxicatedHealthOverlay", true);
      CHAT_SCRAMBLE = CLIENT_BUILDER.comment("Should the chat scramble when the player has the Tipsy effect?")
              .define("scrambleChat", true);
      NAME_SCRAMBLE = CLIENT_BUILDER.comment("Should other player's nametags scramble when the player has the Tipsy effect?")
              .define("scrambleName", true);
      SIGN_SCRAMBLE = CLIENT_BUILDER.comment("Should signs scramble when the player has the Tipsy effect?")
              .define("scrambleSign", true);

      TIPSY_SCREEN_EFFECT = CLIENT_BUILDER.comment("Should the screen wobble when the player has the Tipsy effect?")
              .define("tipsyScreenEffect", true);
      CLIENT_BUILDER.pop();

      CLIENT_CONFIG = CLIENT_BUILDER.build();
   }
}
