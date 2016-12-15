package pl.swidurski.gp;

import pl.swidurski.gp.initialization.InitializationMethod;

/**
 * Created by student on 11.12.2016.
 */
public interface GeneticAlgorithm {
    //<editor-fold desc="Getters">
    int getPopulationSize();

    Population getPopulation();

    InstructionSet getInstructionSet();

    Fitness getFitnessFunction();

    StopCondition getStopCondition();

    Selection getSelectionMethod();

    Crossover getCrossoverMethod();

    Mutation getMutationMethod();

    InitializationMethod getInitializationMethod();
    //</editor-fold>

    //<editor-fold desc="Setters">
    void setPopulationSize(int size);

    void setPopulation(Population population);

    void setInstructionSet(InstructionSet instructionSet);

    void setFitnessFunction(Fitness fitness);

    void setStopCondition(StopCondition stopCondition);

    void setSelectionMethod(Selection selection);

    void setCrossoverMethod(Crossover crossoverMethod);

    void setMutationMethod(Mutation mutationMethod);

    void setInitializationMethod(InitializationMethod initializationMethod);
    //</editor-fold>

    //<editor-fold desc="Methods">
    Individual start();

    Population initialize();

    Population select(Population population);

    Population crossover(Population first, Population second);

    Population mutation(Population first, Population second);

    boolean stop(Individual champion, Population population);

    Individual findChampion(Population population);
    //</editor-fold>

    void setUpdateEventHandler(UpdateEvent updateEvent);
}
