package org.monjasa.engine.entities.components;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.entity.component.SerializableComponent;
import com.almasb.fxgl.entity.components.IntegerComponent;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class EntityHPComponent extends IntegerComponent implements SerializableComponent {

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

    @Override
    public void read(Bundle bundle) {
        setValue(bundle.get(getClass().getSimpleName() + ".value"));
        setMaxValue(bundle.get(getClass().getSimpleName() + ".maxValue"));
    }

    @Override
    public void write(Bundle bundle) {
        bundle.put(getClass().getSimpleName() + ".value", getValue());
        bundle.put(getClass().getSimpleName() + ".maxValue", getMaxValue());
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
