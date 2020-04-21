package org.monjasa.engine.levels;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.LevelLoader;
import com.almasb.fxgl.entity.level.tiled.*;
import javafx.scene.paint.Color;
import org.joou.UInteger;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.InflaterInputStream;

public class PlatformerTMXLevelLoader implements LevelLoader {

    @Override
    public Level load(URL url, GameWorld gameWorld) {

        try {
            TiledMap map = parse(url.openStream());
            TilesetLoader tilesetLoader = new TilesetLoader(map, url);
            List<Entity> tileLayerEntities = createTileLayerEntities(map, tilesetLoader);
            List<Entity> objectEntities = createObjectLayerEntities(map, tilesetLoader, gameWorld);

            Level level = new Level(
                    map.getWidth() * map.getTilewidth(),
                    map.getHeight() * map.getTileheight(),
                    Stream.of(tileLayerEntities, objectEntities).flatMap(Collection::stream).collect(Collectors.toList()));

            map.getProperties().forEach(level.getProperties()::setValue);

            return level;

        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private List<Entity> createTileLayerEntities(TiledMap map, TilesetLoader tilesetLoader) {

        return map.getLayers().stream()
                .filter(layer -> layer.getType().equals("tilelayer"))
                .map(layer -> {
                    Entity layerEntity = new Entity();
                    layerEntity.getViewComponent().addChild(tilesetLoader.loadView(layer.getName()));
                    return layerEntity;
                }).collect(Collectors.toList());
    }

    private List<Entity> createObjectLayerEntities(TiledMap map, TilesetLoader tilesetLoader, GameWorld world) {
        return map.getLayers().stream()
                .filter(layer -> layer.getType().equals("objectgroup"))
                .flatMap(layer -> layer.getObjects().stream())
                .map(tiledObject -> {

                    SpawnData data = new SpawnData(
                            tiledObject.getX(),
                            tiledObject.getY() - (tiledObject.getGid() == 0 ? 0 : tiledObject.getHeight())
                    );

                    data.put("name", tiledObject.getName());
                    data.put("type", tiledObject.getType());
                    data.put("width", tiledObject.getWidth());
                    data.put("height", tiledObject.getHeight());
                    data.put("rotation", tiledObject.getRotation());
                    data.put("id", tiledObject.getId());
                    data.put("gid", tiledObject.getGid());

                    tiledObject.getProperties().forEach(data::put);

                    Entity entity = world.create(tiledObject.getType(), data);
                    data.getData().forEach(entity::setProperty);

                    entity.addComponent(new IDComponent(tiledObject.getName(), tiledObject.getId()));

                    entity.setPosition(data.getX(), data.getY());
                    entity.setRotation(tiledObject.getRotation());

                    if (tiledObject.getGid() != 0) {
                        entity.getViewComponent().addChild(tilesetLoader.loadView(
                                tiledObject.getGid(),
                                tiledObject.isFlippedHorizontal(),
                                tiledObject.isFlippedVertical()
                        ));
                    }

                    return entity;
                }).collect(Collectors.toList());
    }

    public TiledMap parse(InputStream inputStream) throws XMLStreamException {

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(inputStream, "UTF-8");

        TiledMap map = new TiledMap();
        ArrayList<Layer> layers = new ArrayList<>();
        ArrayList<Tileset> tilesets = new ArrayList<>();

        Layer currentLayer = new Layer();
        Tileset currentTileset = new Tileset();
        Tile currentTile = new Tile();
        TiledObject currentObject = new TiledObject();

        boolean insideTileTag = false;
        boolean mapPropertiesFinished = false;

        while (eventReader.hasNext()) {

            XMLEvent event = eventReader.nextEvent();

            if (event.isStartElement()) {
                StartElement start = event.asStartElement();

                switch (start.getName().getLocalPart()) {
                    case "map":
                        parseMap(map, start);
                        break;
                    case "tileset":
                        currentTileset = new Tileset();
                        parseTileset(currentTileset, start);
                        break;
                    case "tile":
                        currentTile = new Tile();
                        parseTile(currentTile, start);
                        insideTileTag = true;
                        break;
                    case "image":
                        if (insideTileTag) {
                            parseImage(currentTile, start);
                        } else {
                            parseImage(currentTileset, start);
                        }
                        break;
                    case "layer":
                        currentLayer = new Layer();
                        parseTileLayer(currentLayer, start);
                        break;
                    case "data":
                        parseData(currentLayer, eventReader.getElementText(), start);
                        break;
                    case "objectgroup":
                        currentLayer = new Layer();
                        parseObjectGroupLayer(currentLayer, start);
                        break;
                    case "object":
                        mapPropertiesFinished = true;

                        currentObject = new TiledObject();
                        parseObject(currentLayer, currentObject, start);
                        break;
                    case "property":
                        if (mapPropertiesFinished) {
                            parseObjectProperty(currentObject, start);
                        } else {
                            parseMapProperty(map, start);
                        }
                        break;
                    //TODO polygon
                }
            }

            if (event.isEndElement()) {
                EndElement endElement = event.asEndElement();

                switch (endElement.getName().getLocalPart()) {
                    case "tileset":
                        tilesets.add(currentTileset);
                        break;
                    case "tile":
                        currentTileset.getTiles().add(currentTile);
                        insideTileTag = false;
                        break;
                    case "layer":
                    case "objectgroup":
                        layers.add(currentLayer);
                        break;
                }
            }
        }

        map.setLayers(layers);
        map.setTilesets(tilesets);

        return map;
    }

    private void parseMap(TiledMap map, StartElement start) {
        map.setWidth(getIntAttribute("width", start));
        map.setHeight(getIntAttribute("height", start));
        map.setTilewidth(getIntAttribute("tilewidth", start));
        map.setTileheight(getIntAttribute("tileheight", start));
        map.setNextobjectid(getIntAttribute("nextobjectid", start));

        map.setType("map");
        map.setVersion(1);
        map.setInfinite(getIntAttribute("infinite", start) == 1);
        map.setBackgroundcolor(getStringAttribute("backgroundcolor", start));
        map.setOrientation(getStringAttribute("orientation", start));
        map.setRenderorder(getStringAttribute("renderorder", start));
        map.setTiledversion(getStringAttribute("tiledversion", start));
    }

    private void parseTileset(Tileset tileset, StartElement start) {
        tileset.setFirstgid(getIntAttribute("firstgid", start));
        tileset.setName(getStringAttribute("name", start));
        tileset.setTilewidth(getIntAttribute("tilewidth", start));
        tileset.setTileheight(getIntAttribute("tileheight", start));
        tileset.setSpacing(getIntAttribute("spacing", start));
        tileset.setTilecount(getIntAttribute("tilecount", start));
        tileset.setColumns(getIntAttribute("columns", start));
    }

    private void parseTile(Tile tile, StartElement start) {
        tile.setId(getIntAttribute("id", start));
    }

    private void parseImage(Tileset tileset, StartElement start) {
        tileset.setImage(getStringAttribute("source", start));
        tileset.setImagewidth(getIntAttribute("width", start));
        tileset.setImageheight(getIntAttribute("height", start));
        tileset.setTransparentcolor(getStringAttribute("trans", start));
    }

    private void parseImage(Tile tile, StartElement start) {
        tile.setImage(getStringAttribute("source", start));
        tile.setImagewidth(getIntAttribute("width", start));
        tile.setImageheight(getIntAttribute("height", start));
        tile.setTransparentcolor(getStringAttribute("trans", start));
    }

    private void parseTileLayer(Layer layer, StartElement start) {
        layer.setType("tilelayer");
        layer.setName(getStringAttribute("name", start));
        layer.setWidth(getIntAttribute("width", start));
        layer.setHeight(getIntAttribute("height", start));
        layer.setOpacity(getFloatAttribute("opacity", start));
        layer.setVisible(getIntAttribute("visible", start) == 1);
    }

    private void parseData(Layer layer, String data, StartElement start) {

        switch (getStringAttribute("encoding", start)) {

            case "csv":
                layer.setData(Arrays.stream(data.replace("\n", "").split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList()));
                return;

            case "base64":

                System.err.println("TO DO.........");

                byte[] bytes = Base64.getDecoder().decode(data.trim());
                switch (getStringAttribute("compresiion", start)) {
                    case "zlib":
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        InflaterInputStream inflaterInputStream = new InflaterInputStream(new ByteArrayInputStream(bytes));
                        break;
                    case "gzip":
                        break;
                }

                return;

            default:
                throw new RuntimeException();
        }
    }

    private void parseObjectGroupLayer(Layer layer, StartElement start) {
        layer.setType("objectgroup");
        layer.setName(getStringAttribute("name", start));
    }

    private void parseObject(Layer layer, TiledObject obj, StartElement start) {

        obj.setName(getStringAttribute("name", start));
        obj.setType(getStringAttribute("type", start));
        obj.setId(getIntAttribute("id", start));
        obj.setX(getIntAttribute("x", start));
        obj.setY(getIntAttribute("y", start));
        obj.setRotation(getFloatAttribute("rotation", start));
        obj.setWidth(getIntAttribute("width", start));
        obj.setHeight(getIntAttribute("height", start));

        UInteger gidUInt = getUIntAttribute("gid", start);

        UInteger FLIPPED_HORIZONTALLY_FLAG = UInteger.valueOf(1 << 31);
        UInteger FLIPPED_VERTICALLY_FLAG = UInteger.valueOf(1 << 30);
        UInteger FLIPPED_DIAGONALLY_FLAG = UInteger.valueOf(1 << 29);

        obj.setFlippedHorizontal((gidUInt.longValue() & FLIPPED_HORIZONTALLY_FLAG.longValue()) != 0);
        obj.setFlippedVertical((gidUInt.longValue() & FLIPPED_VERTICALLY_FLAG.longValue()) != 0);

        UInteger GID_FLAGS = UInteger.valueOf(FLIPPED_DIAGONALLY_FLAG.longValue() | FLIPPED_HORIZONTALLY_FLAG.longValue() | FLIPPED_VERTICALLY_FLAG.longValue());

        int gid = UInteger.valueOf(gidUInt.longValue() & (~GID_FLAGS.longValue())).intValue();

        obj.setGid(gid);
        layer.getObjects().add(obj);
    }

    private void parseObjectProperty(TiledObject obj, StartElement start) {

        String propName = getStringAttribute("name", start);
        String propType = getStringAttribute("type", start);

        // todo cast
        switch (propType) {
            case "int":
                obj.getProperties().put(propName, getIntAttribute("value", start));
                break;
            case "bool":
                obj.getProperties().put(propName, getBoolAttribute("value", start));
                break;
            case "float":
                obj.getProperties().put(propName, getFloatAttribute("value", start));
                break;
            case "string":
                obj.getProperties().put(propName, getStringAttribute("value", start));
                break;
            case "color":
                obj.getProperties().put(propName, getColorAttribute("value", start));
                break;
            default:
                throw new RuntimeException("parseObjectProperty");
        }
    }

    private void parseMapProperty(TiledMap map, StartElement start) {

        String propName = getStringAttribute("name", start);
        String propType = getStringAttribute("type", start);

        map.getPropertytypes().put(propName, propType);
        // todo cast
        switch (propType) {
            case "int":
                map.getProperties().put(propName, getIntAttribute("value", start));
                break;
            case "bool":
                map.getProperties().put(propName, getBoolAttribute("value", start));
                break;
            case "float":
                map.getProperties().put(propName, getFloatAttribute("value", start));
                break;
            case "string":
                map.getProperties().put(propName, getStringAttribute("value", start));
                break;
            case "color":
                map.getProperties().put(propName, getColorAttribute("value", start));
                break;
            default:
                throw new RuntimeException("parseMapProperty");
        }
    }

    private Color getColorAttribute(String attrName, StartElement startElement) {
        return Color.web(getStringAttribute(attrName, startElement));
    }


    private boolean getBoolAttribute(String attrName, StartElement startElement) {
        return Boolean.parseBoolean(getStringAttribute(attrName, startElement));
    }

    private float getFloatAttribute(String attrName, StartElement startElement) {

        Attribute attribute = startElement.getAttributeByName(new QName(attrName));
        if (attribute != null)
            return Float.parseFloat(getStringAttribute(attrName, startElement));
        else
            return 0.0f;
    }

    private int getIntAttribute(String attrName, StartElement startElement) {

        Attribute attribute = startElement.getAttributeByName(new QName(attrName));

        assert attribute != null;
        try {
            return Integer.parseInt(getStringAttribute(attrName, startElement));
        } catch (NumberFormatException exception) {
            return (int) getFloatAttribute(attrName, startElement);
        }
    }

    private UInteger getUIntAttribute(String attrName, StartElement startElement) {

        Attribute attribute = startElement.getAttributeByName(new QName(attrName));

        if (attribute != null) {
            return UInteger.valueOf(attribute.getValue());
        } else {
            return UInteger.valueOf(0);
        }

    }

    private String getStringAttribute(String attrName, StartElement startElement) {

        Attribute attribute = startElement.getAttributeByName(new QName(attrName));
        if (attribute != null)
            return attribute.getValue();
        else
            return "";
    }
}


