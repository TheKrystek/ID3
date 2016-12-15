package pl.swidurski.gui;


import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.StringConverter;
import pl.swidurski.gp.GeneticAlgorithm;
import pl.swidurski.gp.Individual;
import pl.swidurski.gp.Range;
import pl.swidurski.gp.operators.Operator;
import pl.swidurski.gp.operators.Variable;
import pl.swidurski.gp.operators.VariableInstance;
import pl.swidurski.gp.operators.math.*;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GPController {

    private static final String DEFAULT_PATH = "R1.csv";

    @FXML
    private TextField pathField;
    @FXML
    private ComboBox<Variable> dependantVariableCombobox;
    @FXML
    private CheckBox additionCheckBox;
    @FXML
    private CheckBox subtractionCheckBox;
    @FXML
    private CheckBox multiplicationCheckBox;
    @FXML
    private CheckBox divisionCheckBox;
    @FXML
    private CheckBox protectedDivisionCheckBox;
    @FXML
    private CheckBox powerCheckBox;
    @FXML
    private CheckBox protectedPowerCheckBox;
    @FXML
    private CheckBox exponentialCheckBox;
    @FXML
    private CheckBox logarithmCheckBox;
    @FXML
    private CheckBox protectedLogarithmCheckBox;
    @FXML
    private CheckBox cosinusCheckBox;
    @FXML
    private CheckBox sinusCheckBox;
    @FXML
    private TextField iterationLimitTextField;
    @FXML
    private TextField populationTextField;
    @FXML
    private TableView<Result> resultTableView;
    @FXML
    private TableColumn<Result, String> iterationColumn;
    @FXML
    private TableColumn<Result, String> populationSizeColumn;
    @FXML
    private TableColumn<Result, String> equationColumn;
    @FXML
    private TableColumn<Result, String> fitnessColumn;
    @FXML
    private TextField equationTextField;
    @FXML
    private TextField fitnessTextField;
    @FXML
    private TextField crosoverPercentTextField;
    @FXML
    private TextField mutationPercentTextField;
    @FXML
    private TextField tournamentSizeTextField;
    @FXML
    private TextField maxDepthTextField;
    @FXML
    private Button startButton;
    @FXML
    private Tab chartTab;
    @FXML
    private ComboBox<Variable> variableComboBox;
    @FXML
    private LineChart<Number, Number> chart;


    private List<Pair<CheckBox, Operator>> operatorsCheckBoxList = new LinkedList<>();

    private SimpleIntegerProperty populationSize = new SimpleIntegerProperty(10);
    private SimpleIntegerProperty iterationLimit = new SimpleIntegerProperty(10);
    private SimpleIntegerProperty mutationPercent = new SimpleIntegerProperty(5);
    private SimpleIntegerProperty crossoverPercent = new SimpleIntegerProperty(70);
    private SimpleIntegerProperty tournamentSize = new SimpleIntegerProperty(2);
    private SimpleIntegerProperty maxDepth = new SimpleIntegerProperty(3);
    private File path = new File(DEFAULT_PATH);
    private GPConfigurer configurer = new GPConfigurer();
    private Result bestSolution;


    public void init() {
        setupTableView();
        addTextFieldHandler();
        setupDependentVariable();
        setupPopulationSize();
        setupIterationLimit();
        setupTournamentSize();
        setupMutationPercent();
        setupCrossoverPercent();
        setupMaxDepth();
        setupCheckBoxes();
        dependantVariableCombobox.valueProperty().addListener((observable, oldValue, newValue) -> {
            startButton.setDisable(newValue == null);
        });
    }

    private void setupTableView() {
        iterationColumn.setCellValueFactory(p -> new ReadOnlyStringWrapper(String.valueOf(p.getValue().getIteration())));
        fitnessColumn.setCellValueFactory(p -> new ReadOnlyStringWrapper(String.valueOf(p.getValue().getFitness())));
        populationSizeColumn.setCellValueFactory(p -> new ReadOnlyStringWrapper(String.valueOf(p.getValue().getPopulationSize())));
        equationColumn.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getEquation()));
    }


    private void setupCheckBoxes() {
        add(new Addition(), additionCheckBox);
        add(new Subtraction(), subtractionCheckBox);
        add(new Multiplication(), multiplicationCheckBox);
        add(new Division(), divisionCheckBox);
        add(new ProtectedDivision(), protectedDivisionCheckBox);
        add(new Power(), powerCheckBox);
        add(new ProtectedPower(), protectedPowerCheckBox);
        add(new Exponentiation(), exponentialCheckBox);
        add(new Logarithm(), logarithmCheckBox);
        add(new ProtectedLogarithm(), protectedLogarithmCheckBox);
        add(new Cosinus(), cosinusCheckBox);
        add(new Sinus(), sinusCheckBox);
    }

    private void add(Operator operator, CheckBox checkBox) {
        operatorsCheckBoxList.add(new Pair<>(checkBox, operator));
    }

    private void setupPopulationSize() {
        setupIntegerFilter(populationTextField, populationSize);
        populationSize.addListener((observable, oldValue, newValue) -> configurer.setPopulationSize(newValue.intValue()));
        configurer.setPopulationSize(populationSize.intValue());

    }


    private void setupIterationLimit() {
        setupIntegerFilter(iterationLimitTextField, iterationLimit);
        iterationLimit.addListener((observable, oldValue, newValue) -> configurer.setIterationLimit(newValue.intValue()));
        configurer.setIterationLimit(iterationLimit.intValue());
    }


    private void setupCrossoverPercent() {
        setupIntegerFilter(crosoverPercentTextField, crossoverPercent);
        crossoverPercent.addListener((observable, oldValue, newValue) -> configurer.setCrossoverPercent(newValue.intValue()));
        configurer.setCrossoverPercent(crossoverPercent.intValue());

    }

    private void setupMutationPercent() {
        setupIntegerFilter(mutationPercentTextField, mutationPercent);
        mutationPercent.addListener((observable, oldValue, newValue) -> configurer.setMutationPercent(newValue.intValue()));
        configurer.setMutationPercent(mutationPercent.intValue());

    }

    private void setupTournamentSize() {
        setupIntegerFilter(tournamentSizeTextField, tournamentSize);
        tournamentSize.addListener((observable, oldValue, newValue) -> configurer.setTournamentSize(newValue.intValue()));
        configurer.setTournamentSize(tournamentSize.intValue());
    }

    private void setupMaxDepth() {
        setupIntegerFilter(maxDepthTextField, maxDepth);
        maxDepth.addListener((observable, oldValue, newValue) -> configurer.setMaxDepth(newValue.intValue()));
        configurer.setMaxDepth(maxDepth.intValue());
    }

    private void setupIntegerFilter(TextField textField, SimpleIntegerProperty property) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            // If loosing focus try to parse value
            if (!newValue) {
                parseValue(textField, property);
            }
        });
        textField.textProperty().addListener((observable, oldValue, newValue) -> parseValue(textField, property));
        textField.setText(String.valueOf(property.intValue()));
    }

    private void parseValue(TextField textField, SimpleIntegerProperty property) {
        try {
            Integer value = new Integer(textField.getText());
            property.setValue(value);
        } catch (NumberFormatException e) {
            System.err.println("Don't change anything");
        }
    }


    private void setupDependentVariable() {
        StringConverter<Variable> converter = new StringConverter<Variable>() {
            @Override
            public String toString(Variable object) {
                return object.getName();
            }

            @Override
            public Variable fromString(String string) {
                return null;
            }
        };
        dependantVariableCombobox.setConverter(converter);
        variableComboBox.setConverter(converter);

        dependantVariableCombobox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            configurer.setDependentVariable(newValue);
        });
    }


    private void addTextFieldHandler() {
        pathField.textProperty().addListener((observable, oldValue, newValue) -> {
            File file = new File(newValue);
            if (file.exists()) {
                path = file;
            }
        });

        pathField.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                Stage stage = (Stage) pathField.getScene().getWindow();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialDirectory(new File(".").getAbsoluteFile());
                fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
                fileChooser.setTitle("Select a CSV file");
                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null && selectedFile.exists()) {
                    path = selectedFile;
                    pathField.setText(path.getAbsolutePath());
                }
            }
        });
        pathField.setText(DEFAULT_PATH);
    }

    @FXML
    void loadFileAction() throws IOException {
        configurer.readFile(path);
        dependantVariableCombobox.getItems().setAll(configurer.getVariables());
        dependantVariableCombobox.getSelectionModel().select(configurer.getDependentVariable());
    }

    @FXML
    void startAction() throws Exception {
        resultTableView.getItems().clear();
        startButton.setDisable(true);
        chartTab.setDisable(true);
        configurer.setOperators(getOperators());
        GeneticAlgorithm gp = configurer.prepare();
        variableComboBox.getItems().setAll(configurer.getIndependentVariables());
        variableComboBox.getSelectionModel().select(0);

        gp.setUpdateEventHandler((population, champion, iteration) -> {
            System.out.println(String.format("%s. Population: %s, Best solution: %s (fitness: %s)", iteration, population.size(), champion.asSymbol(), champion.getFitness()));
            Result result = new Result(champion, population, iteration);
            Platform.runLater(() -> {
                resultTableView.getItems().add(result);
                resultTableView.scrollTo(result);
            });
        });

        GpTask task = new GpTask(gp);
        task.setOnSucceeded(event -> {
            bestSolution = task.getValue();
            fitnessTextField.setText(String.valueOf(bestSolution.getFitness()));
            equationTextField.setText(bestSolution.getEquation());
            startButton.setDisable(false);
            chartTab.setDisable(false);
        });
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private List<Operator> getOperators() {
        return operatorsCheckBoxList.stream().filter(pair -> pair.getKey().isSelected()).map(Pair::getValue).collect(Collectors.toCollection(LinkedList::new));
    }

    @FXML
    private void drawAction() {
        Variable variable = variableComboBox.getValue();
        if (variable == null) {
            return;
        }

        chart.getData().clear();

        Variable dependentVariable = configurer.getDependentVariable();

        Range x = variable.getRange();
        Range y = dependentVariable.getRange();

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel(variable.getName());
        yAxis.setLabel(dependentVariable.getName());

        chart.setTitle("Chart");
        XYChart.Series original = new XYChart.Series();
        original.setName("Original");

        ObservableList<Variable> variables = variableComboBox.getItems();
        for (Variable v : variables) {
            if (!v.equals(variable)) {
                v.getInstance().setValueFromRange(0);
            }
        }
        Set<Double> set = new HashSet<>();
        for (int i = 0; i < x.size(); i++) {

            double v = x.get(i);
            if (!set.contains(v)) {
                original.getData().add(new XYChart.Data(v, y.get(i)));
                set.add(v);
            }
        }

        XYChart.Series approximated = new XYChart.Series();
        approximated.setName("Approximated");
        Individual champion = bestSolution.getIndividual();
        VariableInstance instance = variable.getInstance();

        for (int i = 0; i < x.size(); i++) {
            instance.setValueFromRange(i);
            approximated.getData().add(new XYChart.Data(x.get(i), champion.calculate()));
        }
        chart.getData().add(original);
        chart.getData().add(approximated);
    }
}
