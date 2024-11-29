package umpaz.brewinandchewin.common.utility;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class BnCRecipeWrapper extends RecipeWrapper {
    private final IFluidHandler tank;

    public BnCRecipeWrapper(IItemHandlerModifiable itemHandler, IFluidHandler fluidHandler) {
        super(itemHandler);
        this.tank = fluidHandler;
    }

    public FluidStack getFluid(int slot)
    {
        return tank.getFluidInTank(slot);
    }

    public void fillFluid(Fluid fluid, int amount) {
        tank.fill(new FluidStack(fluid, amount), IFluidHandler.FluidAction.EXECUTE);
    }

    public FluidStack drainFluid(int slot, int amount) {
        FluidStack stack = tank.getFluidInTank(slot);
        return stack.isEmpty() ? FluidStack.EMPTY : tank.drain(amount, IFluidHandler.FluidAction.EXECUTE);
    }


    public FluidStack drainFluidNoUpdate(int index, int amount) {
        FluidStack stack = tank.getFluidInTank(index);
        return stack.isEmpty() ? FluidStack.EMPTY : tank.drain(amount, IFluidHandler.FluidAction.SIMULATE);
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < tank.getTanks(); ++i) {
            if (!tank.getFluidInTank(i).isEmpty())
                return false;
        }
        return super.isEmpty();
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < tank.getTanks(); ++i) {
            tank.drain(tank.getTankCapacity(i), IFluidHandler.FluidAction.EXECUTE);
        }
    }
}
