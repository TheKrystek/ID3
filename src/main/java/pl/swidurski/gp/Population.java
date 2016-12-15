package pl.swidurski.gp;

import java.util.ArrayList;

/**
 * Created by student on 11.12.2016.
 */
public class Population extends ArrayList<Individual> {

    public Population() {
    }



    public Population(Individual... individuals) {
        for (Individual individual : individuals) {
            add(individual);
        }
    }

    public void add(Individual i1, Individual i2) {
        add(i1);
        add(i2);
    }

    public void add(Population population) {
        addAll(population);
    }
}
