package talonos.blightbuster.tileentity;

import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyStorage;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
import talonos.blightbuster.BlightBuster;
import talonos.blightbuster.network.BlightbusterNetwork;
import talonos.blightbuster.network.packets.SpawnCleanseParticlesPacket;
import talonos.blightbuster.tileentity.dawnmachine.DawnMachineResource;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IAspectSource;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.blocks.BlockFluxGoo;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.monster.*;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.tiles.TileNode;

import java.util.List;
import java.util.Locale;

public class DawnMachineTileEntity extends TileEntity implements IAspectSource, IAspectContainer, IEnergyReceiver, IEnergyStorage {

    private int currentRf = 0;
    public static final int MAX_RF = 8000;

    private boolean aerIsActive = false;
    private int aerCooldownRemaining = 0;
    public static final int AER_COOLDOWN = 20 * 30;
    private boolean spendAer = false;

    private int ticksSinceLastCleanse = 0;

    private int lastCleanseX = Integer.MAX_VALUE;
    private int lastCleanseZ = Integer.MAX_VALUE;

    private AspectList internalAspectList = new AspectList();

    private boolean hasInitializedChunkloading = false;

    public DawnMachineTileEntity() {

    }

    @Override
    public void updateEntity() {
        if (getWorldObj().isRemote)
            return;

        if (!hasInitializedChunkloading) {
            BlightBuster.instance.chunkLoader.newDawnMachine(getWorldObj(), xCoord, yCoord, zCoord);
            hasInitializedChunkloading = true;
        }

        if (aerCooldownRemaining > 0)
            aerCooldownRemaining--;

        int cleanseLength = haveEnoughFor(DawnMachineResource.MACHINA) ? 7 : 20;

        ticksSinceLastCleanse %= cleanseLength;

        if (ticksSinceLastCleanse == 0) {
            setUpAerRange();

            for (int i = 0; i < 5; i++) {
                getNewCleanseCoords();

                boolean anythingToDo = hasAnythingToCleanseHere();

                if (!anythingToDo) {
                    if (haveEnoughFor(DawnMachineResource.COGNITIO)) {
                        spend(DawnMachineResource.COGNITIO);
                        continue;
                    }
                }

                if (cleanseLength == 7)
                    spend(DawnMachineResource.MACHINA);

                executeCleanse();
                ticksSinceLastCleanse++;
                break;
            }
        } else {
            ticksSinceLastCleanse++;
        }
    }

    protected void executeCleanse() {
        if (spendAer) {
            spendAer = false;
            spend(DawnMachineResource.AER);
        }

        cleanseBiome();

        boolean didUseIgnis = cleanseBlocks();

        if (haveEnoughFor(DawnMachineResource.SANO))
            cleanseMobs();

        BlightbusterNetwork.sendToNearbyPlayers(new SpawnCleanseParticlesPacket(lastCleanseX, lastCleanseZ, didUseIgnis), worldObj.provider.dimensionId, lastCleanseX, 128.0f, lastCleanseZ, 150);
    }

    protected boolean hasAnythingToCleanseHere() {
        BiomeGenBase biome = getWorldObj().getBiomeGenForCoords(lastCleanseX, lastCleanseZ);

        //Can cleanse biome?
        if (biome.biomeID == Config.biomeTaintID ||
                biome.biomeID == Config.biomeEerieID ||
                biome.biomeID == Config.biomeMagicalForestID)
            return true;

        int herbaTopBlock = -1;
        boolean canHerba = haveEnoughFor(DawnMachineResource.HERBA);
        if (canHerba) {
            herbaTopBlock = getWorldObj().getTopSolidOrLiquidBlock(lastCleanseX, lastCleanseZ);
        }

        boolean canIgnis = haveEnoughFor(DawnMachineResource.IGNIS);
        boolean canVacuos = haveEnoughFor(DawnMachineResource.VACUOS);
        boolean canAura = haveEnoughFor(DawnMachineResource.AURAM);
        if (canIgnis || canVacuos || canHerba || canAura) {
            //Are there any taint blocks to cleanse?
            for (int i = 0; i < 256; i++) {
                Block block = getWorldObj().getBlock(lastCleanseX, i, lastCleanseZ);
                int meta = getWorldObj().getBlockMetadata(lastCleanseX, i, lastCleanseZ);

                if (canIgnis && block == ConfigBlocks.blockTaintFibres)
                    return true;
                if (canIgnis && block == ConfigBlocks.blockTaint && meta != 2)
                    return true;
                if (canVacuos && block == ConfigBlocks.blockFluxGoo)
                    return true;
                if (canHerba && i == herbaTopBlock && block == Blocks.dirt)
                    return true;
                if (canAura && block == ConfigBlocks.blockAiry) {
                    if (meta == 0) {
                        TileNode node = (TileNode)getWorldObj().getTileEntity(lastCleanseX, i, lastCleanseZ);
                        if (node != null) {
                            if (node.getNodeType() == NodeType.TAINTED) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        if (haveEnoughFor(DawnMachineResource.SANO)) {
            if (getWorldObj().getEntitiesWithinAABB(ITaintedMob.class, AxisAlignedBB.getBoundingBox(lastCleanseX, 0, lastCleanseZ, lastCleanseX+1, 255, lastCleanseZ+1)).size() > 0)
                return true;
        }

        return false;
    }

    protected void cleanseBiome() {
        BiomeGenBase biome = getWorldObj().getBiomeGenForCoords(lastCleanseX, lastCleanseZ);
        if (biome.biomeID != Config.biomeTaintID &&
                biome.biomeID != Config.biomeEerieID &&
                biome.biomeID != Config.biomeMagicalForestID)
            return;

        BiomeGenBase[] genBiomes = null;
        genBiomes = getWorldObj().getWorldChunkManager().loadBlockGeneratorData(genBiomes, lastCleanseX, lastCleanseZ, 1, 1);
        if (genBiomes != null && genBiomes.length > 0 && genBiomes[0] != null) {
            Utils.setBiomeAt(getWorldObj(), lastCleanseX, lastCleanseZ, genBiomes[0]);
        }
    }

    protected boolean cleanseBlocks() {
        boolean haveUsedIgnis = false;

        // - Ignis is used to cleanse tainted blocks
        // - Vacuos will remove crusted taint blocks when ignis acts on them, instead of converting to flux goo
        // - Herba will convert the top dirt block to grass/top tainted soil block to grass instead of dirt
        // - Auram will convert tained node blocks to normal node blocks
        // - Arbor will detect when we've found a stack of crusted taint 3 high and put a sapling there
        boolean canArbor = haveEnoughFor(DawnMachineResource.ARBOR);
        boolean canHerba = haveEnoughFor(DawnMachineResource.HERBA);

        //Get the y-value of the block herba might want to act on
        int herbaTopBlock = -1;
        if (haveEnoughFor(DawnMachineResource.HERBA))
            herbaTopBlock = getWorldObj().getTopSolidOrLiquidBlock(lastCleanseX, lastCleanseZ);

        //We do the cleanse from top to bottom and every time we find crusted taint, we count how many consecutive
        //ones we cleanse.  When we find something that isn't crusted taint, after cleansing it, we plant a sapling on
        //it if there was enough crusted taint above it
        int consecutiveCrustedTaint = 0;
        for (int y = 255; y >= 0; y--) {
            Block block = getWorldObj().getBlock(lastCleanseX, y, lastCleanseZ);
            int meta = getWorldObj().getBlockMetadata(lastCleanseX, y, lastCleanseZ);

            boolean thisIsCrustedTaint = (block == ConfigBlocks.blockTaint && meta == 0);

            if (!thisIsCrustedTaint && consecutiveCrustedTaint > 0 && getWorldObj().isAirBlock(lastCleanseX, y, lastCleanseZ)) {
                List swarmerSpawns = getWorldObj().getEntitiesWithinAABB(EntityTaintSporeSwarmer.class, AxisAlignedBB.getBoundingBox(lastCleanseX, y, lastCleanseZ, lastCleanseX + 1, y + 1, lastCleanseZ + 1));
                if (swarmerSpawns.size() > 0)
                    thisIsCrustedTaint = true;
            }

            boolean plantSaplingAbove = false;
            if (thisIsCrustedTaint && haveEnoughFor(DawnMachineResource.IGNIS) && haveEnoughFor(DawnMachineResource.VACUOS))
                consecutiveCrustedTaint++;
            else {
                if (canArbor && !thisIsCrustedTaint && consecutiveCrustedTaint >= 3)
                    plantSaplingAbove = true;
                else
                    consecutiveCrustedTaint = 0;
            }

            haveUsedIgnis = cleanseSingleBlock(lastCleanseX, y, lastCleanseZ, block, meta, canHerba && y == herbaTopBlock) || haveUsedIgnis;

            if (plantSaplingAbove) {
                canArbor = spendAndCheck(DawnMachineResource.ARBOR);

                for (Object swarmerObj : getWorldObj().getEntitiesWithinAABB(EntityTaintSporeSwarmer.class, AxisAlignedBB.getBoundingBox(lastCleanseX, y+1, lastCleanseZ, lastCleanseX + 1, y + 1 + consecutiveCrustedTaint, lastCleanseZ + 1))) {
                    ((Entity)swarmerObj).setDead();
                }

                BiomeGenBase biome = getWorldObj().getBiomeGenForCoords(lastCleanseX, lastCleanseZ);
                String biomeName = biome.biomeName.toLowerCase(Locale.ENGLISH);

                //Default to oak
                int treeType = 0;
                if (biomeName.contains("jungle"))
                    treeType = 3; //Jungle tree
                else if (biomeName.contains("taiga") || biomeName.contains("tundra"))
                    treeType = 2; //Spruce trees
                else if (biomeName.contains("birch"))
                    treeType = 1; //Birch trees
                else if (biomeName.contains("savanna"))
                    treeType = 4; //Acacia trees
                else if (biomeName.contains("roof"))
                    treeType = 5;

                getWorldObj().setBlock(lastCleanseX, y + 1, lastCleanseZ, Blocks.sapling, treeType, 3);
                consecutiveCrustedTaint = 0;
            }
        }

        return haveUsedIgnis;
    }

    private boolean cleanseSingleBlock(int x, int y, int z, Block block, int meta, boolean doHerbaCheck) {
        boolean canIgnis = haveEnoughFor(DawnMachineResource.IGNIS);
        boolean canVacuos = haveEnoughFor(DawnMachineResource.VACUOS);

        //Cleanse crusted taint
        if (canIgnis && block == ConfigBlocks.blockTaint && meta == 0) {
            spend(DawnMachineResource.IGNIS);

            Block replaceBlock = ConfigBlocks.blockFluxGoo;
            int replaceMeta = ((BlockFluxGoo)ConfigBlocks.blockFluxGoo).getQuanta();

            if (haveEnoughFor(DawnMachineResource.VACUOS)) {
                spend(DawnMachineResource.VACUOS);
                replaceBlock = Blocks.air;
                replaceMeta = 0;
            }

            getWorldObj().setBlock(lastCleanseX, y, lastCleanseZ, replaceBlock, replaceMeta, 3);
            return true;
        }

        //Cleanse tainted soil
        if (canIgnis && block == ConfigBlocks.blockTaint && meta == 1) {
            spend(DawnMachineResource.IGNIS);

            Block replaceBlock = Blocks.dirt;
            if (doHerbaCheck) {
                spend(DawnMachineResource.HERBA);
                replaceBlock = Blocks.grass;
            }

            getWorldObj().setBlock(lastCleanseX, y, lastCleanseZ, replaceBlock);
            return true;
        }

        //Cleanse taint fibres
        if (canIgnis && block == ConfigBlocks.blockTaintFibres) {
            spend(DawnMachineResource.IGNIS);
            getWorldObj().setBlock(lastCleanseX, y, lastCleanseZ, Blocks.air);
            return true;
        }

        if (canVacuos && block == ConfigBlocks.blockFluxGoo) {
            spend(DawnMachineResource.VACUOS);

            getWorldObj().setBlock(lastCleanseX, y, lastCleanseZ, Blocks.air);
            return false;
        }

        if (doHerbaCheck && block == Blocks.dirt) {
            spend(DawnMachineResource.HERBA);
            getWorldObj().setBlock(lastCleanseX, y, lastCleanseZ, Blocks.grass);
            return false;
        }

        if (haveEnoughFor(DawnMachineResource.AURAM) && block == ConfigBlocks.blockAiry && meta == 0) {
            TileNode node = (TileNode)getWorldObj().getTileEntity(lastCleanseX, y, lastCleanseZ);
            if (node != null && node.getNodeType() == NodeType.TAINTED) {
                spend(DawnMachineResource.AURAM);
                node.setNodeType(NodeType.NORMAL);
            }
            return false;
        }

        return false;
    }

    protected void cleanseMobs() {
        List entities = getWorldObj().getEntitiesWithinAABB(ITaintedMob.class, AxisAlignedBB.getBoundingBox(lastCleanseX, 0, lastCleanseZ, lastCleanseX + 1, 256, lastCleanseZ + 1));

        for (Object entityObj : entities) {
            if (entityObj instanceof EntityTaintSheep)
            {
                spend(DawnMachineResource.SANO);
                cleanseSingleMob((Entity)entityObj, new EntitySheep(getWorldObj()));
            } else if (entityObj instanceof EntityTaintChicken) {
                spend(DawnMachineResource.SANO);
                cleanseSingleMob((Entity)entityObj, new EntityChicken(getWorldObj()));
            } else if (entityObj instanceof EntityTaintCow) {
                spend(DawnMachineResource.SANO);
                cleanseSingleMob((Entity)entityObj, new EntityCow(getWorldObj()));
            } else if (entityObj instanceof EntityTaintPig) {
                spend(DawnMachineResource.SANO);
                cleanseSingleMob((Entity)entityObj, new EntityPig(getWorldObj()));
            } else if (entityObj instanceof EntityTaintVillager) {
                spend(DawnMachineResource.SANO);
                cleanseSingleMob((Entity)entityObj, new EntityVillager(getWorldObj()));
            } else if (entityObj instanceof EntityTaintCreeper) {
                spend(DawnMachineResource.SANO);
                cleanseSingleMob((Entity)entityObj, new EntityCreeper(getWorldObj()));
            }

            if (!haveEnoughFor(DawnMachineResource.SANO))
                return;
        }
    }

    private void cleanseSingleMob(Entity tainted, EntityLivingBase cleansed) {
        //new entity copies original entity location
        cleansed.copyLocationAndAnglesFrom(tainted);
        //original entity spawns new entity into the world
        tainted.worldObj.spawnEntityInWorld(cleansed);
        //new entity removes the old entity
        cleansed.worldObj.removeEntity(tainted);
    }

    public boolean spendAndCheck(DawnMachineResource resource) {
        spend(resource);
        return haveEnoughFor(resource);
    }

    public boolean haveEnoughFor(DawnMachineResource resource) {
        int energyCost = resource.getEnergyCost();

        boolean halfEssentia = false;
        if (currentRf >= energyCost)
            halfEssentia = true;

        int cost = resource.getAspectCost();
        if (halfEssentia)
            cost /= 2;

        return internalAspectList.getAmount(resource.getAspect()) >= cost;
    }

    public void spend(DawnMachineResource resource) {
        if (!haveEnoughFor(resource))
            return;

        int energyCost = resource.getEnergyCost();
        boolean halfEssentia = false;
        if (currentRf >= energyCost) {
            halfEssentia = true;
            currentRf -= energyCost;
        }

        int cost = resource.getAspectCost();
        if (halfEssentia)
            cost /= 2;

        internalAspectList.remove(resource.getAspect(), cost);
        signalUpdate();
    }

    private void setUpAerRange() {
        boolean aerChunkLoadingActive = BlightBuster.instance.chunkLoader.getAerStatus(getWorldObj(), xCoord, yCoord, zCoord);
        boolean canAffordAer = haveEnoughFor(DawnMachineResource.AER);

        if (canAffordAer != this.aerIsActive) {
            if (!canAffordAer || aerCooldownRemaining <= 0) {
                this.aerIsActive = canAffordAer;

                if (!canAffordAer)
                    this.aerCooldownRemaining = AER_COOLDOWN;
            }
        }

        if (this.aerIsActive != aerChunkLoadingActive) {
            BlightBuster.instance.chunkLoader.changeAerStatus(getWorldObj(), xCoord, yCoord, zCoord, this.aerIsActive);
        }
    }

    private void getNewCleanseCoords() {
        int chunkX = xCoord / 16;
        int chunkZ = zCoord / 16;
        int xInChunk = xCoord % 16;
        int zInChunk = zCoord % 16;

        int minChunkX = chunkX - 1;
        int minChunkZ = chunkZ - 1;
        int maxChunkX = chunkX + 2;
        int maxChunkZ = chunkZ + 2;

        int freeMinChunkX = minChunkX;
        int freeMinChunkZ = minChunkZ;
        int freeMaxChunkX = maxChunkX;
        int freeMaxChunkZ = maxChunkZ;

        if (haveEnoughFor(DawnMachineResource.AER)) {
            minChunkX -= 2;
            minChunkZ -= 2;
            maxChunkX += 2;
            maxChunkZ += 2;
        }

        if (xInChunk < 8) {
            minChunkX--;
            maxChunkX--;
        }

        if (zInChunk < 8) {
            minChunkZ--;
            maxChunkZ--;
        }

        if (haveEnoughFor(DawnMachineResource.ORDO)) {
            spend(DawnMachineResource.ORDO);
            generateScanlineCoords(minChunkX * 16, minChunkZ * 16, maxChunkX * 16 + 15, maxChunkZ * 16 + 15);
        } else
            generateRandomCoords(minChunkX * 16, minChunkZ * 16, maxChunkX * 16 + 15, maxChunkZ * 16 + 15);

        int cleanseChunkX = lastCleanseX / 16;
        int cleanseChunkZ = lastCleanseZ / 16;

        if (cleanseChunkX < freeMinChunkX || cleanseChunkZ < freeMinChunkZ || cleanseChunkX > freeMaxChunkX || cleanseChunkZ > freeMaxChunkZ)
            spendAer = true;
    }

    private void generateScanlineCoords(int minX, int minZ, int maxX, int maxZ) {
        if (lastCleanseX == Integer.MAX_VALUE || lastCleanseX < minX)
            lastCleanseX = minX-1;
        if (lastCleanseZ == Integer.MAX_VALUE || lastCleanseZ < minZ)
            lastCleanseZ = minZ;

        lastCleanseX++;

        if (lastCleanseX > maxX) {
            lastCleanseX = minX;
            lastCleanseZ++;
        }

        if (lastCleanseZ > maxZ) {
            lastCleanseZ = minZ;
        }
    }

    private void generateRandomCoords(int minX, int minZ, int maxX, int maxZ) {
        int diffX = maxX - minX + 1;
        int diffZ = maxZ - minZ + 1;

        lastCleanseX = getWorldObj().rand.nextInt(diffX) + minX;
        lastCleanseZ = getWorldObj().rand.nextInt(diffZ) + minZ;
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
            signalUpdate();
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

        signalUpdate();

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

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        readCustomNBT(tag);
    }

    private void readCustomNBT(NBTTagCompound tag) {
        internalAspectList.readFromNBT(tag.getCompoundTag("Essentia"));
        currentRf = tag.getInteger("CurrentRF");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        writeCustomNBT(tag);
    }

    private void writeCustomNBT(NBTTagCompound tag) {
        NBTTagCompound essentia = new NBTTagCompound();
        internalAspectList.writeToNBT(essentia);
        tag.setTag("Essentia", essentia);

        tag.setInteger("CurrentRF", currentRf);
    }

    protected void signalUpdate() {
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        markDirty();
    }

    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeCustomNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, -999, nbttagcompound);
    }

    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        super.onDataPacket(net, pkt);
        readCustomNBT(pkt.func_148857_g());
    }
}
