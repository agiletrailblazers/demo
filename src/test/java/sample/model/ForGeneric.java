package sample.model;

import java.util.List;

public class ForGeneric<T> {
    private List<T> elements;

    private String value;

    public List<T> getElements() {
        return elements;
    }

    public void setElements(List<T> elements) {
        this.elements = elements;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
