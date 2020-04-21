package org.monjasa.engine.levels.tmx;

public enum PlatformerTMXObjectType {

    MAP("map"),
    TILE_LAYER("tilelayer"),
    OBJECT_GROUP("objectgroup");

    private String value;

    PlatformerTMXObjectType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
