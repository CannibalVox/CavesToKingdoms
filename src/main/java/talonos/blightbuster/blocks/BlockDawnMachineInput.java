package talonos.blightbuster.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.util.ForgeDirection;
import talonos.blightbuster.BBStrings;
import talonos.blightbuster.BlightBuster;
import talonos.blightbuster.multiblock.BlockMultiblock;
import talonos.blightbuster.tileentity.DawnMachineSpoutTileEntity;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;

import java.util.Random;

public class BlockDawnMachineInput extends BlockMultiblock {

    private IIcon backgroundTop;
    private IIcon backgroundSide;
    private IIcon[] topLeftIcons = new IIcon[6];
    private IIcon[] topRightIcons = new IIcon[6];
    private IIcon[] bottomLeftIcons = new IIcon[6];
    private IIcon[] bottomRightIcons = new IIcon[6];

    public BlockDawnMachineInput() {
        super(Material.wood, BBBlock.dawnMachineMultiblock);

        this.setBlockName(BlightBuster.MODID+"_"+ BBStrings.dawnMachineInputName);
        this.setStepSound(soundTypeWood);
        this.setLightLevel(.875f);
        this.setBlockTextureName("dawnMachineBuffer");
        this.setHardness(10.0F);
        this.setResistance(500.0F);
        GameRegistry.registerBlock(this, this.getUnlocalizedName());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister registry) {
        backgroundTop = registry.registerIcon("thaumcraft:silverwoodtop");
        backgroundSide = registry.registerIcon("thaumcraft:silverwoodside");
        IIcon bufferLayer = registry.registerIcon("blightbuster:dawnMachineBuffer");
        IIcon spoutLayer = registry.registerIcon("blightbuster:dawnMachineSpout");

        topLeftIcons[0] = bufferLayer;
        topLeftIcons[1] = bufferLayer;
        topLeftIcons[2] = spoutLayer;
        topLeftIcons[3] = spoutLayer;
        topLeftIcons[4] = spoutLayer;
        topLeftIcons[5] = bufferLayer;

        topRightIcons[0] = bufferLayer;
        topRightIcons[1] = bufferLayer;
        topRightIcons[2] = spoutLayer;
        topRightIcons[3] = spoutLayer;
        topRightIcons[4] = bufferLayer;
        topRightIcons[5] = spoutLayer;

        bottomLeftIcons[0] = bufferLayer;
        bottomLeftIcons[1] = bufferLayer;
        bottomLeftIcons[2] = spoutLayer;
        bottomLeftIcons[3] = spoutLayer;
        bottomLeftIcons[4] = bufferLayer;
        bottomLeftIcons[5] = bufferLayer;

        bottomRightIcons[0] = bufferLayer;
        bottomRightIcons[1] = bufferLayer;
        bottomRightIcons[2] = spoutLayer;
        bottomRightIcons[3] = spoutLayer;
        bottomRightIcons[4] = bufferLayer;
        bottomRightIcons[5] = bufferLayer;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 1;
    }

    @Override
    public boolean canRenderInPass(int pass) {
        return (pass == 0 || pass == 1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        if (ForgeHooksClient.getWorldRenderPass() == 1) {
            return getInputIcon(transformSide(side, meta), meta);
        }

        if (side == 0 || side == 1)
            return backgroundTop;
        return backgroundSide;
    }

    @Override
    public Item getItemDropped(int meta, Random par2Random, int par3) {
        return Item.getItemFromBlock(ConfigBlocks.blockMagicalLog);
    }

    @Override
    public int damageDropped(int p_149692_1_) { return 1; }

    private IIcon getInputIcon(int side, int meta) {
        int block = meta/4;

        switch(block) {
            case 1:
                return bottomRightIcons[side];
            case 2:
                return topLeftIcons[side];
            case 3:
                return topRightIcons[side];
            default:
                return bottomLeftIcons[side];
        }
    }

    @Override
    public boolean hasTileEntity(int meta)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta)
    {
        return new DawnMachineSpoutTileEntity();
    }

    public Aspect getSpoutAspect(int side, int meta) {
        ForgeDirection sideDir = ForgeDirection.VALID_DIRECTIONS[transformSide(side, meta)];
        int inputBlockIndex = meta/4;

        switch (inputBlockIndex) {
            case 0:
                //West lower block, auram is south, herba is north
                if (sideDir == ForgeDirection.NORTH)
                    return Aspect.PLANT;
                else if (sideDir == ForgeDirection.SOUTH)
                    return Aspect.AURA;
                else
                    return null;
            case 1:
                //East lower block, sano is north, machina is south
                if (sideDir == ForgeDirection.NORTH)
                    return Aspect.HEAL;
                else if (sideDir == ForgeDirection.SOUTH)
                    return Aspect.MECHANISM;
                else
                    return null;
            case 2:
                //West upper block, west is ordo, north is arbor, south is vacuos
                if (sideDir == ForgeDirection.SOUTH)
                    return Aspect.VOID;
                else if (sideDir == ForgeDirection.NORTH)
                    return Aspect.TREE;
                else if (sideDir == ForgeDirection.WEST)
                    return Aspect.ORDER;
                else
                    return null;
            default:
                //East upper block, east is aer, north is ignis, south is cognitio
                if (sideDir == ForgeDirection.NORTH)
                    return Aspect.FIRE;
                else if (sideDir == ForgeDirection.SOUTH)
                    return Aspect.MIND;
                else if (sideDir == ForgeDirection.EAST)
                    return Aspect.AIR;
                else
                    return null;
        }
    }
}
