package talonos.cavestokingdoms.client.pages;

import cpw.mods.fml.common.registry.GameRegistry;
import iguanaman.iguanatweakstconstruct.util.HarvestLevels;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static mantle.lib.CoreRepo.logger;

public class C2KMiningPage extends OreDiscoveryPage
{
	//The title of the page
    String title;
    
    //Itemstacks representing the tools we draw
    ItemStack ore;

    int minheight = 0;
    int maxheight = 64;

    String location;

    //What harvest level unlocks this? (Same as is needed to mine it.)
    int requiredLevel;

    //Can it mine? If so, how well?
    int minesAsLevel = -1;
    
    //Description of the material
    String description;

    //The resource to display:
    ResourceLocation background;

    String whereFound = "Unknown.";

    String heightFound = "Unknown.";

    int locationImg = 0;
    int indexNum = 0;

    @Override
    public void readPageFromXML (Element element)
    {
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

        nodes = element.getElementsByTagName("location");
        if (nodes != null&&nodes.item(0)!= null&&nodes.item(0).getTextContent()!=null)
        {
            location = nodes.item(0).getTextContent();
        }

            nodes = element.getElementsByTagName("minesAsLevel");
            if (nodes != null && nodes.item(0) != null)
            {
                try
                {
                    minesAsLevel = Integer.parseInt(nodes.item(0).getTextContent());
                }
                catch(NumberFormatException e)
                {
                    Logger.getAnonymousLogger().log(Level.WARNING, "Can't parse required level for "+title+"!");
                }
            }

            //Get the ore
            nodes = element.getElementsByTagName("ore");
            if (nodes != null&&nodes.item(0)!= null)
            {
                    Node oreNode = nodes.item(0);
                    String total = oreNode.getTextContent();
                    String mod = total.substring(0, total.indexOf(':'));
                    String itemName = total.substring(total.indexOf(':') + 1);
                    int secondColonPosition = itemName.indexOf(':');
                    int meta = 0;
                    if (secondColonPosition != -1) {
                        meta = Integer.parseInt(itemName.substring(itemName.indexOf(':') + 1));
                        itemName = itemName.substring(0, itemName.indexOf(':'));
                    }
                    Item iconItem = GameRegistry.findItem(mod, itemName);
                    if (iconItem != null)
                    {
                        ore = new ItemStack(GameRegistry.findItem(mod, itemName), 1, meta);
                        requiredLevel = GameRegistry.findBlock(mod, itemName).getHarvestLevel(meta);
                    }
            }


        //Get the correct resource location:
        loadStringsAndImageLoc(location);

        int pictureNum = locationImg/9;
        indexNum = locationImg%9;





        location = "cavestokingdoms:textures/gui/locations"+pictureNum+".png";
        background = new ResourceLocation(location);
        if(background == null)
        {
            logger.warn(nodes.item(0).getTextContent() + " could not be found in the image cache(location)!");
        }
    }



    @Override
    public void renderContentLayer (int localWidth, int localHeight, boolean isTranslatable)
    {
        //if (isDiscovered(requiredLevel))
        {
            drawNormal(localWidth, localHeight);
        }
        //else
        {
           // drawLocked(localWidth, localHeight+(76));
        }
    }

    private void loadStringsAndImageLoc(String location)
    {
        if (location.equals("all"))
        {
            locationImg = 0;
        }
        if (location.equals("nether"))
        {
            locationImg = 1;
        }

        whereFound = StatCollector.translateToLocal("manual.cavestokingdoms.location."+location.toLowerCase());
    }

    private void drawNormal(int localWidth, int localHeight)
    {
        String minesAsLevel = StatCollector.translateToLocal("manual.cavestokingdoms.minesaslevel");
        String requiredToMine = StatCollector.translateToLocal("manual.cavestokingdoms.requiredtomine");
        String biome = StatCollector.translateToLocal("manual.cavestokingdoms.biome");
        String height = StatCollector.translateToLocal("manual.cavestokingdoms.height");
        String between = StatCollector.translateToLocal("manual.cavestokingdoms.between");
        String and = StatCollector.translateToLocal("manual.cavestokingdoms.and");

        manual.fonts.drawString("\u00a7n" + title, localWidth + 70, localHeight + 4, 0);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();

        manual.renderitem.renderItemAndEffectIntoGUI(manual.fonts, manual.getMC().renderEngine, ore, localWidth + 4, localHeight + 4);

        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);


        manual.renderitem.zLevel = 100;

        manual.fonts.drawString(minesAsLevel+": "+this.minesAsLevel+" ("+ HarvestLevels.getHarvestLevelName(this.minesAsLevel)+")", localWidth + 0, localHeight+25, 0);
        if (requiredLevel != -1)
        {
            manual.fonts.drawSplitString(requiredToMine + ": " + requiredLevel + " (" + HarvestLevels.getHarvestLevelName(requiredLevel) + ")", localWidth + 0, localHeight + 35, 100, 0);
        }
        manual.fonts.drawSplitString("\u00a7l"+biome + ": \u00a7r" + whereFound, localWidth + 0, localHeight + 60, 106, 0);
        manual.fonts.drawSplitString("\u00a7l"+height + ": \u00a7r" + between + " " + maxheight + " " + and + " " + minheight + ".", localWidth + 0, localHeight + 105, 100, 0);
        manual.fonts.drawSplitString(description, localWidth, localHeight + 130, 178, 0);

        manual.renderitem.zLevel = 0;
    }

    private void drawLocked(int localWidth, int localHeight)
    {
        String undiscovered = StatCollector.translateToLocal("manual.cavestokingdoms.undiscovered");
        String pleasetouch = StatCollector.translateToLocal("manual.cavestokingdoms.pleasetouch");
        String tounlock = StatCollector.translateToLocal("manual.cavestokingdoms.tounlock");

        manual.fonts.drawString("\u00a7n" + undiscovered, localWidth + 14, localHeight + 4, 0);
        manual.fonts.drawString(pleasetouch, localWidth + 18, localHeight + 16, 0);
        manual.fonts.drawString(tounlock, localWidth + 60, localHeight + 26, 0);
    }


    public void renderBackgroundLayer (int localWidth, int localHeight)
    {
        int xIndex = indexNum%3;
        int yIndex = indexNum/3;
        if (background != null)
        {
            manual.getMC().getTextureManager().bindTexture(background);
        }
        //manual.getMC().renderEngine.bindTexture(location);
        manual.drawTexturedModalRect(localWidth+110, localHeight + 15, xIndex*70, yIndex*85, 70, 85);
    }

    private boolean isDiscovered(int level)
    {
        return isDiscovered("discover.cavestokingdoms.harvestlevel"+level);
    }
}