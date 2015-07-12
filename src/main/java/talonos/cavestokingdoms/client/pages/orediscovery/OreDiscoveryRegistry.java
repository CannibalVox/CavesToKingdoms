package talonos.cavestokingdoms.client.pages.orediscovery;

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
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;
import talonos.cavestokingdoms.client.pages.orediscovery.entries.IDiscoveryEntry;
import talonos.cavestokingdoms.client.pages.orediscovery.entries.ItemDiscoveryEntry;
import talonos.cavestokingdoms.client.pages.orediscovery.entries.OreDictionaryDiscoveryEntry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class OreDiscoveryRegistry {
    private static OreDiscoveryRegistry instance = null;

    public static OreDiscoveryRegistry getInstance() {
        if (instance == null)
            instance = new OreDiscoveryRegistry();
        return instance;
    }

    private List<IDiscoveryEntry> discoverData = new ArrayList<IDiscoveryEntry>();

    public OreDiscoveryRegistry() {
        //Handle registry code.
        registerDiscovery((Item)Item.itemRegistry.getObject("minecraft:gravel"), "discover.cavestokingdoms.gravel");
        registerDiscovery((Item)Item.itemRegistry.getObject("minecraft:reeds"), "discover.cavestokingdoms.reed");
        registerDiscovery((Item)Item.itemRegistry.getObject("thismoddoesbtexist:imusingittotestrobustness"), "discover.cavestokingdoms.falseitem");

        FMLCommonHandler.instance().bus().register(this);
    }

    public void registerDiscovery(String oreDictionaryEntry, String discovery) {
        discoverData.add(new OreDictionaryDiscoveryEntry(oreDictionaryEntry, discovery));
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

    public void registerDiscovery(Item item, int meta, String discovery) {
        this.registerDiscovery(item, meta, 0xF, discovery);
    }

    public void registerDiscovery(Item item, int meta, int metaFlags, String discovery) {
        if (findDiscovery(new ItemStack(item, 1, meta & metaFlags)) != null)
            throw new RuntimeException("A matching discovery already exists in the registry.  Adding that discovery would be ambiguous.");

        discoverData.add(new ItemDiscoveryEntry(item, meta, metaFlags, discovery));
    }

    public String findDiscovery(ItemStack stack) {
        if (stack == null)
            return null;

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

        checkDiscovery(item, event.player);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onCrafting(PlayerEvent.ItemCraftedEvent event) {
        ItemStack item = event.crafting;

        checkDiscovery(item, event.player);
    }

    protected void checkDiscovery(ItemStack item, EntityPlayer player) {
        if (player.worldObj.isRemote)
            return;

        String discoveryOre = findDiscovery(item);

        if (discoveryOre != null && !hasDiscovery(player, discoveryOre)) {
            addDiscovery(player, discoveryOre);
            player.addChatMessage(new ChatComponentTranslation("blightfallmanual.discovery.add", new Object[] {StatCollector.translateToLocal(discoveryOre)}));
        }
    }

    public void scanPlayerForDiscoveries(EntityPlayer player) {
        InventoryPlayer inventory = player.inventory;
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            checkDiscovery(stack, player);
        }
    }

    public boolean hasDiscovery(EntityPlayer player, String discoveryOre) {
        return hasDiscovery(player.getEntityData(), discoveryOre);
    }

    public void addDiscovery(EntityPlayer player, String discoveryOre) {
        addDiscovery(player.getEntityData(), discoveryOre);
    }

    public void addAllDiscoveries(NBTTagCompound tag) {
        for (IDiscoveryEntry entry : discoverData) {
            addDiscovery(tag, entry.getDiscoveredOreData());
        }
    }

    public void clearDiscoveries(NBTTagCompound tag) {
        NBTTagList list = tag.getTagList("cavesToKingdomsOreDiscoveries", 8);
        if (list.tagCount() > 0)
            tag.removeTag("cavesToKingdomsOreDiscoveries");
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
}
