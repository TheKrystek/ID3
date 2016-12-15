package pl.swidurski.gui;

import lombok.Getter;
import lombok.Setter;
import pl.swidurski.gp.*;
import pl.swidurski.gp.initialization.RampedHalfAndHalf;
import pl.swidurski.gp.operators.Operator;
import pl.swidurski.gp.operators.Variable;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: Krystian Świdurski
 */
public class GPConfigurer {
    @Getter
    private List<Variable> variables;

    @Setter
    private List<Operator> operators;

    @Setter
    @Getter
    private Variable dependentVariable;

    @Setter
    private int iterationLimit;
    @Setter
    private int populationSize;
    @Setter
    private int mutationPercent;
    @Setter
    private int crossoverPercent;
    @Setter
    private int tournamentSize;
    @Setter
    private int maxDepth;

    public void readFile(File path) throws IOException {
        CSVReader reader = new CSVReader(path);
        variables = reader.read();
        setupDependentVariable(variables);
    }


    private void setupDependentVariable(List<Variable> variables) {
        dependentVariable = variables.get(variables.size() - 1);
    }

    public GeneticAlgorithm prepare() {
        VanillaGP gp = new VanillaGP(populationSize, iterationLimit, dependentVariable);
        gp.setInstructionSet(createInstructionSet());
        gp.setInitializationMethod(new RampedHalfAndHalf(maxDepth, 50));
        gp.setSelectionMethod(new TournamentSelection(tournamentSize, 2));
        gp.setFitnessFunction(new MeanSquaredError());
        gp.setStopCondition(new IterationLimit(iterationLimit));
        gp.setCrossoverMethod(new SubtreeSwappingCrossover());
        gp.setMutationMethod(new RandomSubtreeMutation(maxDepth));
        return gp;
    }

    private InstructionSet createInstructionSet() {
        InstructionSet instructionSet = new DefaultInstructionSet();
        instructionSet.addAll(operators);
        instructionSet.addAll(getIndependentVariablesAsOperators());
        return instructionSet;
    }

    public Collection<Variable> getIndependentVariables() {
        return variables.stream().filter(variable -> !variable.equals(dependentVariable)).collect(Collectors.toCollection(LinkedList::new));
    }

    private Collection<Operator> getIndependentVariablesAsOperators() {
        return variables.stream().filter(variable -> !variable.equals(dependentVariable)).collect(Collectors.toCollection(LinkedList::new));
    }


}
