package talonos.cavestokingdoms.client.pages;

import mantle.client.pages.BookPage;
import net.minecraft.item.ItemStack;

public abstract class OreDiscoveryPage extends BookPage {

    private String discoveryName;

    protected OreDiscoveryPage(String discoveryName) {
        this.discoveryName = discoveryName;
    }

    protected OreDiscoveryPage(){}

    protected boolean isDiscovered()
    {
        return isDiscovered(discoveryName);
    }

    protected boolean isDiscovered(String neededDiscovery)
    {
        if (neededDiscovery == null)
        {
            //No discovery needed; default to true;
            return true;
        }
        ItemStack book = OreDiscoveryRegistry.getInstance().getManualBook(manual);
        if (OreDiscoveryRegistry.getInstance().hasDiscovery(book.getTagCompound(), neededDiscovery))
            return true;

        return OreDiscoveryRegistry.getInstance().hasDiscovery(manual.getMC().thePlayer, neededDiscovery);
    }
}
