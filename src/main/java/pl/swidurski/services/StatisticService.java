package pl.swidurski.services;

import pl.swidurski.model.Condition;
import pl.swidurski.model.DataSet;
import pl.swidurski.model.Rule;

import java.util.List;

/**
 * Created by Krystek on 2016-11-07.
 */
public class StatisticService {

    private final DataSet dataSet;
    private final List<Rule> rules;

    public StatisticService(DataSet dataSet, List<Rule> rules) {
        this.dataSet = dataSet;
        this.rules = rules;
    }


    public void calculateStatistics() {
        for (Rule rule : rules) {
            int matchesXandY = 0;
            int matchesX = 0;
            for (int i = 0; i < dataSet.getResult().getValues().size(); i++) {
                boolean match = true;
                for (Condition condition : rule.getConditions()) {
                    String attr = getAttribute(condition.getAttributeName(), i);
                    if (!attr.equals(condition.getValue())) {
                        match = false;
                        break;
                    }
                }

                if (match){
                    matchesX++;
                }

                if (!getResult(i).equals(rule.getResult().getValue())) {
                    match = false;
                }

                if (match) {
                    matchesXandY++;
                    rule.getSupportingIndexes().add(i);
                }
            }
            rule.setSupport((matchesXandY / (double) dataSet.getResult().getValues().size()));
            rule.setConfidence((matchesXandY / (double) matchesX));
        }
    }

    private String getResult(int i) {
        return dataSet.getResult().getValues().get(i);
    }

    private String getAttribute(String name, int i) {
        return dataSet.getAttribute(name).getValues().get(i);
    }

}

