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

        //Register all metals, first by ingot, then nugget, then block, then ore.
        //This will lead to some impossible combinations (Like void metal blocks or
        //damascus steel ore) but it should still work because of the OreDictionary
        registerAllMetalsWith("ingot");
        registerAllMetalsWith("block");
        registerAllMetalsWith("nugget");
        registerAllMetalsWith("ore");

        //Register some gems and stuff
        registerDiscovery("ore:crystalCertusQuartz","discover.cavestokingdoms.certusquartz");
        registerDiscovery("ore:crystalNetherQuartz","discover.cavestokingdoms.netherquartz");
        registerDiscovery("ore:gemAmethyst","discover.cavestokingdoms.amethyst");
        registerDiscovery("ore:gemRuby","discover.cavestokingdoms.ruby");
        registerDiscovery("ore:gemSapphire","discover.cavestokingdoms.sapphire");
        registerDiscovery("ore:gravel","discover.cavestokingdoms.gravel");
        registerDiscovery("ore:netherrack","discover.cavestokingdoms.netherrack");
        registerDiscovery("ore:obsidian","discover.cavestokingdoms.obsidian");

        //Some things lack oredict entries.
        registerDiscovery(GameRegistry.findItem("ExtraUtilities","bedrockiumIngot"), "discover.cavestokingdoms.bedrockium");
        registerDiscovery(GameRegistry.findItem("Natura","bloodwood"), 0, "discover.cavestokingdoms.bloodwood");
        registerDiscovery(GameRegistry.findItem("Natura","planks"),  4, "discover.cavestokingdoms.bloodwood");
        registerDiscovery(GameRegistry.findItem("Tconstruct","strangeFood"),  1, "discover.cavestokingdoms.blueslime");
        registerDiscovery(GameRegistry.findItem("minecraft","dye"),  15, "discover.cavestokingdoms.bonemeal");
        registerDiscovery(GameRegistry.findItem("minecraft","cactus"), "discover.cavestokingdoms.cactus");
        registerDiscovery(GameRegistry.findItem("appliedenergistics2","item.ItemMultiMaterial"),  1, "discover.cavestokingdoms.certusquartz");
        registerDiscovery(GameRegistry.findItem("minecraft","chainmail_helmet"), "discover.cavestokingdoms.chain");
        registerDiscovery(GameRegistry.findItem("minecraft","chainmail_chestplate"), "discover.cavestokingdoms.chain");
        registerDiscovery(GameRegistry.findItem("minecraft","chainmail_leggings"), "discover.cavestokingdoms.chain");
        registerDiscovery(GameRegistry.findItem("minecraft","chainmail_boots"), "discover.cavestokingdoms.chain");
        registerDiscovery(GameRegistry.findItem("Thaumcraft","ItemBootsCultist"), "discover.cavestokingdoms.crimson");
        registerDiscovery(GameRegistry.findItem("Thaumcraft","ItemChestplateCultistLeaderPlate"), "discover.cavestokingdoms.crimson");
        registerDiscovery(GameRegistry.findItem("Thaumcraft","ItemChestplateCultistPlate"), "discover.cavestokingdoms.crimson");
        registerDiscovery(GameRegistry.findItem("Thaumcraft","ItemChestplateCultistRobe"), "discover.cavestokingdoms.crimson");
        registerDiscovery(GameRegistry.findItem("Thaumcraft","ItemHelmetCultistLeaderPlate"), "discover.cavestokingdoms.crimson");
        registerDiscovery(GameRegistry.findItem("Thaumcraft","ItemHelmetCultistPlate"), "discover.cavestokingdoms.crimson");
        registerDiscovery(GameRegistry.findItem("Thaumcraft","ItemHelmetCultistRobe"), "discover.cavestokingdoms.crimson");
        registerDiscovery(GameRegistry.findItem("Thaumcraft","ItemLeggingsCultistLeaderPlate"), "discover.cavestokingdoms.crimson");
        registerDiscovery(GameRegistry.findItem("Thaumcraft","ItemLeggingsCultistPlate"), "discover.cavestokingdoms.crimson");
        registerDiscovery(GameRegistry.findItem("Thaumcraft","ItemLeggingsCultistRobe"), "discover.cavestokingdoms.crimson");
        registerDiscovery(GameRegistry.findItem("Natura","Dark Tree"),  0, "discover.cavestokingdoms.darkwood");
        registerDiscovery(GameRegistry.findItem("Natura","planks"),  11, "discover.cavestokingdoms.darkwood");
        registerDiscovery(GameRegistry.findItem("Natura","Dark Tree"),  1, "discover.cavestokingdoms.fusewood");
        registerDiscovery(GameRegistry.findItem("Natura","planks"),  12, "discover.cavestokingdoms.fusewood");
        registerDiscovery(GameRegistry.findItem("Natura","tree"),  2, "discover.cavestokingdoms.ghostwood");
        registerDiscovery(GameRegistry.findItem("Natura","planks"),  2, "discover.cavestokingdoms.ghostwood");
        registerDiscovery(GameRegistry.findItem("Tconstruct","materials"),  36, "discover.cavestokingdoms.glue");
        registerDiscovery(GameRegistry.findItem("minecraft","slime_ball"), "discover.cavestokingdoms.greenslime");
        registerDiscovery(GameRegistry.findItem("ThaumicTinkerer","kamiResource"),  1, "discover.cavestokingdoms.ichorcloth");
        registerDiscovery(GameRegistry.findItem("minecraft","leather"), "discover.cavestokingdoms.leather");
        registerDiscovery(GameRegistry.findItem("Thaumcraft","ItemResource"),  7, "discover.cavestokingdoms.magiccloth");
        registerDiscovery(GameRegistry.findItem("ExtraTiC","chunk"),  165, "discover.cavestokingdoms.peridot");
        registerDiscovery(GameRegistry.findItem("minecraft","reeds"), "discover.cavestokingdoms.reeds");
        registerDiscovery(GameRegistry.findItem("customnpcs","npcNanorumChest"), "discover.cavestokingdoms.scout");
        registerDiscovery(GameRegistry.findItem("customnpcs","npcSoldierLegs"), "discover.cavestokingdoms.scout");
        registerDiscovery(GameRegistry.findItem("customnpcs","npcSoldierHead"), "discover.cavestokingdoms.scout");
        registerDiscovery(GameRegistry.findItem("customnpcs","npcWizardHead"), "discover.cavestokingdoms.wizard");
        registerDiscovery(GameRegistry.findItem("customnpcs","npcWizardChest"), "discover.cavestokingdoms.wizard");
        registerDiscovery(GameRegistry.findItem("customnpcs","npcWizardPants"), "discover.cavestokingdoms.wizard");
        registerDiscovery(GameRegistry.findItem("customnpcs","npcAssassinHead"), "discover.cavestokingdoms.assassin");
        registerDiscovery(GameRegistry.findItem("customnpcs","npcAssassinBoots"), "discover.cavestokingdoms.assassin");
        registerDiscovery(GameRegistry.findItem("customnpcs","npcAssassinLeggings"), "discover.cavestokingdoms.assassin");



        FMLCommonHandler.instance().bus().register(this);
    }

    private void registerAllMetalsWith(String type)
    {
        registerDiscovery("ore:"+type+"Adamantine","discover.cavestokingdoms.adamantine");
        registerDiscovery("ore:"+type+"Alumite","discover.cavestokingdoms.alumite");
        registerDiscovery("ore:"+type+"Amordrine","discover.cavestokingdoms.amordrine");
        registerDiscovery("ore:"+type+"Angmallen","discover.cavestokingdoms.angmallen");
        registerDiscovery("ore:"+type+"Ardite","discover.cavestokingdoms.ardite");
        registerDiscovery("ore:"+type+"AstralSilver","discover.cavestokingdoms.astralsilver");
        registerDiscovery("ore:"+type+"Atlarus","discover.cavestokingdoms.atlarus");
        registerDiscovery("ore:"+type+"BlackSteel","discover.cavestokingdoms.blacksteel");
        registerDiscovery("ore:"+type+"Brass","discover.cavestokingdoms.brass");
        registerDiscovery("ore:"+type+"Bronze","discover.cavestokingdoms.bronze");
        registerDiscovery("ore:"+type+"Carmot","discover.cavestokingdoms.carmot");
        registerDiscovery("ore:"+type+"Celenegil","discover.cavestokingdoms.celenegil");
        registerDiscovery("ore:"+type+"Ceruclase","discover.cavestokingdoms.ceruclase");
        registerDiscovery("ore:"+type+"Cobalt","discover.cavestokingdoms.cobalt");
        registerDiscovery("ore:"+type+"Copper","discover.cavestokingdoms.copper");
        registerDiscovery("ore:"+type+"DamascusSteel","discover.cavestokingdoms.damascussteel");
        registerDiscovery("ore:"+type+"DeepIron","discover.cavestokingdoms.deepiron");
        registerDiscovery("ore:"+type+"Desichalkos","discover.cavestokingdoms.desichalkos");
        registerDiscovery("ore:"+type+"Electrum","discover.cavestokingdoms.electrum");
        registerDiscovery("ore:"+type+"ElvenElementium","discover.cavestokingdoms.elementium");
        registerDiscovery("ore:"+type+"Enderium","discover.cavestokingdoms.enderium");
        registerDiscovery("ore:"+type+"Eximite","discover.cavestokingdoms.eximite");
        registerDiscovery("ore:"+type+"Fairy","discover.cavestokingdoms.fairy");
        registerDiscovery("ore:"+type+"ElectrumFlux","discover.cavestokingdoms.fluxinfused");
        registerDiscovery("ore:"+type+"Gold","discover.cavestokingdoms.gold");
        registerDiscovery("ore:"+type+"Haderoth","discover.cavestokingdoms.haderoth");
        registerDiscovery("ore:"+type+"Hepatizon","discover.cavestokingdoms.hepatizon");
        registerDiscovery("ore:"+type+"Ignatius","discover.cavestokingdoms.ignatius");
        registerDiscovery("ore:"+type+"Inolashite","discover.cavestokingdoms.inolashite");
        registerDiscovery("ore:"+type+"Invar","discover.cavestokingdoms.invar");
        registerDiscovery("ore:"+type+"Iron","discover.cavestokingdoms.iron");
        registerDiscovery("ore:"+type+"Kalendrite","discover.cavestokingdoms.kalendrite");
        registerDiscovery("ore:"+type+"Lead","discover.cavestokingdoms.lead");
        registerDiscovery("ore:"+type+"Lumium","discover.cavestokingdoms.lumium");
        registerDiscovery("ore:"+type+"Manasteel","discover.cavestokingdoms.manasteel");
        registerDiscovery("ore:"+type+"Manyullyn","discover.cavestokingdoms.manyullyn");
        registerDiscovery("ore:"+type+"Midasium","discover.cavestokingdoms.midasium");
        registerDiscovery("ore:"+type+"Mithril","discover.cavestokingdoms.mithril");
        registerDiscovery("ore:"+type+"Nickel","discover.cavestokingdoms.nickel");
        registerDiscovery("ore:"+type+"Orichalcum","discover.cavestokingdoms.orichalcum");
        registerDiscovery("ore:"+type+"Oureclase","discover.cavestokingdoms.oureclase");
        registerDiscovery("ore:"+type+"PigIron","discover.cavestokingdoms.pigiron");
        registerDiscovery("ore:"+type+"Platinum","discover.cavestokingdoms.platinum");
        registerDiscovery("ore:"+type+"Pokefennium","discover.cavestokingdoms.pokefennium");
        registerDiscovery("ore:"+type+"Prometheum","discover.cavestokingdoms.prometheum");
        registerDiscovery("ore:"+type+"Quicksilver","discover.cavestokingdoms.quicksilver");
        registerDiscovery("ore:"+type+"Sanguinite","discover.cavestokingdoms.sanguinite");
        registerDiscovery("ore:"+type+"ShadowIron","discover.cavestokingdoms.shadowiron");
        registerDiscovery("ore:"+type+"ShadowSteel","discover.cavestokingdoms.shadowsteel");
        registerDiscovery("ore:"+type+"Signalum","discover.cavestokingdoms.signalum");
        registerDiscovery("ore:"+type+"Silver","discover.cavestokingdoms.silver");
        registerDiscovery("ore:"+type+"Steel","discover.cavestokingdoms.steel");
        registerDiscovery("ore:"+type+"Tartarite","discover.cavestokingdoms.tartarite");
        registerDiscovery("ore:"+type+"Terrasteel","discover.cavestokingdoms.terrasteel");
        registerDiscovery("ore:"+type+"Thaumium","discover.cavestokingdoms.thaumium");
        registerDiscovery("ore:"+type+"Tin","discover.cavestokingdoms.tin");
        registerDiscovery("ore:"+type+"Void","discover.cavestokingdoms.voidmetal");
        registerDiscovery("ore:"+type+"Vulcanite","discover.cavestokingdoms.vulcanite");
        registerDiscovery("ore:"+type+"Vyroxeres","discover.cavestokingdoms.vyroxeres");
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
