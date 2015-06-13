package talonos.blightbuster.tileentity;

import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyStorage;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import talonos.blightbuster.tileentity.dawnmachine.DawnMachineResource;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectSource;
import thaumcraft.api.aspects.IEssentiaTransport;

public class DawnMachineTileEntity extends TileEntity implements IAspectSource, IEnergyReceiver, IEnergyStorage {

    private int currentRf = 0;
    public static final int MAX_RF = 8000;
    private AspectList internalAspectList = new AspectList();

    public DawnMachineTileEntity() {

    }

    @Override
    public AspectList getAspects() {
        AspectList aspectList = new AspectList();

        for (DawnMachineResource resource : DawnMachineResource.values()) {
            int value = internalAspectList.getAmount(resource.getAspect());
            value /= resource.getValueMultiplier();
            aspectList.add(resource.getAspect(), value);
        }

        return aspectList;
    }

    @Override
    public void setAspects(AspectList aspectList) {
        //Ehhhhh
    }

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        return DawnMachineResource.getResourceFromAspect(aspect) != null;
    }

    @Override
    public int addToContainer(Aspect aspect, int i) {
        DawnMachineResource relevantResource = DawnMachineResource.getResourceFromAspect(aspect);

        if (relevantResource == null)
            return i;

        int currentValue = internalAspectList.getAmount(aspect);
        int remainingRoom = relevantResource.getMaximumValue() - currentValue;

        int essentiaRemaining = remainingRoom / relevantResource.getValueMultiplier();

        if (essentiaRemaining > 0) {
            int essentiaToMove = Math.min(i, essentiaRemaining);
            i -= essentiaToMove;
            internalAspectList.add(aspect, essentiaToMove * relevantResource.getValueMultiplier());
        }

        return i;
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int i) {

        //This container is input-only, we're working here!
        return false;
    }

    @Override
    public boolean takeFromContainer(AspectList aspectList) {
        //This contianer is input-only, we're working here!
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int i) {
        if (i == 0)
            return true;

        DawnMachineResource relevantResource = DawnMachineResource.getResourceFromAspect(aspect);

        if (relevantResource == null)
            return false;

        int currentValue = internalAspectList.getAmount(aspect) / relevantResource.getValueMultiplier();

        return (currentValue >= i);
    }

    @Override
    public boolean doesContainerContain(AspectList aspectList) {
        boolean successful = true;
        for (Aspect aspect : aspectList.getAspects())
            successful = doesContainerContainAmount(aspect, aspectList.getAmount(aspect)) && successful;

        return successful;
    }

    @Override
    public int containerContains(Aspect aspect) {
        DawnMachineResource relevantResource = DawnMachineResource.getResourceFromAspect(aspect);

        if (relevantResource == null)
            return 0;

        return internalAspectList.getAmount(aspect) / relevantResource.getValueMultiplier();
    }

    public boolean needsMore(Aspect aspect) {
        DawnMachineResource relevantResource = DawnMachineResource.getResourceFromAspect(aspect);

        if (relevantResource == null)
            return false;

        return (relevantResource.getMaximumValue() - internalAspectList.getAmount(aspect)) >= relevantResource.getValueMultiplier();
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        if (from != ForgeDirection.DOWN)
            return 0;

        int room = MAX_RF - currentRf;

        int actualReceive = Math.min(maxReceive, room);

        if (!simulate)
            currentRf += actualReceive;

        return actualReceive;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        if (from != ForgeDirection.DOWN)
            return 0;

        return currentRf;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        if (from != ForgeDirection.DOWN)
            return 0;

        return MAX_RF;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return (from == ForgeDirection.DOWN);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return receiveEnergy(ForgeDirection.DOWN, maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return currentRf;
    }

    @Override
    public int getMaxEnergyStored() {
        return MAX_RF;
    }
}
