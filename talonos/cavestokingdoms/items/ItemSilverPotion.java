package talonos.cavestokingdoms.items;

import talonos.cavestokingdoms.lib.DEFS;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSilverPotion extends Item
{
	
		private String name = "silverPotion";

		public ItemSilverPotion() 
		{
			setUnlocalizedName(DEFS.MODID + "_" + name );
			GameRegistry.registerItem(this, name);
			setCreativeTab(CreativeTabs.tabMaterials);
			setTextureName(DEFS.MODID + ":" + name);
		}
		
       
       public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
       {
           if (!par3EntityPlayer.capabilities.isCreativeMode)
           {
               --par1ItemStack.stackSize;
           }

           par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

           if (!par2World.isRemote)
           {
               par2World.spawnEntityInWorld(new EntitySilverPotion(par2World, par3EntityPlayer));
           }

           return par1ItemStack;
       }
}