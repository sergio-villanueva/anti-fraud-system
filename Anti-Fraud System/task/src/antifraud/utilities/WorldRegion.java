package antifraud.utilities;

public enum WorldRegion {
    EAP("EAP"), //East Asia and Pacific
    ECA("ECA"), //Europe and Central Asia
    HIC("HIC"), //High-Income countries
    LAC("LAC"), //Latin America and the Caribbean
    MENA("MENA"), //The Middle East and North Africa
    SA("SA"), //South Asia
    SSA("SSA") //Sub-Saharan Africa
    ;

    WorldRegion(String stringRegion) {
        this.stringRegion = stringRegion;
    }

    private final String stringRegion;

    public String getStringRegion() {
        return stringRegion;
    }

    public static WorldRegion getNullableWorldRegionByString(String stringRegion) {
        WorldRegion[] worldRegions = values();
        for (WorldRegion worldRegion : worldRegions) {
            if (worldRegion.getStringRegion().equalsIgnoreCase(stringRegion)) return worldRegion;
        }
        return null;
    }
}
