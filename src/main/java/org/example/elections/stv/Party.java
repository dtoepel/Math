package org.example.elections.stv;

import java.awt.*;

public class Party implements Aligned {
    private final Alignment alignment;
    private final String name;
    private final String shortName;
    private final java.awt.Color color;

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
