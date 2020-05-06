package org.monjasa.engine.util;

import java.lang.reflect.Array;
import java.util.Arrays;

public class CircularQueue<T> {

    private int currentSize;
    private int capacity;
    private T[] circularQueueElements;

    private int rearPosition;
    private int frontPosition;

    @SuppressWarnings("unchecked")
    public CircularQueue(Class<T> objectClass, int capacity) {
        this.capacity = capacity;
        circularQueueElements = (T[]) Array.newInstance(objectClass, capacity);
        currentSize = 0;
        frontPosition = -1;
        rearPosition = -1;
    }

    public void addElement(T element) throws FullCircularQueueException {

        if (isFull()) {
            throw new FullCircularQueueException("Circular Queue is full. Element cannot be added");
        }

        rearPosition = (rearPosition + 1) % circularQueueElements.length;
        circularQueueElements[rearPosition] = element;
        currentSize++;

        if (frontPosition == -1) {
            frontPosition = rearPosition;
        }
    }

    public T peekElement() throws EmptyCircularQueueException {

        if (isEmpty()) {
            throw new EmptyCircularQueueException("Circular Queue is empty. Element cannot be retrieved");
        }

        return circularQueueElements[frontPosition];
    }

    public T peekNextElement() throws EmptyCircularQueueException {

        T retrievedElement;

        if (isEmpty()) {
            throw new EmptyCircularQueueException("Circular Queue is empty. Element cannot be retrieved");
        }

        retrievedElement = circularQueueElements[frontPosition];
        frontPosition = (frontPosition + 1) % circularQueueElements.length;

        return retrievedElement;
    }

    public T pullElement() throws EmptyCircularQueueException {

        T retrievedElement;

        if (isEmpty()) {
            throw new EmptyCircularQueueException("Circular Queue is empty. Element cannot be retrieved");
        }

        retrievedElement = circularQueueElements[frontPosition];
        circularQueueElements[frontPosition] = null;
        frontPosition = (frontPosition + 1) % circularQueueElements.length;
        currentSize--;

        return retrievedElement;
    }

    public boolean isFull() {
        return (currentSize == circularQueueElements.length);
    }

    public boolean isEmpty() {
        return (currentSize == 0);
    }

    @Override
    public String toString() {
        return "CircularQueue [" + Arrays.toString(circularQueueElements) + "]";
    }
}
