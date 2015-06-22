package talonos.blightbuster.tileentity.dawnmachine;

import thaumcraft.api.aspects.Aspect;

public enum DawnMachineResource {

    SANO(Aspect.HEAL, 128, 2, 4096, 63),
    IGNIS(Aspect.FIRE, 512, 2, 16384, 16),
    AER(Aspect.AIR, 128, 2, 4096, 63),
    COGNITIO(Aspect.MIND, 32, 2, 1024, 250),
    MACHINA(Aspect.MECHANISM, 128, 2, 4096, 63),
    AURAM(Aspect.AURA, 1, 16, 32, 64000),
    VACUOS(Aspect.VOID, 128, 2, 4096, 63),
    ORDO(Aspect.ORDER, 128, 2, 4096, 63),
    ARBOR(Aspect.TREE, 64, 2, 2048, 125),
    HERBA(Aspect.PLANT, 128, 2, 4096, 63);

    private Aspect aspect;
    private int valueMultiplier;
    private int cost;
    private int maximumValue;
    private int rfDiscountCost;

    DawnMachineResource(Aspect aspect, int valueMultiplier, int cost, int maximumValue, int rfDiscountCost) {
        this.aspect = aspect;
        this.valueMultiplier = valueMultiplier;
        this.cost = cost;
        this.maximumValue = maximumValue;
        this.rfDiscountCost = rfDiscountCost;
    }

    public Aspect getAspect() { return aspect; }
    public int getValueMultiplier() { return valueMultiplier; }
    public int getAspectCost() { return cost; }
    public int getMaximumValue() { return maximumValue; }
    public int getEnergyCost() { return rfDiscountCost; }

    public static DawnMachineResource getResourceFromAspect(Aspect aspect) {
        if (aspect == Aspect.HEAL)
            return SANO;

        if (aspect == Aspect.FIRE)
            return IGNIS;

        if (aspect == Aspect.AIR)
            return AER;

        if (aspect == Aspect.MIND)
            return COGNITIO;

        if (aspect == Aspect.MECHANISM)
            return MACHINA;

        if (aspect == Aspect.AURA)
            return AURAM;

        if (aspect == Aspect.VOID)
            return VACUOS;

        if (aspect == Aspect.ORDER)
            return ORDO;

        if (aspect == Aspect.TREE)
            return ARBOR;

        if (aspect == Aspect.PLANT)
            return HERBA;

        return null;
    }
}
