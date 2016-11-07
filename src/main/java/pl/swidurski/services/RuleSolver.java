package pl.swidurski.services;

import lombok.Getter;
import pl.swidurski.model.Condition;
import pl.swidurski.model.Item;
import pl.swidurski.model.Rule;

import java.util.List;
import java.util.Optional;

/**
 * Author: Krystian Świdurski
 */
public class RuleSolver {

    private final List<Rule> rules;


    public RuleSolver(List<Rule> rules) {
        this.rules = rules;
    }

    public Rule solve(List<Item> items){
        for (Rule rule : rules) {
            boolean match = true;
            for (Item item : items) {
                Optional<Condition> condition = rule.getConditions().stream().filter(p -> p.getAttributeName().equals(item.getAttributeName())).findFirst();
                if (!condition.isPresent()) {
                    continue;
                }

                if (!condition.get().match(item.getValue()))
                {
                    match = false;
                    break;
                }
            }
            if (match) {
                return rule;
            }
        }
        return null;
    }

}
