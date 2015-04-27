package talonos.biomescanner.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import talonos.biomescanner.map.Zone;

import java.util.List;

public class ItemBadge extends Item {
    private IIcon[] zoneIcons = new IIcon[Zone.values().length * 3];
    private IIcon completionIcon;

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        completionIcon = iconRegister.registerIcon("biomescanner:completionBadge");

        for(Zone zone : Zone.values()) {
            zoneIcons[zone.ordinal()*3] = iconRegister.registerIcon("biomescanner:zoneBadge-"+zone.toString()+"-bronze");
            zoneIcons[zone.ordinal()*3 + 1] = iconRegister.registerIcon("biomescanner:zoneBadge-"+zone.toString()+"-silver");
            zoneIcons[zone.ordinal()*3 + 2] = iconRegister.registerIcon("biomescanner:zoneBadge-"+zone.toString()+"-gold");
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int damage) {
        if (damage < zoneIcons.length)
            return zoneIcons[damage];
        else
            return completionIcon;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        int damage = stack.getItemDamage();
        if (damage >= zoneIcons.length)
            return StatCollector.translateToLocal("item.completionBadge.name");

        int medalType = damage % 3;

        String zoneName = StatCollector.translateToLocal(Zone.values()[damage/3].getDisplay());
        String medalDisplay;
        switch (medalType) {
            case 0:
                medalDisplay = StatCollector.translateToLocal("item.zoneBronze.name");
                break;
            case 1:
                medalDisplay = StatCollector.translateToLocal("item.zoneSilver.name");
                break;
            case 2:
            default:
                medalDisplay = StatCollector.translateToLocal("item.zoneGold.name");
        }

        return medalDisplay.replace("{0}", zoneName);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean advanced) {
        int damage = itemStack.getItemDamage();
        if (damage < zoneIcons.length) {
            String zoneName = StatCollector.translateToLocal(Zone.values()[damage/3].getDisplay());
            int medalType = damage % 3;
            switch (medalType) {
                case 0:
                    addBadgeData("gui.bronzeInfo", zoneName, info);
                    break;
                case 1:
                    addBadgeData("gui.silverInfo", zoneName, info);
                    break;
                case 2:
                    addBadgeData("gui.goldInfo", zoneName, info);
                    break;
            }
            return;
        }

        addBadgeData("gui.completionInfo", "", info);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < zoneIcons.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
        list.add(new ItemStack(this, 1, zoneIcons.length));
    }

    private void addBadgeData(String value, String zoneName, List info) {
        for (int i = 0; i < 99; i++) {
            if (!StatCollector.canTranslate(value + Integer.toString(i)))
                break;

            info.add("\u00a79" + StatCollector.translateToLocal(value + Integer.toString(i)).replace("{0}", zoneName));
        }
    }
}
