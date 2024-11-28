package umpaz.brewinandchewin.common.block.entity;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import umpaz.brewinandchewin.common.BnCConfiguration;
import umpaz.brewinandchewin.common.block.KegBlock;
import umpaz.brewinandchewin.common.block.entity.container.KegItemHandler;
import umpaz.brewinandchewin.common.block.entity.container.KegMenu;
import umpaz.brewinandchewin.common.crafting.KegPouringRecipe;
import umpaz.brewinandchewin.common.crafting.KegFermentingRecipe;
import umpaz.brewinandchewin.common.registry.BnCBlockEntityTypes;
import umpaz.brewinandchewin.common.registry.BnCItems;
import umpaz.brewinandchewin.common.registry.BnCRecipeTypes;
import umpaz.brewinandchewin.common.tag.BnCTags;
import umpaz.brewinandchewin.common.utility.BnCTextUtils;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.mixin.accessor.RecipeManagerAccessor;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KegBlockEntity extends SyncedBlockEntity implements MenuProvider, Nameable, RecipeHolder {

    public static final int CONTAINER_SLOT = 4;
    public static final int OUTPUT_SLOT = 5;
    public static final int INVENTORY_SIZE = OUTPUT_SLOT + 1;

    private final ItemStackHandler inventory;
    private final LazyOptional<IItemHandler> inputHandler;
    private final LazyOptional<IItemHandler> outputHandler;
    private final FluidTank fluidTank;
    private final LazyOptional<FluidTank> fluidTankHandler;

    private int fermentTime;
    private int fermentTimeTotal;
    private Component customName;

    public int kegTemperature;

    protected final ContainerData kegData;
    private final Object2IntOpenHashMap<ResourceLocation> usedRecipeTracker;

    private ResourceLocation lastRecipeID;
    private boolean checkNewRecipe;

    public KegBlockEntity(BlockPos pos, BlockState state) {
        super(BnCBlockEntityTypes.KEG.get(), pos, state);
        this.inventory = createHandler();
        this.inputHandler = LazyOptional.of(() -> new KegItemHandler(inventory, Direction.UP));
        this.outputHandler = LazyOptional.of(() -> new KegItemHandler(inventory, Direction.DOWN));
        this.fluidTank = createFluidTank();
//        this.fluidTank.setFluid(new FluidStack(BnCFluids.SOURCE_BEER_FLUID.get(), 1000));
        this.fluidTankHandler = LazyOptional.of(() -> fluidTank);
        this.kegData = createIntArray();
        this.usedRecipeTracker = new Object2IntOpenHashMap<>();
        this.checkNewRecipe = true;

    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        inventory.deserializeNBT(compound.getCompound("Inventory"));
        fluidTank.readFromNBT(compound.getCompound("FluidTank"));
        fermentTime = compound.getInt("FermentTime");
        fermentTimeTotal = compound.getInt("FermentTimeTotal");
        if (compound.contains("CustomName", 8)) {
            customName = Component.Serializer.fromJson(compound.getString("CustomName"));
        }
        CompoundTag compoundRecipes = compound.getCompound("RecipesUsed");
        for (String key : compoundRecipes.getAllKeys()) {
            usedRecipeTracker.put(new ResourceLocation(key), compoundRecipes.getInt(key));
        }
    }

    public static FluidStack getMealFromItem(ItemStack kegStack) {
        if (!kegStack.is(BnCItems.KEG.get())) {
            return FluidStack.EMPTY;
        }

        CompoundTag compound = kegStack.getTagElement("BlockEntityTag");
        if (compound != null) {
            if (compound.contains("FluidTank")) {
                return FluidStack.loadFluidStackFromNBT(compound.getCompound("FluidTank"));
            }

        }

        return FluidStack.EMPTY;
    }

    public FluidStack getOutput() {
        return fluidTank.getFluid();
    }

    public CompoundTag writeMeal(CompoundTag compound) {
        if (getOutput().isEmpty()) return compound;

        ItemStackHandler drops = new ItemStackHandler(INVENTORY_SIZE);
        for (int i = 0; i < INVENTORY_SIZE; ++i) {
            drops.setStackInSlot(i, i == CONTAINER_SLOT ? inventory.getStackInSlot(i) : ItemStack.EMPTY);
        }
        if (customName != null) {
            compound.putString("CustomName", Component.Serializer.toJson(customName));
        }
//      compound.put("Container", drinkContainerStack.serializeNBT());
        compound.put("Inventory", drops.serializeNBT());
        if (!fluidTank.isEmpty()) {
            compound.put("FluidTank", this.fluidTank.writeToNBT(new CompoundTag()));
        }
        return compound;
    }


    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("Inventory", inventory.serializeNBT());
        compound.put("FluidTank", fluidTank.writeToNBT(new CompoundTag()));
        compound.putInt("FermentTime", fermentTime);
        compound.putInt("FermentTimeTotal", fermentTimeTotal);
        if (customName != null) {
            compound.putString("CustomName", Component.Serializer.toJson(customName));
        }
        CompoundTag compoundRecipes = new CompoundTag();
        usedRecipeTracker.forEach((recipeId, craftedAmount) -> compoundRecipes.putInt(recipeId.toString(), craftedAmount));
        compound.put("RecipesUsed", compoundRecipes);
    }

    private CompoundTag writeUpdateTag(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("Inventory", inventory.serializeNBT());
        compound.put("FluidTank", fluidTank.writeToNBT(new CompoundTag()));
        compound.putInt("FermentTime", fermentTime);
        compound.putInt("FermentTimeTotal", fermentTimeTotal);
        return compound;
    }

    public CompoundTag writeDrink(CompoundTag compound) {
        if (customName != null) {
            compound.putString("CustomName", Component.Serializer.toJson(customName));
        }
        if (!fluidTank.isEmpty()) {
            compound.put("FluidTank", this.fluidTank.writeToNBT(new CompoundTag()));
        }
        return compound;
    }


    public static boolean isValidTemp(int kegTemp, int want) {
        return switch (want) {
            case 1 -> kegTemp <= 1;
            case 2 -> kegTemp <= 2;
            case 3 -> kegTemp == 3;
            case 4 -> kegTemp >= 4;
            case 5 -> kegTemp >= 5;
            default -> false; // Consider adding a default case to handle unexpected values
        };
    }

    protected boolean canFerment(KegFermentingRecipe recipe, KegBlockEntity keg) {
        if (!hasInput()) return false;
        if (level == null) return false;
        if (!isValidTemp(keg.getTemperature(), recipe.getTemperature()))
            return false; // make sure the temperature is valid


        if (recipe.getFluidIngredient() == null) { // if the recipe does not require a fluid
            if (keg.fluidTank.getFluidAmount() > 0) return false; // make sure the fluid is empty
        } else {
            if (!keg.fluidTank.getFluid().getFluid().isSame(recipe.getFluidIngredient().getFluid()))
                return false; // make sure the fluid is the same
            if (keg.fluidTank.getFluidAmount() % recipe.getFluidIngredient().getAmount() != 0)
                return false; // make sure the fluid amount is a multiple of the recipe amount
        }


//      if (recipe.getResultFluid()!=null) { // if the recipe has a fluid result
//         FluidStack resultStack = new FluidStack(recipe.getResultFluid(), recipe.getAmount());
//
//
//      }

        return true;
    }

    public static void fermentingTick(Level level, BlockPos pos, BlockState state, KegBlockEntity keg) {
        boolean didInventoryChange = false;

        keg.updateTemperature();

        if (keg.hasInput()) {
            Optional<KegFermentingRecipe> recipe = keg.getMatchingRecipe(new RecipeWrapper(keg.inventory));
            if (recipe.isPresent()) {
                if (keg.canFerment(recipe.get(), keg)) {
                    didInventoryChange = keg.processFermenting(recipe.get(), keg);
                } else {
                    keg.fermentTime = 0;
                }
            } else {
                keg.fermentTime = 0;
            }

        } else if (keg.fermentTime > 0) {
            keg.fermentTime = Mth.clamp(keg.fermentTime - 2, 0, keg.fermentTimeTotal);
        }

        ItemStack out = keg.fluidExtract(keg, keg.inventory.getStackInSlot(CONTAINER_SLOT), keg.inventory.getStackInSlot(OUTPUT_SLOT), keg.inventory.getSlotLimit(OUTPUT_SLOT), false);
        if (!out.isEmpty()) {
            keg.inventory.insertItem(OUTPUT_SLOT, out, false);
            didInventoryChange = true;
        }

        if (didInventoryChange) {
            keg.inventoryChanged();
        }
    }

    private Optional<KegFermentingRecipe> getMatchingRecipe(RecipeWrapper inventoryWrapper) {
        if (level == null) return Optional.empty();

        if (lastRecipeID != null) {
            Recipe<RecipeWrapper> recipe = ((RecipeManagerAccessor) level.getRecipeManager())
                    .getRecipeMap(BnCRecipeTypes.FERMENTING.get())
                    .get(lastRecipeID);
            if (recipe instanceof KegFermentingRecipe kegFerm) {
                boolean hasFluidIngredient = kegFerm.getFluidIngredient() == null || kegFerm.getFluidIngredient().isFluidEqual(this.fluidTank.getFluid());
                if (recipe.matches(inventoryWrapper, level) && hasFluidIngredient) {
                    return Optional.of((KegFermentingRecipe) recipe);
                }
            }
        }

        if (checkNewRecipe) {
            Optional<KegFermentingRecipe> recipe = level.getRecipeManager().getAllRecipesFor(BnCRecipeTypes.FERMENTING.get()).stream().filter(a -> a.matches(inventoryWrapper, level) && (a.getFluidIngredient() == null || a.getFluidIngredient().isFluidEqual(this.fluidTank.getFluid()))).findFirst();
            if (recipe.isPresent()) {
                ResourceLocation newRecipeID = recipe.get().getId();
                if (lastRecipeID != null && !lastRecipeID.equals(newRecipeID)) {
                    fermentTime = 0;
                }
                lastRecipeID = newRecipeID;
                return recipe;
            }
        }

        checkNewRecipe = false;
        return Optional.empty();
    }

    private boolean hasInput() {
        for (int i = 0; i < OUTPUT_SLOT; ++i) {
            if (!inventory.getStackInSlot(i).isEmpty()) return true;
        }
        return false;
    }

    private boolean processFermenting(KegFermentingRecipe recipe, KegBlockEntity keg) {
        if (level == null) return false;

        ++fermentTime;
        fermentTimeTotal = recipe.getFermentTime();
        if (fermentTime < fermentTimeTotal) {
            setChanged();
            return false;
        }


        fermentTime = 0;
        if (recipe.getResultFluid() != null) {
            keg.fluidTank.setFluid(new FluidStack(recipe.getResultFluid(), keg.fluidTank.getFluidAmount()));
            if (keg.level.isClientSide())
                keg.level.playLocalSound(keg.getBlockPos(), SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1, 0.8f, true);

        }

        if (recipe.getResultItem() != null) {
            keg.inventory.insertItem(OUTPUT_SLOT, new ItemStack(recipe.getResultItem(), recipe.getAmount()), false);
        }


        for (int i = 0; i < OUTPUT_SLOT; ++i) {
            ItemStack slotStack = inventory.getStackInSlot(i);
            if (slotStack.hasCraftingRemainingItem()) {
                ejectIngredientRemainder(slotStack.getCraftingRemainingItem());
            }
            if (!slotStack.isEmpty())
                slotStack.shrink(1);
        }
        return true;
    }

    public ItemStack fluidExtract(KegBlockEntity keg, ItemStack slotIn, ItemStack slotOut, int maxTakeAmount, boolean isCreative) {
        Item container = slotIn.getItem();
        ItemStack output = ItemStack.EMPTY;

        Optional<KegPouringRecipe> recipe = keg.getPouringRecipe(container, keg.fluidTank.getFluid());
        boolean changed = false;

        if (slotIn.isEmpty())
            return output;


        if (recipe.isPresent() && (keg.fluidTank.isEmpty() || keg.fluidTank.getFluid().getFluid().isSame(recipe.get().getFluid()))) { // if the recipe is present and the fluid is empty or the same
            if (container.equals(recipe.get().getContainer().getItem()) && // if container is same
                    recipe.get().getAmount() <= keg.fluidTank.getFluidAmount()) { // the amount is LTE the fluid amount
                int containerAmount = Math.min(maxTakeAmount, Mth.clamp(slotIn.getCount(), 0, Math.min(keg.fluidTank.getFluidAmount() / recipe.get().getAmount(), recipe.get().getOutput().getMaxStackSize() - slotOut.getCount())));
                keg.fluidTank.drain(new FluidStack(keg.fluidTank.getFluid(), recipe.get().getAmount() * containerAmount), IFluidHandler.FluidAction.EXECUTE);

                if (output.isEmpty()) {
                    output = recipe.get().getOutput().copyWithCount(containerAmount);
                } else {
                    output.grow(containerAmount);
                }
                changed = true;
            } else if (container.equals(recipe.get().getOutput().getItem()) && // if result is same
                    keg.fluidTank.getFluidAmount() + recipe.get().getAmount() <= keg.fluidTank.getCapacity()) { // if the result can fit in the container
                int containerAmount = Math.min(Math.min(maxTakeAmount, Mth.clamp(slotIn.getCount(), 0, recipe.get().getOutput().getMaxStackSize() - slotOut.getCount())), (keg.fluidTank.getCapacity() - keg.fluidTank.getFluidAmount()) / recipe.get().getAmount());

                keg.fluidTank.fill(new FluidStack(recipe.get().getFluid(), recipe.get().getAmount() * containerAmount), IFluidHandler.FluidAction.EXECUTE);

                if (!isCreative) {
                    if (output.isEmpty()) {
                        output = recipe.get().getContainer().copyWithCount(containerAmount);
                    } else {
                        output.grow(containerAmount);
                    }
                } else {
                    output = slotIn ;
                }
                changed = true;
            }

            if (changed) {
                setChanged();
            }
        }

        // TODO: Test me.
        LazyOptional<IFluidHandlerItem> fluidHandler = slotIn.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
        IFluidHandlerItem iFluidItemHandler = fluidHandler.orElse(null);
        if (fluidHandler.isPresent() && !slotIn.isEmpty()) {
            if (keg.fluidTank.getFluid().isFluidEqual(iFluidItemHandler.getFluidInTank(0)) || keg.fluidTank.getFluid().isEmpty()) {
                int containerAmount = Math.min(maxTakeAmount, Mth.clamp(slotIn.getCount(), 0, Math.min(iFluidItemHandler.getContainer().getCount() / iFluidItemHandler.getTankCapacity(0), iFluidItemHandler.getContainer().getCount() - slotOut.getCount())));
                int amountToDrain = keg.fluidTank.getCapacity() - keg.fluidTank.getFluidAmount();
                int amount = iFluidItemHandler.drain(amountToDrain, IFluidHandler.FluidAction.SIMULATE).getAmount();
                if (amount > 0) {
                    keg.fluidTank.fill(iFluidItemHandler.drain(amountToDrain, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                    if (amount <= amountToDrain) {
                        if (output.isEmpty()) {
                            output = iFluidItemHandler.getContainer().copyWithCount(containerAmount);
                        } else {
                            output.grow(containerAmount);
                        }
                        setChanged();
                    }
                }
            } else if (!keg.fluidTank.getFluid().isEmpty() && iFluidItemHandler.isFluidValid(0, keg.fluidTank.getFluid())) {
                int containerAmount = Math.min(Math.min(maxTakeAmount, Mth.clamp(slotIn.getCount(), 0, iFluidItemHandler.getContainer().getMaxStackSize() - slotOut.getCount())), (keg.fluidTank.getCapacity() - keg.fluidTank.getFluidAmount()) / iFluidItemHandler.getTankCapacity(0));
                int amountToDrain = keg.fluidTank.getFluidAmount();
                int amount = iFluidItemHandler.fill(new FluidStack(keg.fluidTank.getFluid(), amountToDrain), IFluidHandler.FluidAction.SIMULATE);
                if (amount > 0) {
                    iFluidItemHandler.fill(new FluidStack(keg.fluidTank.getFluid(), amountToDrain), IFluidHandler.FluidAction.EXECUTE);
                    keg.fluidTank.drain(amountToDrain, IFluidHandler.FluidAction.EXECUTE);
                    if (amount <= amountToDrain) {
                        if (!isCreative) {
                            if (output.isEmpty()) {
                                output = recipe.get().getContainer().copyWithCount(containerAmount);
                            } else {
                                output.grow(containerAmount);
                            }
                        } else {
                            output = slotIn;
                        }
                        setChanged();

                    }
                }
            }

        }

        return output;
    }

    public Optional<KegPouringRecipe> getPouringRecipe(Item slot, FluidStack fluid) {
        if (level == null) return Optional.empty();
        Optional<KegPouringRecipe> recipe = level.getRecipeManager().getAllRecipesFor(BnCRecipeTypes.KEG_POURING.get())
                .stream()
                .filter(r -> (r.getContainer().getItem() == slot || r.getOutput().getItem() == (slot)) && (fluid.isEmpty() || r.getFluid().isSame(fluid.getFluid())))
                .findFirst();
        if (recipe.isPresent()) {
            lastRecipeID = recipe.get().getId();
            return recipe;
        }
        return Optional.empty();
    }

    public void updateTemperature() {
        ArrayList<BlockState> states = new ArrayList<>();
        int range = 1;
        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    states.add(level.getBlockState(worldPosition.offset(x, y, z)));
                }
            }
        }

        int heat = states.stream().filter(s -> s.is(ModTags.HEAT_SOURCES)).filter(s -> s.hasProperty(BlockStateProperties.LIT)).filter(s -> s.getValue(BlockStateProperties.LIT)).mapToInt(s -> 1).sum();
        heat += states.stream().filter(s -> s.is(ModTags.HEAT_SOURCES)).filter(s -> !s.hasProperty(BlockStateProperties.LIT)).mapToInt(s -> 1).sum();
        BlockState stateBelow = level.getBlockState(worldPosition.below());
        if (stateBelow.is(ModTags.HEAT_CONDUCTORS)) {
            BlockState stateFurtherBelow = level.getBlockState(worldPosition.below(2));
            if (stateFurtherBelow.is(ModTags.HEAT_SOURCES)) {
                if (stateFurtherBelow.hasProperty(BlockStateProperties.LIT)) {
                    if (stateFurtherBelow.getValue(BlockStateProperties.LIT)) {
                        heat += 1;
                    }
                } else {
                    heat += 1;
                }
            }
        }
        int cold = states.stream().filter(s -> s.is(BnCTags.FREEZE_SOURCES)).mapToInt(s -> 1).sum();

        if (BnCConfiguration.KEG_BIOME_TEMP.get()) {
            Holder<Biome> biome = level.getBiome(worldPosition);
            if (biome.isBound()) {
                float biomeTemperature = biome.value().getBaseTemperature();
                if (biomeTemperature <= 0) {
                    cold += 2;
                } else if (biomeTemperature == 2) {
                    heat += 2;
                }
            }
        }

        kegTemperature = heat - cold;

        if (BnCConfiguration.KEG_DIM_TEMP.get() && level.dimensionType().ultraWarm()) {
            kegTemperature += 3;
        }
    }

    public int getTemperature() {
        if (kegTemperature <= -(BnCConfiguration.KEG_COLD.get() + BnCConfiguration.KEG_FREEZING.get())) {
            return 1;
        } else if (kegTemperature <= -BnCConfiguration.KEG_COLD.get()) {
            return 2;
        } else if (kegTemperature < BnCConfiguration.KEG_WARM.get()) {
            return 3;
        } else if (kegTemperature < BnCConfiguration.KEG_WARM.get() + BnCConfiguration.KEG_HOT.get()) {
            return 4;
        } else {
            return 5;
        }
    }

    protected void ejectIngredientRemainder(ItemStack remainderStack) {
        Direction direction = getBlockState().getValue(KegBlock.FACING).getCounterClockWise();
        double x = worldPosition.getX() + 0.5 + (direction.getStepX() * 0.25);
        double y = worldPosition.getY() + 0.7;
        double z = worldPosition.getZ() + 0.5 + (direction.getStepZ() * 0.25);
        ItemUtils.spawnItemEntity(level, remainderStack, x, y, z,
                direction.getStepX() * 0.08F, 0.25F, direction.getStepZ() * 0.08F);
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation recipeID = recipe.getId();
            usedRecipeTracker.addTo(recipeID, 1);
        }
    }

    @Nullable
    @Override
    public Recipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void awardUsedRecipes(Player player, List<ItemStack> items) {
        List<Recipe<?>> usedRecipes = getUsedRecipesAndPopExperience(player.level(), player.position());
        player.awardRecipes(usedRecipes);
        usedRecipeTracker.clear();
    }

    public List<Recipe<?>> getUsedRecipesAndPopExperience(Level level, Vec3 pos) {
        List<Recipe<?>> list = Lists.newArrayList();

        for (Object2IntMap.Entry<ResourceLocation> entry : usedRecipeTracker.object2IntEntrySet()) {
            level.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                splitAndSpawnExperience((ServerLevel) level, pos, entry.getIntValue(), ((KegFermentingRecipe) recipe).getExperience());
            });
        }

        return list;
    }

    private static void splitAndSpawnExperience(ServerLevel level, Vec3 pos, int craftedAmount, float experience) {
        int expTotal = Mth.floor((float) craftedAmount * experience);
        float expFraction = Mth.frac((float) craftedAmount * experience);
        if (expFraction != 0.0F && Math.random() < (double) expFraction) {
            ++expTotal;
        }

        ExperienceOrb.award(level, pos, expTotal);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public FluidTank getFluidTank() {
        return fluidTank;
    }

    public NonNullList<ItemStack> getDroppableInventory() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < INVENTORY_SIZE; ++i) {
            drops.add(inventory.getStackInSlot(i));
        }
        return drops;
    }

    @Override
    public Component getName() {
        return customName != null ? customName : BnCTextUtils.getTranslation("container.keg");
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Override
    @Nullable
    public Component getCustomName() {
        return customName;
    }

    public void setCustomName(Component name) {
        customName = name;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player, Player entity) {
        return new KegMenu(id, player, this, kegData);
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap.equals(ForgeCapabilities.ITEM_HANDLER)) {
            if (side == null || side.equals(Direction.UP)) {
                return inputHandler.cast();
            } else {
                return outputHandler.cast();
            }
        }
        if (cap.equals(ForgeCapabilities.FLUID_HANDLER)) {
            return fluidTankHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        inputHandler.invalidate();
        outputHandler.invalidate();
        fluidTankHandler.invalidate();
    }

    @Override
    public CompoundTag getUpdateTag() {
        return writeUpdateTag(new CompoundTag());
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(INVENTORY_SIZE) {
            @Override
            protected void onContentsChanged(int slot) {
                if (slot >= 0 && slot < OUTPUT_SLOT) {
                    checkNewRecipe = true;
                }
                inventoryChanged();
            }
        };
    }

    private FluidTank createFluidTank() {
        return new FluidTank(3000) {
            @Override
            protected void onContentsChanged() {
                super.onContentsChanged();
                setChanged();
                inventoryChanged();
                checkNewRecipe = true;
            }
        };
    }

    private ContainerData createIntArray() {
        return new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> KegBlockEntity.this.fermentTime;
                    case 1 -> KegBlockEntity.this.fermentTimeTotal;
                    case 2 -> KegBlockEntity.this.getTemperature();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> KegBlockEntity.this.fermentTime = value;
                    case 1 -> KegBlockEntity.this.fermentTimeTotal = value;
                    case 2 -> KegBlockEntity.this.getTemperature();

                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }
}