package talonos.blightbuster.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import talonos.blightbuster.tileentity.dawnmachine.DawnMachineResource;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectSource;
import thaumcraft.api.aspects.IEssentiaTransport;

public class DawnMachineTileEntity extends TileEntity implements IAspectSource {

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
}
