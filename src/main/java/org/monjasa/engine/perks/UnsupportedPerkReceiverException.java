package org.monjasa.engine.perks;

import java.util.Optional;

public class UnsupportedPerkReceiverException extends RuntimeException {

    private Object receiver;
    private Object object;

    public UnsupportedPerkReceiverException(Object receiver) {
        this.receiver = receiver;
        this.object = Optional.empty();
    }

    public UnsupportedPerkReceiverException(Object receiver, Object object) {
        this.receiver = receiver;
        this.object = object;
    }

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();
        result.append("UnsupportedPerkReceiverException{" + "receiver=").append(receiver);

        if (object != null) result.append(", object=").append(object);

        return result.append('}').toString();
    }

    public Object getReceiver() {
        return receiver;
    }

    public Optional<?> getObject() {
        return Optional.ofNullable(object);
    }
}
