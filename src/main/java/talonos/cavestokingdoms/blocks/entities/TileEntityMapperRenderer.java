package talonos.cavestokingdoms.blocks.entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import talonos.cavestokingdoms.BiomeMapColors;

@SideOnly(Side.CLIENT)
public class TileEntityMapperRenderer extends TileEntitySpecialRenderer
{
    private final ResourceLocation location;
    private final DynamicTexture bufferedImage;
    private int[] intArray = new int[176*180];
    
    public TileEntityMapperRenderer()
    {
        this.bufferedImage = new DynamicTexture(176, 180);
        this.location = Minecraft.getMinecraft().renderEngine.getDynamicTextureLocation("islandmapper", this.bufferedImage);
        this.intArray = this.bufferedImage.getTextureData();

        for (int i = 0; i < this.intArray.length; ++i)
        {
            this.intArray[i] = 0;
        }
    }

    public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float par8)
    {
    	TileEntityIslandMapper mapper = (TileEntityIslandMapper)entity;
        for (int i = 0; i < 176*180; ++i)
        {
            byte b0 = mapper.mapData[i];
            int b = b0 & 0xFF;
            this.intArray[i] = BiomeMapColors.colors[b];
        }

        this.bufferedImage.updateDynamicTexture();
        
        this.bindTexture(location);

        Tessellator tessellator = Tessellator.instance;
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        //GL11.glEnable(GL11.GL_BLEND);
        //GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0, 0, -.1, 0, 1);
        tessellator.addVertexWithUV(0, 1, -.1, 0, 0);
        tessellator.addVertexWithUV(1, 1, -.1, 1, 0);
        tessellator.addVertexWithUV(1, 0, -.1, 1, 1);

        //tessellator.addVertexWithUV(0, 0, 0, 0, 0);
        //tessellator.addVertexWithUV(1, 0, 0, 1, 0);
        //tessellator.addVertexWithUV(1, 1, 0, 1, 1);
        //tessellator.addVertexWithUV(0, 1, 0, 0, 1);

        tessellator.draw();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        //GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
