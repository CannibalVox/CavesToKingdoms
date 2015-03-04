package talonos.cavestokingdoms.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class CtKGuiHandler implements IGuiHandler 
{
	public CtKGuiHandler()
	{
		
	}
	
    /**
     * Gets the server element.
     * This means, do something server side, when this ID gets called.
     */
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }
 
    /**
     * Gets the client element.
     * This means, do something client side, when this ID gets called.
     */
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if(ID == 0)
            return new AltarGui();
        return null;
    }
}
