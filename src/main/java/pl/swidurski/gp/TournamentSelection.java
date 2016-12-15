package pl.swidurski.gp;

import java.util.List;
import java.util.stream.Collectors;

import static pl.swidurski.gp.RandomHelper.getRandomIntLowerThan;

/**
 * Created by student on 11.12.2016.
 */
public class TournamentSelection implements Selection {

    private final int size;
    private final int newPopulationSize;

    public TournamentSelection(int size, int newPopulationSize) {
        this.size = size;
        this.newPopulationSize = newPopulationSize;
    }

    @Override
    public Population select(Population population) {
        Population sample = createRandomSample(population, size);
        List<Individual> sorted = sample.stream().sorted((o1, o2) -> Double.compare(o1.getFitness(), o2.getFitness())).collect(Collectors.toList());

        Population newPopulation = new Population();
        for (int i = 0; i < newPopulationSize; i++) {
            newPopulation.add(sorted.get(i));
        }
        return newPopulation;
    }


    private Population createRandomSample(Population population, int size) {
        Population sample = new Population();
        for (int i = 0; i < size; i++) {
            sample.add(population.get(getRandomIntLowerThan(population.size())));
        }
        return sample;
    }
}
