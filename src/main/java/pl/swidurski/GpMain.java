package pl.swidurski;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.swidurski.gp.*;
import pl.swidurski.gp.operators.Constant;
import pl.swidurski.gp.operators.math.Addition;
import pl.swidurski.gp.operators.math.Multiplication;
import pl.swidurski.gui.GPController;

import java.io.IOException;

public class GpMain extends Application {
    public static final String GUI_MAIN_FXML = "gui/gp.fxml";
    public static final String MAIN_CSS = "main.css";
    public static final String TITLE = "GP - K.Swidurski";

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(GpMain.class.getResource(GUI_MAIN_FXML));
        Parent root = loader.load();
        GPController controller = loader.getController();
        controller.init();
        stage.setTitle(TITLE);
        stage.setScene(new Scene(root, 1000, 1000));
        stage.setMaximized(true);
        stage.getScene().getStylesheets().add(MAIN_CSS);
        stage.show();
    }


    public static void main(String[] args) throws IOException {
        launch(args);

//        Variable results = new Variable("y", 1);
//        VanillaGP gp = new VanillaGP(1, 1, results);
//
//
//        InitializationMethod initMethod = new RampedHalfAndHalf(5, 50);
//        Population population = initMethod.initialize(gp);
//
//        for (Individual individual : population) {
//            double result = individual.calculate();
//            System.out.println(individual.asSymbol() + " = " + result);
//        }

//        Range rangeX = new DiscreteRange(Arrays.asList(1.0, 2.0, 3.0));
//        Range rangeY = new DiscreteRange(Arrays.asList(1.0, 1.0, 1.0));
//
//        Variable y = new Variable("y", 1);
//        Variable x = new Variable("x", 0);
//
//        y.setRange(rangeY);
//        x.setRange(rangeX);
//
//        InstructionSet instructionSet = new DefaultInstructionSet(x);
//
//        OperatorNode root = new OperatorNode(new Addition());
//        root.add(new OperatorNode(x));
//        root.add(new OperatorNode(new Constant(1)));
//
//        Individual individual = new Individual(root);
//        individual.setIndependentVariables(instructionSet.getVariables());
//        individual.setDependentVariable(y);
//
//        Fitness fitness = new MeanSquaredError();
//        System.err.println(root.asSymbol());
//        System.err.tprintln(fitness.calculate(individual));


//        OperatorNode A = new OperatorNode(new Addition());
//        A.add(new OperatorNode(new Constant(1)));
//        A.add(new OperatorNode(new Constant(2)));
//
//
//        OperatorNode B = new OperatorNode(new Multiplication());
//        B.add(new OperatorNode(new Constant(4)));
//        B.add(new OperatorNode(new Constant(5)));
//
////        SubtreeSwappingCrossover crossover = new SubtreeSwappingCrossover();
////        Population population = crossover.crossover(null, new Population(new Individual(A)), new Population(new Individual(B)));
//
//        VanillaGP gp = new VanillaGP(10, 10, null);
//        gp.setInstructionSet(new DefaultInstructionSet());
//        RandomSubtreeMutation mutation = new RandomSubtreeMutation(2);
//        Population population = mutation.mutate(gp, new Population(new Individual(A)), new Population(new Individual(B)));
//
//        for (Individual individual : population) {
//            double result = individual.calculate();
//            System.err.println(individual.asSymbol() + " = " + result);
//        }
    }
}
