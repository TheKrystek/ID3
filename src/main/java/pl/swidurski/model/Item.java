package pl.swidurski.model;

import lombok.Data;

/**
 * Author: Krystian Świdurski
 */
@Data
public class Item {
    String value;
    Attribute attribute;
    String attributeName;


    public Item(Attribute attribute, String value) {
        this.value = value;
        this.attribute = attribute;
    }

    public String getAttributeName() {
        return attribute.getName();
    }
}