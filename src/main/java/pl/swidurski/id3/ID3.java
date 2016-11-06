package pl.swidurski.id3;

import lombok.Getter;
import pl.swidurski.model.*;

import java.util.List;

/**
 * Created by student on 23.10.2016.
 */
public class ID3 {

    private final DataSet dataSet;

    TreeNode root;

    public ID3(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public TreeNode calculate() {
        root = new TreeNode(null, null, null);
        calculate(root, dataSet, null, null);
        return root.getChildren().get(0);
    }

    public void calculate(TreeNode parent, DataSet dataSet, Attribute target, String value) {
        // Pobierz czesc danych dotyczaca tylko docelowego atrybutu
        if (target != null) {
            dataSet.removeAttribute(target);
            dataSet = dataSet.getSubSet(target, value);
        }

        if (!dataSet.getResult().hasDistinctValues()) {
            parent.addChild(new TreeNode(null, dataSet.getResult().getValues().get(0), value));
            return;
        }

        Pair pair = getAttributeWithLowestEntropy(dataSet);
        if (pair == null){
            double sum = dataSet.getResult().getDistinctValues().stream().mapToDouble(p -> p.getCount()).sum();
            Entry mostPossible = null;
            for (Entry entry : dataSet.getResult().getDistinctValues()) {
                if (mostPossible == null || entry.getCount() > mostPossible.getCount()) {
                    mostPossible = entry;
                }
            }
            String nodeText = String.format("%s (%.1f%%)", mostPossible.getString(), mostPossible.getCount() / sum * 100);
            parent.addChild(new TreeNode(null, nodeText, value));
            return;
        }


        Attribute bestAttribute = pair.getAttribute();
        if (dataSet.getNumberOfNonResultAttributest() == 0) {
            TreeNode node = new TreeNode(null, bestAttribute.getName(), value);
            node.setInfo(pair.getInfo());
            parent.addChild(node);
            return;
        }

        TreeNode node = new TreeNode(bestAttribute, bestAttribute.getName(), value);
        node.setInfo(pair.getInfo());
        parent.addChild(node);
        for (Entry entry : bestAttribute.getDistinctValues()) {
            calculate(node, dataSet, bestAttribute, entry.getString());
        }

    }


    private Pair getAttributeWithLowestEntropy(DataSet dataSet) {
        Info info = new Info();
        Attribute bestAttribute = null;
        Double minEntropy = null;

        //
        info.setEntropy(calcEntropy(dataSet.getResult()));

        for (Attribute attribute : dataSet.getAttributes()) {
            if (attribute.isResult())
                continue;

            List<Entry> distinctValues = attribute.getDistinctValues();
            double conditionalEntropy = 0;

            for (Entry value : distinctValues) {
                double P = getProbability(attribute, value);
                DataSet subSet = dataSet.getSubSet(attribute, value.getString());
                conditionalEntropy += P * calcEntropy(subSet.getResult());
            }
            if (minEntropy == null || conditionalEntropy < minEntropy) {
                minEntropy = conditionalEntropy;
                bestAttribute = attribute;
            }
        }

        if (bestAttribute == null) {
            return null;
        }

        info.setConditionalEntropy(minEntropy);
        info.setInformationGain(info.getEntropy() - minEntropy);
        return new Pair(bestAttribute, info);
    }


    private double getProbability(Attribute attribute, Entry value) {
        return value.getCount() / (double) attribute.getValues().size();
    }

    public double calcEntropy(Attribute attribute) {
        List<Entry> distinctValues = attribute.getDistinctValues();
        double entropy = 0;
        for (Entry value : distinctValues) {
            double P = getProbability(attribute, value);
            entropy -= P * (Math.log(P) / Math.log(2));
        }
        return entropy;
    }


    class Pair {
        public Pair(Attribute attribute, Info info) {
            this.attribute = attribute;
            this.info = info;
        }

        @Getter
        Attribute attribute;
        @Getter
        Info info;
    }

}
