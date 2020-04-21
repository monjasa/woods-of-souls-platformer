package org.monjasa.engine.levels.tmx;

import javafx.scene.paint.Color;
import org.joou.UInteger;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

class PlatformerTMXAttributeService {

    private static String EMPTY_ATTRIBUTE = "";

    private StartElement startElement;

    PlatformerTMXAttributeService(StartElement startElement) {
        this.startElement = startElement;
    }

    Color getColorAttribute(String attributeName) {
        return Color.web(getStringAttribute(attributeName));
    }


    boolean getBoolAttribute(String attributeName) {
        return Boolean.parseBoolean(getStringAttribute(attributeName));
    }

    float getFloatAttribute(String attributeName) {
        String attributeValue = getStringAttribute(attributeName);
        if (attributeValue.equals(EMPTY_ATTRIBUTE)) return 0.0f;
        else return Float.parseFloat(getStringAttribute(attributeName));
    }

    int getIntAttribute(String attributeName) {
        try {
            return Integer.parseInt(getStringAttribute(attributeName));
        } catch (NumberFormatException exception) {
            return (int) getFloatAttribute(attributeName);
        }
    }

    UInteger getUIntAttribute(String attributeName) {
        String attributeValue = getStringAttribute(attributeName);
        if (attributeValue.equals(EMPTY_ATTRIBUTE)) return UInteger.valueOf(0);
        else return UInteger.valueOf(attributeValue);
    }

    String getStringAttribute(String attributeName) {
        Attribute attribute = startElement.getAttributeByName(new QName(attributeName));
        if (attribute != null) return attribute.getValue();
        else return EMPTY_ATTRIBUTE;
    }
}
