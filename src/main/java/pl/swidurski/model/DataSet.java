package pl.swidurski.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by student on 23.10.2016.
 */
public class DataSet {

    @Getter
    private List<Attribute> attributes = new ArrayList<>();

    private HashMap<String, Attribute> stringIndex = new HashMap<>();
    private HashMap<Integer, Attribute> integerIndex = new HashMap<>();


    @Getter
    private Attribute result;

    public void addAttribute(Attribute attribute) {
        if (!attributes.contains(attribute)) {
            attributes.add(attribute);
            integerIndex.put(attribute.getIndex(), attribute);
            stringIndex.put(attribute.getName(), attribute);

            if (attribute.isResult()) {
                this.result = attribute;
            }
        }
    }

    public void removeAttribute(Attribute attribute) {
        if (attributes.contains(attribute) && !attribute.equals(result)) {
            attributes.remove(attribute);
            integerIndex.remove(attribute.getIndex());
            stringIndex.remove(attribute.getName());
        }
    }

    public Attribute getAttribute(int index) {
        return integerIndex.get(index);
    }


    public Attribute getAttribute(String attributeName) {
        return stringIndex.get(attributeName);
    }


    public int getNumberOfAttributes() {
        return attributes.size();
    }


    public int getNumberOfNonResultAttributest() {
        return getNumberOfAttributes() - 1;
    }

    public void removeRow(int row) {
        for (Attribute attribute : getAttributes()) {
            attribute.getValues().remove(attribute.getValues().get(row));
        }
    }


    public DataSet getSubSet(Attribute attribute, String value) {
        DataSet dataSet = new DataSet();

        List<Integer> indexes = getIndexes(attribute, value);

        for (Attribute a : getAttributes()) {
            Attribute newAttribute = new Attribute(a.getName());
            newAttribute.setIndex(a.getIndex());
            newAttribute.setResult(a.isResult());

            for (int i = 0; i < a.getValues().size(); i++) {
                if (indexes.contains(i))
                    newAttribute.addValue(a.getValues().get(i));
            }
            dataSet.addAttribute(newAttribute);
        }
        return dataSet;
    }

    public List<Integer> getIndexes(Attribute attribute, String value) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < attribute.getValues().size(); i++) {
            if (attribute.getValues().get(i).equals(value)) {
                indexes.add(i);
            }
        }
        return indexes;
    }


    public List<String> getRow(int index) {
        return getAttributes().stream().map(attribute -> attribute.getValues().get(index)).collect(Collectors.toList());
    }

}
