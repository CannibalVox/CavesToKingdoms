package talonos.blightbuster.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import talonos.blightbuster.BBStrings;
import talonos.blightbuster.BlightBuster;
import thaumcraft.common.Thaumcraft;

public class BlockDawnMachineDummy extends Block {

    private IIcon backgroundTop;
    private IIcon backgroundSide;
    private IIcon[] bufferLayer = new IIcon[6];

    protected BlockDawnMachineDummy() {
        super(Material.iron);

        this.setBlockName(BlightBuster.MODID+"_"+ BBStrings.dawnMachineBufferName);
        this.setStepSound(soundTypeWood);
        this.setLightLevel(.875f);
        this.setBlockTextureName("dawnMachineBuffer");
        GameRegistry.registerBlock(this, this.getUnlocalizedName());
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister registry) {
        backgroundTop = registry.registerIcon("thaumcraft:silverwoodtop");
        backgroundSide = registry.registerIcon("thaumcraft:silverwoodside");
        IIcon buffer = registry.registerIcon("blightbuster:dawnMachineBuffer");

        bufferLayer[0] = buffer;
        bufferLayer[1] = buffer;
        bufferLayer[2] = buffer;
        bufferLayer[3] = buffer;
        bufferLayer[4] = buffer;
        bufferLayer[5] = buffer;
    }

    public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6) {
        if (par5 == 1) {
            if (par1World.isRemote) {
                Thaumcraft.proxy.blockSparkle(par1World, par2, par3, par4, 16736256, 5);
            }
            return true;
        }
        return super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
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
    public IIcon getIcon(int side, int meta) {
        if (ForgeHooksClient.getWorldRenderPass() == 1)
            return bufferLayer[side];

        if (side == 0 || side == 1)
            return backgroundTop;
        return backgroundSide;
    }
}
