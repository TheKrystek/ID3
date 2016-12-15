package pl.swidurski.gp;

import lombok.Getter;
import pl.swidurski.gp.operators.Function;
import pl.swidurski.gp.operators.Operator;

import java.util.stream.Stream;

/**
 * Author: Krystian Świdurski
 */
public class OperatorNode extends TreeNode<OperatorNode> {

    @Getter
    private Operator operator;


    public OperatorNode(Operator operator) {
        this.operator = operator;
    }

    public Function calculate(Individual individual) {
        return operator.toFunction(individual, this);
    }

    public String asSymbol() {
        return operator.asSymbol(this);
    }


    public Stream<OperatorNode> flattened() {
        return Stream.concat(
                Stream.of(this),
                children.stream().flatMap(OperatorNode::flattened));
    }
}
