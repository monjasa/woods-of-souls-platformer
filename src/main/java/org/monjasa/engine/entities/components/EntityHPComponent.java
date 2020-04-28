package org.monjasa.engine.entities.components;

import com.almasb.fxgl.entity.components.IntegerComponent;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class EntityHPComponent extends IntegerComponent {

    private IntegerProperty maxValueProperty;

    public EntityHPComponent(int initialValue) {
        super(initialValue);
        maxValueProperty = new SimpleIntegerProperty(initialValue);
    }

    public void changeValue(int difference) {
        setValue(Math.max(getValue() + difference, 0));
    }

    public void expandValue(int difference) {
        setMaxValue(Math.max(getMaxValue() + difference, 0));
        changeValue(difference);
    }

    public IntegerProperty maxValueProperty() {
        return maxValueProperty;
    }

    public int getMaxValue() {
        return maxValueProperty.getValue();
    }

    public void setMaxValue(int maxValue) {
        this.maxValueProperty.setValue(maxValue);
    }
}
