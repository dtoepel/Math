package org.example.elections.stv;

import java.awt.*;

public class Party implements Aligned {
    private final Alignment alignment;
    private final String name;
    private final String shortName;
    private final java.awt.Color color;

    public static final Party GREY_PARTY;
    public static final Party THE_HALLOW_PARTY;
    public static final Party CONSERVATIVE_PARTY;
    public static final Party CENTER_PARTY;
    public static final Party LIBERAL_PARTY;
    public static final Party GREEN_PARTY;
    public static final Party COMMUNIST_PARTY;
    //public static final Party LEFT_SEPARATIST_PARTY;
    //public static final Party CENTER_SEPARATIST_PARTY;
    //public static final Party RIGHT_SEPARATIST_PARTY;

    static {
        /* The gray party has a neutral alignment. It has no opinion about anything. */
        GREY_PARTY = new Party(new Alignment(), "Gray Party", "GRAY", new Color(0x808080));

        Alignment hallowPartyAlignment = new Alignment();
        hallowPartyAlignment.setAlignment(Dimension.AUTHORITARIAN_DEMOCRATIC, -.9);
        hallowPartyAlignment.setAlignment(Dimension.INTERNATIONAL_NATIONAL, .5);
        hallowPartyAlignment.setAlignment(Dimension.LIBERAL_CONSERVATIVE, .9);
        hallowPartyAlignment.setAlignment(Dimension.FOSSILE_GREEN, -.3);
        hallowPartyAlignment.setAlignment(Dimension.SOCIAL_INDIVIDUAL, .2);
        hallowPartyAlignment.setAlignment(Dimension.RELIGIOUS_SECULAR, -1.);
        hallowPartyAlignment.setAlignment(Dimension.UNIONIST_SEPARATIST, -.4);
        THE_HALLOW_PARTY = new Party(hallowPartyAlignment, "The Hallow Party", "THE",
                new Color(0xee8800));

        Alignment conservativePartyAlignment = new Alignment();
        conservativePartyAlignment.setAlignment(Dimension.AUTHORITARIAN_DEMOCRATIC, .6);
        conservativePartyAlignment.setAlignment(Dimension.INTERNATIONAL_NATIONAL, .6);
        conservativePartyAlignment.setAlignment(Dimension.LIBERAL_CONSERVATIVE, .5);
        conservativePartyAlignment.setAlignment(Dimension.FOSSILE_GREEN, -.8);
        conservativePartyAlignment.setAlignment(Dimension.SOCIAL_INDIVIDUAL, .9);
        conservativePartyAlignment.setAlignment(Dimension.RELIGIOUS_SECULAR, -.3);
        conservativePartyAlignment.setAlignment(Dimension.UNIONIST_SEPARATIST, -.6);
        CONSERVATIVE_PARTY = new Party(conservativePartyAlignment, "Elephant Party", "ELE",
                new Color(0xbb0000));

        Alignment centerPartyAlignment = new Alignment();
        centerPartyAlignment.setAlignment(Dimension.AUTHORITARIAN_DEMOCRATIC, .7);
        centerPartyAlignment.setAlignment(Dimension.INTERNATIONAL_NATIONAL, .4);
        centerPartyAlignment.setAlignment(Dimension.LIBERAL_CONSERVATIVE, 0.);
        centerPartyAlignment.setAlignment(Dimension.FOSSILE_GREEN, -.5);
        centerPartyAlignment.setAlignment(Dimension.SOCIAL_INDIVIDUAL, .3);
        centerPartyAlignment.setAlignment(Dimension.RELIGIOUS_SECULAR, .5);
        centerPartyAlignment.setAlignment(Dimension.UNIONIST_SEPARATIST, -.5);
        CENTER_PARTY = new Party(centerPartyAlignment, "Eagle Party", "EAG",
                new Color(0xbb88dd));

        Alignment liberalPartyAlignment = new Alignment();
        liberalPartyAlignment.setAlignment(Dimension.AUTHORITARIAN_DEMOCRATIC, .8);
        liberalPartyAlignment.setAlignment(Dimension.INTERNATIONAL_NATIONAL, .2);
        liberalPartyAlignment.setAlignment(Dimension.LIBERAL_CONSERVATIVE, -.5);
        liberalPartyAlignment.setAlignment(Dimension.FOSSILE_GREEN, -.3);
        liberalPartyAlignment.setAlignment(Dimension.SOCIAL_INDIVIDUAL, -.1);
        liberalPartyAlignment.setAlignment(Dimension.RELIGIOUS_SECULAR, -.2);
        liberalPartyAlignment.setAlignment(Dimension.UNIONIST_SEPARATIST, -.5);
        LIBERAL_PARTY = new Party(liberalPartyAlignment, "Donkey Party", "DON",
                new Color(0x0066ee));

        Alignment greenPartyAlignment = new Alignment();
        greenPartyAlignment.setAlignment(Dimension.AUTHORITARIAN_DEMOCRATIC, .9);
        greenPartyAlignment.setAlignment(Dimension.INTERNATIONAL_NATIONAL, -.5);
        greenPartyAlignment.setAlignment(Dimension.LIBERAL_CONSERVATIVE, -.3);
        greenPartyAlignment.setAlignment(Dimension.FOSSILE_GREEN, 1.);
        greenPartyAlignment.setAlignment(Dimension.SOCIAL_INDIVIDUAL, -.6);
        greenPartyAlignment.setAlignment(Dimension.RELIGIOUS_SECULAR, .5);
        greenPartyAlignment.setAlignment(Dimension.UNIONIST_SEPARATIST, -.4);
        GREEN_PARTY = new Party(greenPartyAlignment, "Nature Party", "NAT",
                new Color(0x009944));


        Alignment communistPartyAlignment = new Alignment();
        communistPartyAlignment.setAlignment(Dimension.AUTHORITARIAN_DEMOCRATIC, -.9);
        communistPartyAlignment.setAlignment(Dimension.INTERNATIONAL_NATIONAL, -.5);
        communistPartyAlignment.setAlignment(Dimension.LIBERAL_CONSERVATIVE, 0);
        communistPartyAlignment.setAlignment(Dimension.FOSSILE_GREEN, -.6);
        communistPartyAlignment.setAlignment(Dimension.SOCIAL_INDIVIDUAL, -1.);
        communistPartyAlignment.setAlignment(Dimension.RELIGIOUS_SECULAR, .6);
        communistPartyAlignment.setAlignment(Dimension.UNIONIST_SEPARATIST, -.9);
        COMMUNIST_PARTY = new Party(communistPartyAlignment, "Communist Party", "COM",
                new Color(0x880044));

        //0xeecc00 YELLOW
    }

    public Party(Alignment alignment, String name, String shortName, java.awt.Color color) {
        this.alignment = alignment;
        this.name = name;
        this.shortName = shortName;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public String getShortName() {
        return shortName;
    }

    public String getName() {
        return name;
    }

    public Alignment getAlignment() {
        return alignment;
    }

}
