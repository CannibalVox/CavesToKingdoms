package talonos.cavestokingdoms.client.pages;

import cpw.mods.fml.common.registry.GameRegistry;
import iguanaman.iguanatweakstconstruct.override.XPAdjustmentMap;
import iguanaman.iguanatweakstconstruct.util.HarvestLevels;
import mantle.client.pages.BookPage;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.ArrowMaterial;
import tconstruct.library.tools.BowMaterial;
import tconstruct.library.tools.ToolMaterial;

import java.util.HashMap;
import java.util.Map;

public class BowMaterialsPage extends BookPage
{
    private static final int NUMBER_OF_MATS_PER_PAGE = 2;
    private static final int MAT_OFFSET = 78;

    //The materials this page depicts.
    private ToolMaterial[] materials = new ToolMaterial[NUMBER_OF_MATS_PER_PAGE];
    private BowMaterial[] bowMaterials = new BowMaterial[NUMBER_OF_MATS_PER_PAGE];
    private ArrowMaterial[] arrowMaterials = new ArrowMaterial[NUMBER_OF_MATS_PER_PAGE];
    private String[] matNames = new String[NUMBER_OF_MATS_PER_PAGE];
    private String[] descriptions = new String[NUMBER_OF_MATS_PER_PAGE];
    
    //Itemstacks representing the icons we'll end up drawing on the page.
    private ItemStack[] icons = new ItemStack[NUMBER_OF_MATS_PER_PAGE];
    private int[] iconMetadata = new int[NUMBER_OF_MATS_PER_PAGE];

    public static HashMap<String, Integer> mappings = new HashMap();
    public static boolean init = false;

    public static void init()
    {
        for (Map.Entry<Integer, ToolMaterial> entry : TConstructRegistry.toolMaterials.entrySet())
        {
            mappings.put(entry.getValue().materialName, entry.getKey());
            init = true;
        }
    }

    //I think this initializes the instance of a page, given an XML page element.
    @Override
    public void readPageFromXML (Element element)
    {
        if (!init)
        {
            init();
        }

        try {
            NodeList nodes;
            for (int i = 0; i < NUMBER_OF_MATS_PER_PAGE; i++) {
                //Get the name
                nodes = element.getElementsByTagName("mat" + i);
                if (nodes != null)
                {
                    matNames[i] = nodes.item(0).getTextContent();
                }

                nodes = element.getElementsByTagName("desc" + i);
                if (nodes != null&&nodes.item(0)!=null&&nodes.item(0).getTextContent()!=null)
                {
                    descriptions[i] = nodes.item(0).getTextContent();
                }
                else
                {
                    descriptions[i] = "";
                }

                //Get the icon
                icons[i] = new ItemStack(Items.rotten_flesh);
                nodes = element.getElementsByTagName("icon" + i);
                if (nodes != null&&nodes.item(0)!=null&&nodes.item(0).getTextContent()!=null)
                {
                    //Seperate the string into three parts
                    String total = nodes.item(0).getTextContent();
                    String mod = total.substring(0, total.indexOf(':'));
                    String itemName = total.substring(total.indexOf(':') + 1);
                    int secondColonPosition = itemName.indexOf(':');

                    //Default metadata to 0 if no metadata segment
                    iconMetadata[i] = 0;

                    if (secondColonPosition != -1) {
                        iconMetadata[i] = Integer.parseInt(itemName.substring(itemName.indexOf(':') + 1));
                        itemName = itemName.substring(0, itemName.indexOf(':'));
                    }

                    Item iconItem = GameRegistry.findItem(mod, itemName);

                    if (iconItem != null)
                    {
                        icons[i] = new ItemStack(GameRegistry.findItem(mod, itemName), 1, iconMetadata[i]);
                    }
                }

                //Get the material
                nodes = element.getElementsByTagName("toolmaterial" + i);
                if (nodes != null && nodes.getLength() > 0) {
                    System.out.println("Getting from Materials");
                    materials[i] = TConstructRegistry.getMaterial(nodes.item(0).getTextContent());
                    if (materials[i] != null)
                    {
                        bowMaterials[i] = TConstructRegistry.getBowMaterial(mappings.get(materials[i].name()));
                        arrowMaterials[i] = TConstructRegistry.getArrowMaterial(mappings.get(materials[i].name()));
                    }
                } else {
                    System.out.println("Getting from Name");
                    materials[i] = TConstructRegistry.getMaterial(matNames[i]);
                    if (materials[i] != null)
                    {
                        bowMaterials[i] = TConstructRegistry.getBowMaterial(mappings.get(materials[i].name()));
                        arrowMaterials[i] = TConstructRegistry.getArrowMaterial(mappings.get(materials[i].name()));
                    }
                }

                if (materials[i] == null) {
                    System.err.println("Warning! " + matNames[i] + " could not be found as a material!");
                    //material = TConstructRegistry.getMaterial("Stone");
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("An exception: ");
            e.printStackTrace();
        }
    }

    @Override
    public void renderContentLayer (int localWidth, int localHeight, boolean isTranslatable)
    {
        String durability = new String("Durability");
        String drawSpeed = new String("Draw Delay");
        String flightSpeed = new String("Launch Strength");
        String xpRequired = new String("XP Required");
        String mass = new String("Mass");
        String breakChance = new String("Break Chance");
        String baseAttack = new String("Attack Damage");
        String heart_ = new String("Heart");
        String hearts = new String("Hearts");

        if (isTranslatable)
        {
            //translation stuff would go in here. I forget how to do it even, check TiC's github.
        }

        for (int i = 0; i < NUMBER_OF_MATS_PER_PAGE; i++)
        {
            int offset = i*MAT_OFFSET;
            manual.fonts.drawString("\u00a7n" + matNames[i], localWidth + 45, localHeight + 4 + offset, 0);

            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.enableGUIStandardItemLighting();

            //Put it in front of stuff. I think.
            manual.renderitem.zLevel = 100;

            manual.renderitem.renderItemAndEffectIntoGUI(manual.fonts, manual.getMC().renderEngine, icons[i], localWidth + 150, localHeight + offset + 4);

            //Switch back to normal layer.
            manual.renderitem.zLevel = 0;

            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);

            //It seems as though each row is 10 high.

            boolean bowsOn = bowMaterials[i] != null;
            boolean arrowsOn = arrowMaterials[i] != null;

            if (materials[i] != null)
            {
                manual.fonts.drawString(durability + ": " + materials[i].durability(), localWidth, localHeight + 20 + offset, 0);
                if (bowsOn)
                {
                    manual.fonts.drawString(drawSpeed + ": " + bowMaterials[i].drawspeed, localWidth, localHeight + 30 + offset, 0);
                    manual.fonts.drawString(flightSpeed + ": " + bowMaterials[i].flightSpeedMax, localWidth, localHeight + 40 + offset, 0);
                }
                manual.fonts.drawString(xpRequired + ": " + (XPAdjustmentMap.get(materials[i].materialName) * 100f)+"% ", localWidth, localHeight + 50 + offset, 0);

                manual.fonts.drawString(descriptions[i], localWidth, localHeight + 65 + offset, 0);

                if (arrowsOn)
                {
                    manual.fonts.drawString(mass + ": " + arrowMaterials[i].mass, localWidth + 85, localHeight + 20 + offset, 0);
                    //So, it's dumb, but break chance is multipled by .15 before actually being applied.
                    //And that's just with wood. Other materials have other break rates.
                    //We'll use wood as the staple here.
                    manual.fonts.drawString(breakChance + ": " + (arrowMaterials[i].breakChance * 15f)+"% ", localWidth + 85, localHeight + 30 + offset, 0);
                }

                //Attack is weird.
                int attack = materials[i].attack();
                String heart = (attack == 2 ? " " + heart_ : " " + hearts);
                //Only make it a decimal if it's not an even heart.
                String totalString = baseAttack + ": " + (attack % 2 == 0? materials[i].attack() / 2: materials[i].attack() / 2f) +heart;
                manual.fonts.drawString(totalString, localWidth + 85, localHeight + 40 + offset, 0);
            }
        }
    }
}