package talonos.cavestokingdoms.client.pages;

import mantle.client.pages.BookPage;
import net.minecraft.item.ItemStack;

public abstract class OreDiscoveryPage extends BookPage {

    private String discoveryName;

    protected OreDiscoveryPage(String discoveryName) {
        this.discoveryName = discoveryName;
    }

    protected boolean isDiscovered() {
        ItemStack book = OreDiscoveryRegistry.getInstance().getManualBook(manual);
        if (OreDiscoveryRegistry.getInstance().hasDiscovery(book.getTagCompound(), discoveryName))
            return true;

        return OreDiscoveryRegistry.getInstance().hasDiscovery(manual.getMC().thePlayer, discoveryName);
    }
}
