package talonos.cavestokingdoms.client.pages;

//import iguanaman.iguanatweakstconstruct.override.XPAdjustmentMap;
import mantle.client.pages.BookPage;
import mantle.lib.client.MantleClientRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.*;
import org.w3c.dom.*;

import cpw.mods.fml.common.registry.GameRegistry;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.PatternBuilder;
import tconstruct.library.tools.ToolMaterial;
import tconstruct.library.util.HarvestLevels;
//import iguanaman.iguanatweakstconstruct.util.HarvestLevels;

public class ExtMaterialsPage extends BookPage
{
	//The title of the page
    String title = "";
    
    //The material this page represents.
    ToolMaterial material;
    
    //Itemstacks representing the icons we'll end up drawing on the page.
    ItemStack[] icons;
    Block[] ore;
    int oreMetadata = 0;
    int iconMetadata = 0;
    
    //Itemstacks representing the icons we'll end up drawing on the page.
    String description;

    //I think this initializes the instance of a page, given an XML page element.
    @Override
    public void readPageFromXML (Element element)
    {
    	//System.out.println("Reading page...");
    	//Ingot
        icons = new ItemStack[1];
        ore = new Block[1];
        
        NodeList nodes = element.getElementsByTagName("title");
        if (nodes != null)
        {
            title = nodes.item(0).getTextContent();
        }

        nodes = element.getElementsByTagName("text");
        if (nodes != null)
        {
            description = nodes.item(0).getTextContent();
        }

        //Get the icon
        icons[0] = new ItemStack(Items.rotten_flesh);
        
        nodes = element.getElementsByTagName("icon");
        if (nodes != null)
        {
        	String total = nodes.item(0).getTextContent();
            String mod = total.substring(0, total.indexOf(':'));
            String itemName = total.substring(total.indexOf(':')+1);
            int secondColonPosition = itemName.indexOf(':');
            iconMetadata = 0;
            //System.out.println("mod: "+mod+", itemName: "+itemName);
            if (secondColonPosition != -1)
            {
            	iconMetadata = Integer.parseInt(itemName.substring(itemName.indexOf(':')+1));
            	itemName = itemName.substring(0, itemName.indexOf(':'));
            }
            Item iconItem = GameRegistry.findItem(mod, itemName);
            if (iconItem != null)
            {
            	icons[0] = new ItemStack(GameRegistry.findItem(mod, itemName), 1, iconMetadata);
            }
        }
        
        nodes = element.getElementsByTagName("ore");
        if (nodes != null)
        {
        	String total = nodes.item(0).getTextContent();
        	if (total != "")
        	{
        		String mod = total.substring(0, total.indexOf(':'));
        		String itemName = total.substring(total.indexOf(':')+1);
            	int secondColonPosition = itemName.indexOf(':');
            	oreMetadata = 0;
            	//System.out.println("mod: "+mod+", itemName: "+itemName);
            	if (secondColonPosition != -1)
            	{
            		oreMetadata = Integer.parseInt(itemName.substring(itemName.indexOf(':')+1));
            		itemName = itemName.substring(0, itemName.indexOf(':'));
            	}
            	//System.out.println("Metadata: "+oreMetadata);
            	Block iconBlock = GameRegistry.findBlock(mod, itemName);
            	if (iconBlock != null)
            	{
            		ore[0] = iconBlock;
            	}
        	}
        }
        
        //System.out.println("=========================");
        //System.out.println("==ORE IS "+ore[0]+"!");
        //System.out.println("=========================");
        
        nodes = element.getElementsByTagName("toolmaterial");
        if (nodes != null && nodes.getLength() > 0)
        {
            material = TConstructRegistry.getMaterial(nodes.item(0).getTextContent());
        }
        else
        {
            material = TConstructRegistry.getMaterial(title);
        }

        if (material == null)
        {
        	System.err.println("Warning! "+title+" could not be found as a material!");
        	//material = TConstructRegistry.getMaterial("Stone");
        }
        
    }

    @Override
    public void renderContentLayer (int localWidth, int localHeight, boolean isTranslatable)
    {
        String harvestDiff = new String("Harvest Difficulty");
        String harvestLevel = new String("Harvest Level");
        String durability = new String("Durability");
        String handleModifier = new String("Handle Mod");
        String miningSpeed = new String("Mining Speed");
        String xpRequired = new String("XP Required");
        String baseAttack = new String("Attack Damage");
        String heart_ = new String("Heart");
        String hearts = new String("Hearts");
        String materialTrait = new String("Material Traits");
        String extraMod = new String("+1 Modifiers");
        String normal = new String("normal");

        if (isTranslatable)
        {
            //translation stuff would go in here.

        }
        manual.fonts.drawString("\u00a7n" + title, localWidth + 70, localHeight + 4, 0);
        manual.fonts.drawSplitString(description, localWidth, localHeight + 16, 178, 0);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();
        
        //Put it in front of stuff. I think.
        manual.renderitem.zLevel = 100;
        
        manual.renderitem.renderItemAndEffectIntoGUI(manual.fonts, manual.getMC().renderEngine, icons[0], localWidth + 150, localHeight + 75);
        if (ore[0] != null)
        {
        	manual.renderitem.renderItemAndEffectIntoGUI(manual.fonts, manual.getMC().renderEngine, new ItemStack(Item.getItemFromBlock(ore[0]), 1, oreMetadata), localWidth + 150, localHeight + 95);
        }
        
        //Switch back to normal layer.
        manual.renderitem.zLevel = 0;
        
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        //It seems as though each row is 10 high.
        
        if (ore[0] != null)
        {
        	manual.fonts.drawString(harvestDiff + ": " + ore[0].getHarvestLevel(oreMetadata) + " (" + HarvestLevels.getHarvestLevelName(ore[0].getHarvestLevel(oreMetadata)) + "\u00a70)", localWidth, localHeight + 50, 0);
        }
        if (material != null)
        {
        	manual.fonts.drawString(harvestLevel + ": " + material.harvestLevel() + " (" + HarvestLevels.getHarvestLevelName(material.harvestLevel()) + "\u00a70)", localWidth, localHeight + 60, 0);
        	manual.fonts.drawString(durability + ": " + material.durability(), localWidth, localHeight + 70, 0);
        	manual.fonts.drawString(handleModifier + ": " + material.handleDurability() + "x", localWidth, localHeight + 80, 0);
        	manual.fonts.drawString(miningSpeed + ": " + material.toolSpeed() / 100f, localWidth, localHeight + 90, 0);
        	int attack = material.attack();
        	String heart = (attack == 2 ? " " + heart_ : " " + hearts); //What attention to detail! Thanks, Slimeknights!
        	if (attack % 2 == 0)
        	{
        		manual.fonts.drawString(baseAttack + ": " + material.attack() / 2 + heart, localWidth, localHeight + 100, 0);
        	}
        	else
        	{
        		manual.fonts.drawString(baseAttack + ": " + material.attack() / 2f + heart, localWidth, localHeight + 100, 0);
        	}
        	//manual.fonts.drawString(xpRequired + ": " + (XPAdjustmentMap.get(material.materialName) * 100f)+"% "+normal, localWidth, localHeight + 110, 0);


        	//Here starts a list of attributes.
        	int offset = 0;
        	String ability = material.ability();
        	if (!ability.equals(""))
        	{
            	manual.fonts.drawString(materialTrait + ": " + ability, localWidth, localHeight + 125 + 16 * offset, 0);
            	offset++;
            	if (material.name().equals("Paper") || material.name().equals("Thaumium"))
            		manual.fonts.drawString(extraMod, localWidth, localHeight + 120 + 16 * offset, 0);
        	}
        }
    }
}