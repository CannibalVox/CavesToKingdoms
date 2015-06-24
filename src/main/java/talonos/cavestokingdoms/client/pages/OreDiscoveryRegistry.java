package talonos.cavestokingdoms.client.pages;

import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mantle.client.gui.GuiManual;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class OreDiscoveryRegistry {
    private static OreDiscoveryRegistry instance = null;

    public static OreDiscoveryRegistry getInstance() {
        if (instance == null)
            instance = new OreDiscoveryRegistry();
        return instance;
    }

    private static class DiscoveryEntry {
        private Item discoveryItem;
        private int discoveryMeta;
        private int discoveryMetaFlags;
        private String discoveredOreData;

        public DiscoveryEntry(Item item, int meta, int metaFlags, String oreData) {
            this.discoveryItem = item;
            this.discoveryMeta = meta;
            this.discoveryMetaFlags = metaFlags;
            this.discoveredOreData = oreData;
        }

        public Item getDiscoveryItem() { return discoveryItem; }
        public int getDiscoveryMeta() { return discoveryMeta; }
        public int getDiscoveryMetaFlags() { return discoveryMetaFlags; }
        public String getDiscoveredOreData() { return discoveredOreData; }

        public boolean matches(ItemStack stack) {
            if (getDiscoveryItem() != stack.getItem())
                return false;

            return ((getDiscoveryMeta() & getDiscoveryMetaFlags()) == (stack.getItemDamage() & getDiscoveryMetaFlags()));
        }
    }

    private Field manualItemStack = null;
    private List<DiscoveryEntry> discoverData = new ArrayList<DiscoveryEntry>();

    public OreDiscoveryRegistry() {
        try {
            manualItemStack = GuiManual.class.getDeclaredField("itemstackBook");
            manualItemStack.setAccessible(true);
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException("Failed to find 'itemstackBook' field of GuiManual.", ex);
        }

        //Handle registry code.
        registerDiscovery("minecraft:gravel", "discover.cavestokingdoms.gravel");
        registerDiscovery("minecraft:reeds", "discover.cavestokingdoms.reed");

        FMLCommonHandler.instance().bus().register(this);
    }

    public void registerDiscovery(Block block, String discovery) {
        this.registerDiscovery(block, 0, 0, discovery);
    }

    public void registerDiscovery(Block block, int meta, String discovery) {
        this.registerDiscovery(block, meta, 0xF, discovery);
    }

    public void registerDiscovery(Block block, int meta, int metaFlags, String discovery) {
        this.registerDiscovery(Item.getItemFromBlock(block), meta, metaFlags, discovery);
    }

    public void registerDiscovery(Item item, String discovery) {
        this.registerDiscovery(item, 0, 0, discovery);
    }

    /**
     * Registers a discovery using a string instead of an item. Here to provide lookup functionality instead of
     * repeating code over and over.
     * @param itemString A string representing the item in question, in the format "mod:name[:optional meta]"
     * @param discovery The discovery this maps to.
     */
    public void registerDiscovery(String itemString, String discovery) {
        if (itemString != null) {
            String mod = itemString.substring(0, itemString.indexOf(':'));
            String itemName = itemString.substring(itemString.indexOf(':') + 1);
            int secondColonPosition = itemName.indexOf(':');
            int meta = 0;
            if (secondColonPosition != -1) {
                meta = Integer.parseInt(itemName.substring(itemName.indexOf(':') + 1));
                itemName = itemName.substring(0, itemName.indexOf(':'));
            }
            Item item = GameRegistry.findItem(mod, itemName);
            if (item != null) {
                this.registerDiscovery(item, meta, 0xF, discovery);
            } else {
                throw new RuntimeException("Exception: Cannot find item " + itemName);
            }
        } else {
            throw new RuntimeException("Exception: null string passed to registerDiscovery");
        }
        //There's all sorts of opportunity for ArrayOutOfBounds because of missing colons, etc.
        //But if we're throwing runtime exceptions anyway, I guess we don't care..?
    }

    public void registerDiscovery(Item item, int meta, String discovery) {
        this.registerDiscovery(item, meta, 0xF, discovery);
    }

    public void registerDiscovery(Item item, int meta, int metaFlags, String discovery) {
        if (findDiscovery(new ItemStack(item, 1, meta & metaFlags)) != null)
            throw new RuntimeException("A matching discovery already exists in the registry.  Adding that discovery would be ambiguous.");

        discoverData.add(new DiscoveryEntry(item, meta, metaFlags, discovery));
    }

    public String findDiscovery(ItemStack stack) {
        int size = discoverData.size();
        for (int i = 0; i < size; i++) {
            if (discoverData.get(i).matches(stack))
                return discoverData.get(i).getDiscoveredOreData();
        }

        return null;
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void playerReceivedItem(PlayerEvent.ItemPickupEvent event) {
        ItemStack item = event.pickedUp.getEntityItem();

        String discoveryOre = findDiscovery(item);

        if (discoveryOre != null && !hasDiscovery(event.player, discoveryOre)) {
            addDiscovery(event.player, discoveryOre);
            event.player.addChatMessage(new ChatComponentTranslation("blightfallmanual.discovery.add", new Object[] {StatCollector.translateToLocal(discoveryOre)}));
        }
    }

    public boolean hasDiscovery(EntityPlayer player, String discoveryOre) {
        return hasDiscovery(player.getEntityData(), discoveryOre);
    }

    public void addDiscovery(EntityPlayer player, String discoveryOre) {
        addDiscovery(player.getEntityData(), discoveryOre);
    }

    public boolean hasDiscovery(NBTTagCompound tag, String discoveryOre) {
        if (tag == null)
        {
            return false;
        }
        NBTTagList list = tag.getTagList("cavesToKingdomsOreDiscoveries", 8);
        for (int i = 0; i < list.tagCount(); i++) {
            if (list.getStringTagAt(i).equals(discoveryOre))
                return true;
        }

        return false;
    }

    public void addDiscovery(NBTTagCompound tag, String discoveryOre) {
        NBTTagList list = tag.getTagList("cavesToKingdomsOreDiscoveries", 8);
        list.appendTag(new NBTTagString(discoveryOre));
        tag.setTag("cavesToKingdomsOreDiscoveries", list);
    }

    public void copyDiscoveries(NBTTagCompound from, NBTTagCompound to) {
        NBTTagList list = from.getTagList("cavesToKingdomsOreDiscoveries", 8);
        for (int i = 0; i < list.tagCount(); i++) {
            String discovery = list.getStringTagAt(i);

            if (!hasDiscovery(to, discovery))
                addDiscovery(to, discovery);
        }
    }

    public ItemStack getManualBook(GuiManual manual) {
        try {
            return (ItemStack)manualItemStack.get(manual);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Failed to change accessibility for 'itemstackBook' field of GuiManual.", ex);
        }
    }
}
