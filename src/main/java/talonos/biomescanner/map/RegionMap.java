package talonos.biomescanner.map;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegionMap {

    Map<Zone, Integer> totalBlocksCount = new HashMap<Zone, Integer>();
    Map<Zone, Integer> baselineCleanBlocksCount = new HashMap<Zone, Integer>();
    Map<Zone, Integer> cleanBlocksCount = new HashMap<Zone, Integer>();
    boolean buildingBaseline = true;

    Zone[][] zoneMap = null;

    Map<Integer, Zone> colorToZone = new HashMap<Integer, Zone>();

    public RegionMap() {

        for (Zone zone : Zone.values()) {
            colorToZone.put(zone.getImageColor(), zone);
        }

        try {
            BufferedImage image = ImageIO.read(RegionMap.class.getResourceAsStream("/assets/biomescanner/textures/regionmap.png"));
            zoneMap = new Zone[image.getHeight()][image.getWidth()];

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    zoneMap[y][x] = colorToZone.get(image.getRGB(x,y) & 0xFF);
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void incrementBlock(int x, int y, boolean isClean) {
        Zone zone = zoneMap[y/2][x/2];
        if (buildingBaseline) {
            increment(totalBlocksCount, zone);
            if (isClean)
                increment(baselineCleanBlocksCount, zone);
        }

        if (isClean)
            increment(cleanBlocksCount, zone);
    }

    public void wipeData() {
        cleanBlocksCount.clear();
    }

    public void updateData() {
        buildingBaseline = false;
    }

    public float getZoneCompletion(Zone zone) {
        int baseline = get(baselineCleanBlocksCount, zone);
        int total = get(totalBlocksCount, zone);
        int clean = get(cleanBlocksCount, zone);

        return calculateCompletion(baseline, clean, total);
    }

    public float getCompletion() {
        int baseline = 0;
        int total = 0;
        int clean = 0;

        for (Zone zone : Zone.values()) {
            baseline += get(baselineCleanBlocksCount, zone);
            total += get(totalBlocksCount, zone);
            clean += get(cleanBlocksCount, zone);
        }

        return calculateCompletion(baseline, clean, total);
    }

    private float calculateCompletion(int baseline, int clean, int total) {
        if (total == 0)
            return 0;

        total -= baseline;
        clean -= baseline;

        if (clean < 0)
            clean = 0;
        if (clean > total)
            clean = total;

        return (float)clean/(float)total;
    }

    private void increment(Map<Zone, Integer> map, Zone zone) {
        if (!map.containsKey(zone))
            map.put(zone, 1);
        else
            map.put(zone, map.get(zone)+1);
    }

    private int get(Map<Zone, Integer> map, Zone zone) {
        if (!map.containsKey(zone))
            return 0;
        else
            return map.get(zone);
    }
}
