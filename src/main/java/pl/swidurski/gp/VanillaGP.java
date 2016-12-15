package pl.swidurski.gp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import pl.swidurski.gp.initialization.FullMethod;
import pl.swidurski.gp.initialization.InitializationMethod;
import pl.swidurski.gp.operators.Variable;

/**
 * Created by student on 11.12.2016.
 */

@Data
public class VanillaGP implements GeneticAlgorithm {

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private int iteration = 0;

    private final Variable dependentVariable;
    private int iterationLimit = 100;
    private int populationSize = 10;
    private int tournamentSize = 10;
    private int mutationPercent = 5;
    private int crossoverPercent = 70;
    private Population population;
    private InstructionSet instructionSet;
    private Fitness fitnessFunction;
    private StopCondition stopCondition;
    private Selection selectionMethod;
    private Crossover crossoverMethod;
    private Mutation mutationMethod;
    private InitializationMethod initializationMethod;
    private UpdateEvent updateEventHandler;

    public VanillaGP(int populationSize, int iterationLimit, Variable dependentVariable) {
        this.populationSize = populationSize;
        this.iterationLimit = iterationLimit;
        this.dependentVariable = dependentVariable;
        instructionSet = new DefaultInstructionSet();
        stopCondition = new IterationLimit(iterationLimit);
        selectionMethod = new TournamentSelection(populationSize / 2, 2);
        crossoverMethod = new SubtreeSwappingCrossover();
        mutationMethod = new RandomSubtreeMutation();
        initializationMethod = new FullMethod();
    }

    @Override
    public Individual start() {
        Individual champion;
        population = initialize();
        do {
            calculateFitness(population);
            Population newPopulation = new Population();
            do {
                Population p1 = select(population);
                Population p2 = select(population);

                Population result = new Population();
                if (RandomHelper.getRandomIntLowerThan(100) < mutationPercent) {
                    result.add(mutation(p1, p2));
                }
                if (RandomHelper.getRandomIntLowerThan(100) < crossoverPercent) {
                    result.add(crossover(p1, p2));
                }
                if (result.isEmpty()) {
                    result.add(p1);
                    result.add(p2);
                }

                newPopulation.add(result);
            }
            while (newPopulation.size() < populationSize);
            calculateFitness(newPopulation);
            iteration++;
            champion = findChampion(newPopulation);
            update(newPopulation, champion, iteration);
        }
        while (!stop(champion, null));
        return champion;
    }

    private void calculateFitness(Population newPopulation) {
        for (Individual p : newPopulation) {
            p.setDependentVariable(dependentVariable);
            p.setIndependentVariables(instructionSet.getVariables());
            fitnessFunction.calculateAndAssign(p);
        }
    }

    private void update(Population population, Individual champion, int iteration) {
        if (updateEventHandler != null) {
            updateEventHandler.fire(population, champion, iteration);
        }
    }

    @Override
    public Population initialize() {
        return initializationMethod.initialize(this);
    }

    @Override
    public Population select(Population population) {
        return selectionMethod.select(population);
    }

    @Override
    public Population crossover(Population first, Population second) {
        return crossoverMethod.crossover(this, first, second);
    }

    @Override
    public Population mutation(Population first, Population second) {
        return mutationMethod.mutate(this, first, second);
    }

    @Override
    public boolean stop(Individual champion, Population population) {
        return stopCondition.stop(champion, iteration);
    }

    @Override
    public Individual findChampion(Population population) {
        Double min = null;
        Individual champion = null;
        for (Individual individual : population) {
            if (min == null || individual.getFitness() < min) {
                min = individual.getFitness();
                champion = individual;
            }
        }
        return champion;
    }


}
