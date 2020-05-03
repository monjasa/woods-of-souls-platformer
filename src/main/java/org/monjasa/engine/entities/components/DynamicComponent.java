package org.monjasa.engine.entities.components;

import com.almasb.fxgl.entity.component.Component;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class DynamicComponent extends Component {

    private DoubleProperty horizontalVelocityProperty;
    private DoubleProperty verticalVelocityProperty;

    public DynamicComponent(double horizontalVelocity, double verticalVelocity) {
        horizontalVelocityProperty = new SimpleDoubleProperty(horizontalVelocity);
        verticalVelocityProperty = new SimpleDoubleProperty(verticalVelocity);
    }

    public DoubleProperty horizontalVelocityProperty() {
        return horizontalVelocityProperty;
    }

    public DoubleProperty verticalVelocityProperty() {
        return verticalVelocityProperty;
    }

    public double getHorizontalVelocity() {
        return horizontalVelocityProperty.getValue();
    }

    public void setHorizontalVelocity(double horizontalVelocity) {
        horizontalVelocityProperty.setValue(horizontalVelocity);
    }

    public double getVerticalVelocity() {
        return verticalVelocityProperty.getValue();
    }

    public void setVerticalVelocity(double verticalVelocity) {
        verticalVelocityProperty.setValue(verticalVelocity);
    }
}
