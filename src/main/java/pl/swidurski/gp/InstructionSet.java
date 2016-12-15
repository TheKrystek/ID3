package pl.swidurski.gp;

import lombok.Getter;
import pl.swidurski.gp.operators.Operator;
import pl.swidurski.gp.operators.Terminal;
import pl.swidurski.gp.operators.Variable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by student on 11.12.2016.
 */
public abstract class InstructionSet {

    @Getter
    protected final List<Operator> operators = new LinkedList<>();
    @Getter
    protected final List<Operator> terminals = new LinkedList<>();
    @Getter
    protected final List<Operator> nonterminals = new LinkedList<>();
    @Getter
    protected final List<Variable> variables = new LinkedList<>();


    public void add(Operator operator) {
        operators.add(operator);
        if (operator instanceof Terminal) {
            if (operator instanceof Variable) {
                variables.add((Variable) operator);
            }
            terminals.add(operator);
        } else {
            nonterminals.add(operator);
        }
    }

    public void addAll(Collection<Operator> operators) {
        operators.forEach(this::add);
    }


    public void addAll(Operator... operators) {
        for (Operator operator : operators) {
            add(operator);
        }
    }

}
