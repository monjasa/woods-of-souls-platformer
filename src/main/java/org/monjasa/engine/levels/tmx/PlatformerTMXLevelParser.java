package org.monjasa.engine.levels.tmx;

import com.almasb.fxgl.entity.level.tiled.*;
import org.joou.UInteger;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Collectors;
import java.util.zip.InflaterInputStream;

import static org.monjasa.engine.levels.tmx.PlatformerTMXObjectType.*;

class PlatformerTMXLevelParser {

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
                    // TODO: implement polygons
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

    private void parseMap(TiledMap map, StartElement startElement) {

        PlatformerTMXAttributeService mapService = new PlatformerTMXAttributeService(startElement);

        map.setType(MAP.getValue());
        map.setVersion(1);

        map.setWidth(mapService.getIntAttribute("width"));
        map.setHeight(mapService.getIntAttribute("height"));
        map.setTilewidth(mapService.getIntAttribute("tilewidth"));
        map.setTileheight(mapService.getIntAttribute("tileheight"));
        map.setNextobjectid(mapService.getIntAttribute("nextobjectid"));

        map.setInfinite(mapService.getIntAttribute("infinite") == 1);
        map.setBackgroundcolor(mapService.getStringAttribute("backgroundcolor"));
        map.setOrientation(mapService.getStringAttribute("orientation"));
        map.setRenderorder(mapService.getStringAttribute("renderorder"));
        map.setTiledversion(mapService.getStringAttribute("tiledversion"));
    }

    private void parseTileset(Tileset tileset, StartElement startElement) {

        PlatformerTMXAttributeService tilesetService = new PlatformerTMXAttributeService(startElement);

        tileset.setFirstgid(tilesetService.getIntAttribute("firstgid"));
        tileset.setName(tilesetService.getStringAttribute("name"));
        tileset.setTilewidth(tilesetService.getIntAttribute("tilewidth"));
        tileset.setTileheight(tilesetService.getIntAttribute("tileheight"));
        tileset.setSpacing(tilesetService.getIntAttribute("spacing"));
        tileset.setTilecount(tilesetService.getIntAttribute("tilecount"));
        tileset.setColumns(tilesetService.getIntAttribute("columns"));
    }

    private void parseTile(Tile tile, StartElement startElement) {
        PlatformerTMXAttributeService tileService = new PlatformerTMXAttributeService(startElement);
        tile.setId(tileService.getIntAttribute("id"));
    }

    private void parseImage(Tileset tileset, StartElement startElement) {

        PlatformerTMXAttributeService imageService = new PlatformerTMXAttributeService(startElement);

        tileset.setImage(imageService.getStringAttribute("source"));
        tileset.setImagewidth(imageService.getIntAttribute("width"));
        tileset.setImageheight(imageService.getIntAttribute("height"));
        tileset.setTransparentcolor(imageService.getStringAttribute("trans"));
    }

    private void parseImage(Tile tile, StartElement startElement) {

        PlatformerTMXAttributeService imageService = new PlatformerTMXAttributeService(startElement);

        tile.setImage(imageService.getStringAttribute("source"));
        tile.setImagewidth(imageService.getIntAttribute("width"));
        tile.setImageheight(imageService.getIntAttribute("height"));
        tile.setTransparentcolor(imageService.getStringAttribute("trans"));
    }

    private void parseTileLayer(Layer layer, StartElement startElement) {

        PlatformerTMXAttributeService tileLayerService = new PlatformerTMXAttributeService(startElement);

        layer.setType(TILE_LAYER.getValue());

        layer.setName(tileLayerService.getStringAttribute("name"));
        layer.setWidth(tileLayerService.getIntAttribute("width"));
        layer.setHeight(tileLayerService.getIntAttribute("height"));
        layer.setOpacity(tileLayerService.getFloatAttribute("opacity"));
        layer.setVisible(tileLayerService.getIntAttribute("visible") == 1);
    }

    //TODO: finish the method body

    private void parseData(Layer layer, String data, StartElement startElement) {

        PlatformerTMXAttributeService dataService = new PlatformerTMXAttributeService(startElement);

        switch (dataService.getStringAttribute("encoding")) {

            case "csv":
                layer.setData(Arrays.stream(data.replace("\n", "").split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList()));
                return;

            case "base64":

                System.err.println("TO DO.........");

                byte[] bytes = Base64.getDecoder().decode(data.trim());
                switch (dataService.getStringAttribute("compression")) {
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

    private void parseObjectGroupLayer(Layer layer, StartElement startElement) {

        PlatformerTMXAttributeService objectGroupLayerService = new PlatformerTMXAttributeService(startElement);

        layer.setType(OBJECT_GROUP.getValue());

        layer.setName(objectGroupLayerService.getStringAttribute("name"));
    }

    private void parseObject(Layer layer, TiledObject obj, StartElement startElement) {

        PlatformerTMXAttributeService objectService = new PlatformerTMXAttributeService(startElement);

        obj.setName(objectService.getStringAttribute("name"));
        obj.setType(objectService.getStringAttribute("type"));
        obj.setId(objectService.getIntAttribute("id"));
        obj.setX(objectService.getIntAttribute("x"));
        obj.setY(objectService.getIntAttribute("y"));
        obj.setRotation(objectService.getFloatAttribute("rotation"));
        obj.setWidth(objectService.getIntAttribute("width"));
        obj.setHeight(objectService.getIntAttribute("height"));

        UInteger gidUInt = objectService.getUIntAttribute("gid");

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

    private void parseObjectProperty(TiledObject obj, StartElement startElement) {

        PlatformerTMXAttributeService objectPropertyService = new PlatformerTMXAttributeService(startElement);

        String propName = objectPropertyService.getStringAttribute("name");
        String propType = objectPropertyService.getStringAttribute("type");

        obj.getPropertytypes().put(propName, propType);

        switch (propType) {
            case "int":
                obj.getProperties().put(propName, objectPropertyService.getIntAttribute("value"));
                break;
            case "bool":
                obj.getProperties().put(propName, objectPropertyService.getBoolAttribute("value"));
                break;
            case "float":
                obj.getProperties().put(propName, objectPropertyService.getFloatAttribute("value"));
                break;
            case "string":
            case "":
                obj.getProperties().put(propName, objectPropertyService.getStringAttribute("value"));
                break;
            case "color":
                obj.getProperties().put(propName, objectPropertyService.getColorAttribute("value"));
                break;
            default:
                throw new RuntimeException("parseObjectProperty");
        }
    }

    private void parseMapProperty(TiledMap map, StartElement startElement) {

        PlatformerTMXAttributeService mapPropertyService = new PlatformerTMXAttributeService(startElement);

        String propName = mapPropertyService.getStringAttribute("name");
        String propType = mapPropertyService.getStringAttribute("type");

        map.getPropertytypes().put(propName, propType);

        switch (propType) {
            case "int":
                map.getProperties().put(propName, mapPropertyService.getIntAttribute("value"));
                break;
            case "bool":
                map.getProperties().put(propName, mapPropertyService.getBoolAttribute("value"));
                break;
            case "float":
                map.getProperties().put(propName, mapPropertyService.getFloatAttribute("value"));
                break;
            case "string":
                map.getProperties().put(propName, mapPropertyService.getStringAttribute("value"));
                break;
            case "color":
                map.getProperties().put(propName, mapPropertyService.getColorAttribute("value"));
                break;
            default:
                throw new RuntimeException("parseMapProperty");
        }
    }
}
