package talonos.cavestokingdoms;

import javax.xml.parsers.DocumentBuilderFactory;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import mantle.books.*;
import net.minecraft.util.*;

import org.w3c.dom.Document;

import talonos.cavestokingdoms.lib.DEFS;
import tconstruct.client.TProxyClient;

import java.io.InputStream;

import javax.xml.parsers.*;

import mantle.client.SmallFontRenderer;
import mantle.lib.client.MantleClientRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;

import org.w3c.dom.Document;

public class ManualInfo
{

    public BookData mats1 = new BookData();
    public BookData mats2 = new BookData();
    public BookData mats3 = new BookData();
    public BookData mats4 = new BookData();
    public BookData mats0 = new BookData();
    public BookData ben1 = new BookData();
    public BookData ben2 = new BookData();
    public BookData ben3 = new BookData();
    public BookData ben4 = new BookData();
    public BookData ben5 = new BookData();
    public BookData taint1 = new BookData();
    public BookData taint2 = new BookData();
    
    private Document mats1Doc;
    private Document mats2Doc;
    private Document mats3Doc;
    private Document mats4Doc;
    private Document mats0Doc;
    private Document ben1Doc;
    private Document ben2Doc;
    private Document ben3Doc;
    private Document ben4Doc;
    private Document ben5Doc;
    private Document taint1Doc;
    private Document taint2Doc;

    public ManualInfo()
    {
    	readManuals();
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        Document d = mats1Doc;
        initManual(mats1, "basicManual.0", "\u00a7o" + StatCollector.translateToLocal("manual1.tooltip"), d, "tinker:tinkerbook_diary") ;
        d = mats2Doc;
        initManual(mats2, "basicManual.1", "\u00a7o" + StatCollector.translateToLocal("manual1.tooltip"), d, "tinker:tinkerbook_diary") ;
        d = mats3Doc;
        initManual(mats3, "basicManual.2", "\u00a7o" + StatCollector.translateToLocal("manual1.tooltip"), d, "tinker:tinkerbook_diary") ;
        d = mats4Doc;
        initManual(mats4, "basicManual.3", "\u00a7o" + StatCollector.translateToLocal("manual1.tooltip"), d, "tinker:tinkerbook_diary") ;
        d = mats0Doc;
        initManual(mats0, "basicManual.4", "\u00a7o" + StatCollector.translateToLocal("manual1.tooltip"), d, "tinker:tinkerbook_diary") ;
        d = ben1Doc;
        initManual(ben1, "basicManual.5", "\u00a7o" + StatCollector.translateToLocal("manual1.tooltip"), d, "tinker:tinkerbook_diary") ;
        d = ben2Doc;
        initManual(ben2, "basicManual.6", "\u00a7o" + StatCollector.translateToLocal("manual1.tooltip"), d, "tinker:tinkerbook_diary") ;
        d = ben3Doc;
        initManual(ben3, "basicManual.7", "\u00a7o" + StatCollector.translateToLocal("manual1.tooltip"), d, "tinker:tinkerbook_diary") ;
        d = ben4Doc;
        initManual(ben4, "basicManual.8", "\u00a7o" + StatCollector.translateToLocal("manual1.tooltip"), d, "tinker:tinkerbook_diary") ;
        d = ben5Doc;
        initManual(ben5, "basicManual.9", "\u00a7o" + StatCollector.translateToLocal("manual1.tooltip"), d, "tinker:tinkerbook_diary") ;
        d = taint1Doc;
        initManual(taint1, "basicManual.10", "\u00a7o" + StatCollector.translateToLocal("manual1.tooltip"), d, "tinker:tinkerbook_diary") ;
        d = taint2Doc;
        initManual(taint2, "basicManual.11", "\u00a7o" + StatCollector.translateToLocal("manual1.tooltip"), d, "tinker:tinkerbook_diary") ;
    }

    public BookData initManual (BookData data, String unlocName, String toolTip, Document xmlDoc, String itemImage)
    {
        //proxy.readManuals();
        data.unlocalizedName = unlocName;
        data.toolTip = unlocName;
        data.modID = "item."+DEFS.MODID;
        data.itemImage = new ResourceLocation(data.modID, itemImage);
        data.doc = xmlDoc;
        BookDataStore.addBook(data);
        return data;
    }
    
    public void readManuals ()
    {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        mats1Doc = readManual("/assets/cavestokingdoms/manuals/materials_1.xml", dbFactory);
        mats2Doc = readManual("/assets/cavestokingdoms/manuals/materials_2.xml", dbFactory);
        mats3Doc = readManual("/assets/cavestokingdoms/manuals/materials_3.xml", dbFactory);
        mats4Doc = readManual("/assets/cavestokingdoms/manuals/materials_4.xml", dbFactory);
        mats0Doc = readManual("/assets/cavestokingdoms/manuals/materials_0.xml", dbFactory);
        ben1Doc = readManual("/assets/cavestokingdoms/manuals/materials_5.xml", dbFactory);
        ben2Doc = readManual("/assets/cavestokingdoms/manuals/materials_1.xml", dbFactory);
        ben3Doc = readManual("/assets/cavestokingdoms/manuals/materials_1.xml", dbFactory);
        ben4Doc = readManual("/assets/cavestokingdoms/manuals/materials_1.xml", dbFactory);
        ben5Doc = readManual("/assets/cavestokingdoms/manuals/materials_1.xml", dbFactory);
        taint1Doc = readManual("/assets/cavestokingdoms/manuals/materials_1.xml", dbFactory);
        taint2Doc = readManual("/assets/cavestokingdoms/manuals/materials_1.xml", dbFactory);
    }

    Document readManual (String location, DocumentBuilderFactory dbFactory)
    {
        try
        {
            InputStream stream = CavesToKindgoms.class.getResourceAsStream(location);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

}