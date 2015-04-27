package talonos.biomescanner;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import talonos.biomescanner.item.ItemBadge;

public class BSItems {

    public static void init() {
        badge = new ItemBadge().setMaxStackSize(1);
        GameRegistry.registerItem(badge, "biomescanner:badge");
    }

    public static Item badge;
}
