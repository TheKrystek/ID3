package pl.swidurski.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Data;
import pl.swidurski.gui.dialogs.AskForDiscretizationDialog;
import pl.swidurski.gui.dialogs.AskForRangeDialog;
import pl.swidurski.gui.dialogs.InfoDialog;
import pl.swidurski.gui.tree.Cell;
import pl.swidurski.gui.tree.*;
import pl.swidurski.id3.AttributeDiscretizer;
import pl.swidurski.id3.CSVReader;
import pl.swidurski.id3.ID3;
import pl.swidurski.model.*;
import pl.swidurski.services.RuleSolver;
import pl.swidurski.services.RulesBuilder;
import pl.swidurski.services.StatisticService;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Controller {
    //    public static final String DEFAULT_PATH = "iris.csv";
    public static final String DEFAULT_PATH = "contact-lenses.csv";
    public static final String FORMAT = "%.2f";
    private File path = new File(DEFAULT_PATH);

    //<editor-fold desc="FXML Fields">
    @FXML
    private ListView<Rule> rulesListView;
    @FXML
    private Button loadRulesButton;
    @FXML
    private Tab mainTab;
    @FXML
    private TextField pathField;
    @FXML
    private Tab dataTab;
    @FXML
    private Tab treeTab;
    @FXML
    private Tab rulesTab;
    @FXML
    private TableView<ObservableList<String>> dataTable;
    @FXML
    private TableView<ObservableList<String>> rulesTable;
    @FXML
    private AnchorPane leftPane;
    @FXML
    private Slider stepSlider;
    @FXML
    private Slider siblingDistanceSlider;
    @FXML
    private Slider treeDistanceSlider;
    @FXML
    private Slider nodeSizeSlider;
    @FXML
    private Slider maxDepthSlider;
    @FXML
    private VBox inputPane;
    @FXML
    private Label label;
    @FXML
    private Label entropy;
    @FXML
    private Label conditionalEntropy;
    @FXML
    private Label informationGain;
    @FXML
    private Label supportLabel;
    @FXML
    private Label confidenceLabel;
    @FXML
    private Label ruleLabel;
    @FXML
    private Label resultLabel;
    @FXML
    private GridPane gridPane;

    //</editor-fold>
    private Graph<TreeNode> graph = new Graph<>();
    private TreeLayout layout;
    private TreeNode root;
    private DataSet dataSet;
    private List<Rule> rules;

    public void init() {
        disableTabs(true);
        inputPane.setVisible(false);
        pathField.setText(path.getAbsolutePath());
        addTreeView(graph.getScrollPane());
        addTextFieldHandler();
        addRulesListHandler();
    }

    private void addRulesListHandler() {
        rulesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showRuleDetails(newValue);
            showRuleData(newValue);
        });
    }

    private void showRuleData(Rule rule) {
        rulesTable.getItems().clear();

        if (rule != null) {
            for (Integer index : rule.getSupportingIndexes()) {
                rulesTable.getItems().add(FXCollections.observableArrayList(dataSet.getRow(index)));
            }
        }
    }

    private void showRuleDetails(Rule rule) {
        if (rule == null) {
            return;
        }
        supportLabel.setText(String.format(FORMAT, rule.getSupport()));
        confidenceLabel.setText(String.format(FORMAT, rule.getConfidence()));
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
                fileChooser.setTitle("Select a CSV file");
                fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null && selectedFile.exists()) {
                    path = selectedFile;
                    pathField.setText(path.getAbsolutePath());
                }
            }
        });
    }

    private void addTreeNodeHandler(Graph<TreeNode> graph) {
        Cell.Action<TreeNode> action = value -> showNodeInfo(value);

        for (Cell<TreeNode> cell : graph.getModel().getAllCells()) {
            cell.setOnAction(action);
        }
    }

    private void showNodeInfo(TreeNode node) {
        label.setText("");
        entropy.setText("");
        conditionalEntropy.setText("");
        informationGain.setText("");
        if (node != null) {
            label.setText(node.getLabel());
            if (node.getInfo() != null) {
                entropy.setText(String.format(FORMAT, node.getInfo().getEntropy()));
                conditionalEntropy.setText(String.format(FORMAT, node.getInfo().getConditionalEntropy()));
                informationGain.setText(String.format(FORMAT, node.getInfo().getInformationGain()));
            }
        }
    }

    @FXML
    void loadFileAction() throws IOException {
        CSVReader reader = new CSVReader(path);
        dataSet = reader.read();
        askIfDiscretize(dataSet);
        setupGridPane(dataSet);
        fillDataTable(dataSet);
        drawTree(dataSet);
        disableTabs(false);
    }

    @FXML
    void loadRules() {
        RulesBuilder builder = new RulesBuilder(root);
        rules = builder.build();
        rulesListView.getItems().setAll(rules);
        StatisticService ss = new StatisticService(dataSet, rules);
        ss.calculateStatistics();
    }


    public void askIfDiscretize(DataSet dataSet) {
        if (!checkIfHasNumericAttriubte(dataSet)) {
            return;
        }

        AskForDiscretizationDialog dialog = new AskForDiscretizationDialog();
        Optional<ButtonType> show = dialog.show();
        if (show.get() == dialog.getYes()) {
            for (Attribute attribute : dataSet.getAttributes()) {
                if (attribute.isNumeric() && !attribute.isResult()) {
                    AskForRangeDialog askForRangeDialog = new AskForRangeDialog(attribute);
                    int ranges = askForRangeDialog.show();
                    try {
                        AttributeDiscretizer a = new AttributeDiscretizer(attribute, ranges);
                    } catch (Exception e) {
                        System.err.println("Cannot discrtize attributeName " + attribute.getName());
                    }
                }
            }
        }
    }

    private boolean checkIfHasNumericAttriubte(DataSet dataSet) {
        for (Attribute attribute : dataSet.getAttributes()) {
            if (attribute.isNumeric()) {
                return true;
            }
        }
        return false;
    }


    @FXML
    void refreshAction() {
        layout.execute();
    }

    @FXML
    void showOnTree() {
        clearSelectedNodes();
        markInputOnTree();
        treeTab.getTabPane().getSelectionModel().select(treeTab);
    }

    @FXML
    void useRules(){
        if (rules!= null && !rules.isEmpty()){
            loadRules();
            RuleSolver solver = new RuleSolver(rules);
            Rule result = solver.solve(input);
            if (result != null){
                ruleLabel.setText(result.toString());
                resultLabel.setText(result.getResult().getValue());
            }
        }
        rulesTab.getTabPane().getSelectionModel().select(rulesTab);
    }

    private void clearSelectedNodes() {
        for (Edge<TreeNode> edge : graph.getModel().getAllEdges()) {
            edge.select(false);
        }
        for (Cell<TreeNode> cell : graph.getModel().getAllCells()) {
            cell.select(false);
        }
    }

    private void markInputOnTree() {
        List<Item> items = new ArrayList<>();
        items.addAll(input);

        TreeNode node = root;
        while (items.size() > 0) {
            if (!node.getChildren().isEmpty()) {
                TreeNode current = node;
                Item item = items.stream().filter(p -> p.getAttributeName().equals(current.getLabel())).findFirst().get();
                if (item != null) {
                    current.getCell().select(true);
                    node = node.getChildren().stream().filter(p -> p.getEdge().equals(item.getValue())).findFirst().get();
                    node.getCell().select(true);
                    Edge<TreeNode> edge = graph.getModel().getEdge(node, current);
                    if (edge != null)
                        edge.select(true);
                    items.remove(item);
                }
            } else {
                node.getCell().select(true);
                showNodeInfo(node);
                items.clear();
            }
        }
    }

    @FXML
    public void saveAsPng() {
        Stage stage = (Stage) pathField.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a CSV file");
        fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("PNG files", "*.png"));
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            WritableImage image = graph.getCellLayer().snapshot(new SnapshotParameters(), null);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", selectedFile);
            } catch (IOException e) {
            }
        }
    }




    private void setupGridPane(DataSet dataSet) {
        inputPane.setVisible(true);
        input = getDummyItem(dataSet);
        gridPane.getRowConstraints().clear();
        gridPane.getChildren().clear();
        createRows();
    }

    private void createRows() {
        int i = 0;

        for (Item item : input) {
            gridPane.add(new Label(item.getAttributeName()), 0, i);
            gridPane.add(getNode(item), 1, i);
            i++;
        }
    }

    private Node getNode(Item item) {
        if (item.getAttribute().isDiscrete())
            return addTextField(item);
        return addComboBox(item);
    }

    private Node addTextField(Item item) {
        TextField tf = new TextField();
        tf.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                updateTextField(item, tf);
            }
        });
        tf.setText(item.getAttribute().getAttributeDiscretizer().getInputValues().get(0).toString());
        return tf;
    }

    private void updateTextField(Item item, TextField tf) {
        Double value = getValue(tf.getText());
        item.setValue(item.getAttribute().getAttributeDiscretizer().getRange(value));
    }

    private Node addComboBox(Item item) {
        ComboBox<String> cb = new ComboBox<>();
        for (Entry entry : item.getAttribute().getDistinctValues()) {
            cb.getItems().add(entry.getString());
        }
        cb.valueProperty().addListener((observable, oldValue, newValue) -> item.setValue(newValue));
        cb.getSelectionModel().select(cb.getItems().get(0));
        cb.setMaxWidth(Double.MAX_VALUE);
        return cb;
    }

    private Double getValue(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            new InfoDialog().show();
            return 0.0;
        }
    }


    private void drawTree(DataSet dataSet) {
        root = runID3(dataSet);
        addGraphComponents(root);
        setupLayout();
        addTreeNodeHandler(graph);
    }

    private TreeNode runID3(DataSet dataSet) {
        ID3 id3 = new ID3(dataSet);
        return id3.calculate();
    }

    private void setupLayout() {
        layout = new TreeLayout(graph);
        layout.getStep().bind(stepSlider.valueProperty());
        layout.getLimit().bind(maxDepthSlider.valueProperty());
        layout.getNodeSize().bind(nodeSizeSlider.valueProperty());
        layout.getSiblingDistance().bind(siblingDistanceSlider.valueProperty());
        layout.getTreeDistance().bind(treeDistanceSlider.valueProperty());
        layout.execute();
    }


    private void addTreeView(ScrollPane pane) {
        leftPane.getChildren().setAll(pane);
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
    }


    //<editor-fold desc="Data table setup">
    public void fillDataTable(DataSet dataSet) {
        addColumns(dataSet, dataTable);
        addColumns(dataSet, rulesTable);
        addDataTableItems(dataSet);
    }

    private void addDataTableItems(DataSet dataSet) {
        dataTable.getItems().clear();
        for (int i = 0; i < dataSet.getResult().getValues().size(); i++) {
            dataTable.getItems().add(FXCollections.observableArrayList(dataSet.getRow(i)));
        }
    }

    private void addColumns(DataSet dataSet, TableView<ObservableList<String>> dataTable) {
        dataTable.getColumns().clear();
        dataTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        for (Attribute attribute : dataSet.getAttributes()) {
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(attribute.getName());
            column.setResizable(true);
            column.setSortable(true);
            column.setMinWidth(100);
            column.setMaxWidth(Integer.MAX_VALUE);
            column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(attribute.getIndex())));
            column.setCellFactory(param -> new TableCell<ObservableList<String>, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        if (attribute.isResult()) {
                            setStyle("-fx-background-color: rgba(190, 220, 230, 0.5); -fx-font-weight: bold;");
                        }
                    }
                }
            });

            dataTable.getColumns().add(column);
        }
    }


    private ObservableList<Item> getDummyItem(DataSet dataSet) {
        List<Item> list = new ArrayList<>();
        for (Attribute attribute : dataSet.getAttributes()) {
            if (attribute.isResult())
                continue;
            list.add(new Item(attribute, attribute.getValues().get(0)));
        }
        return FXCollections.observableArrayList(list);
    }

    List<Item> input;

    //</editor-fold>

    private void disableTabs(boolean value) {
        dataTab.setDisable(value);
        treeTab.setDisable(value);
        rulesTab.setDisable(value);
    }


    //<editor-fold desc="Tree creation">
    private void addGraphComponents(TreeNode node) {
        graph.clearAllLayers();
        Model model = graph.getModel();
        model.clear();
        graph.beginUpdate();
        addCells(model, node);
        addEdge(model, node);
        graph.endUpdate();
    }

    public void addCells(Model model, TreeNode node) {
        model.addCell(node, CellType.LABEL);
        for (TreeNode n : node.getChildren()) {
            addCells(model, n);
        }
    }

    public void addEdge(Model model, TreeNode node) {
        if (node.getParent() != null)
            model.addEdge(node, node.getParent(), node.getEdge());
        for (TreeNode n : node.getChildren()) {
            addEdge(model, n);
        }
    }
    //</editor-fold>
}
