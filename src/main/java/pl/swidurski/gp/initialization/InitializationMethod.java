package pl.swidurski.gp.initialization;

import pl.swidurski.gp.GeneticAlgorithm;
import pl.swidurski.gp.Individual;
import pl.swidurski.gp.InstructionSet;
import pl.swidurski.gp.Population;

/**
 * Created by student on 11.12.2016.
 */
public interface InitializationMethod {

    void setInstructionSet(InstructionSet instructionSet);

    Population initialize(GeneticAlgorithm geneticAlgorithm);

    Individual create();
}
