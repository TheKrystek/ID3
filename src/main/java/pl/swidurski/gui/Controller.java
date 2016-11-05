package pl.swidurski.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Data;
import pl.swidurski.gui.tree.*;
import pl.swidurski.id3.CSVReader;
import pl.swidurski.id3.ID3;
import pl.swidurski.model.Attribute;
import pl.swidurski.model.DataSet;
import pl.swidurski.model.Entry;
import pl.swidurski.model.TreeNode;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    public static final String DEFAULT_PATH = "contact-lenses.csv";
    private File path = new File(DEFAULT_PATH);

    //<editor-fold desc="FXML Fields">
    @FXML
    private Tab mainTab;
    @FXML
    private TextField pathField;
    @FXML
    private Tab dataTab;
    @FXML
    private Tab treeTab;
    @FXML
    private TableView<ObservableList<String>> dataTable;
    @FXML
    private TableView<ObservableList<Item>> inputTable;
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
    //</editor-fold>
    private Graph<TreeNode> graph = new Graph<>();
    private TreeLayout layout;
    private TreeNode root;

    public void init() {
        disableTabs(true);
        inputPane.setVisible(false);
        pathField.setText(path.getAbsolutePath());
        addTreeView(graph.getScrollPane());

        addTextFieldHandler();
    }

    private void addTextFieldHandler() {
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
        pl.swidurski.gui.tree.Cell.Action<TreeNode> action = value -> showInfo(value);

        for (pl.swidurski.gui.tree.Cell<TreeNode> cell : graph.getModel().getAllCells()) {
            cell.setOnAction(action);
        }
    }

    private void showInfo(TreeNode node) {
        label.setText("");
        entropy.setText("");
        conditionalEntropy.setText("");
        informationGain.setText("");
        if (node != null) {
            label.setText(node.getLabel());
            if (node.getInfo() != null) {
                entropy.setText(String.valueOf(node.getInfo().getEntropy()));
                conditionalEntropy.setText(String.valueOf(node.getInfo().getConditionalEntropy()));
                informationGain.setText(String.valueOf(node.getInfo().getInformationGain()));
            }
        }
    }

    @FXML
    void loadFileAction() throws IOException {
        CSVReader reader = new CSVReader(path);
        DataSet dataSet = reader.read();
        setupInputTable(dataSet);
        fillDataTable(dataSet);
        drawTree(dataSet);
        disableTabs(false);
    }

    @FXML
    void refreshAction() {
        layout.execute();
    }

    @FXML
    void addRowAction() {
        clearSelectedNodes();
        markInputOnTree();
        treeTab.getTabPane().getSelectionModel().select(treeTab);
    }

    private void clearSelectedNodes() {
        for (Edge<TreeNode> edge : graph.getModel().getAllEdges()) {
            edge.select(false);
        }
        for (pl.swidurski.gui.tree.Cell<TreeNode> cell : graph.getModel().getAllCells()) {
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
                Item item = items.stream().filter(p -> p.getAttribute().equals(current.getLabel())).findFirst().get();
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
                showInfo(node);
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

    @Data
    class Item {
        String value;
        String attribute;

        public Item(String attribute, String value) {
            this.value = value;
            this.attribute = attribute;
        }
    }


    private void setupInputTable(DataSet dataSet) {
        inputPane.setVisible(true);
        inputTable.setEditable(true);
        addInputTableColumns(dataSet);
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
        addColumns(dataSet);
        addItems(dataSet);
    }

    private void addItems(DataSet dataSet) {
        dataTable.getItems().clear();
        for (int i = 0; i < dataSet.getResult().getValues().size(); i++) {
            dataTable.getItems().add(FXCollections.observableArrayList(dataSet.getRow(i)));
        }
    }

    private void addColumns(DataSet dataSet) {
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

    private void addInputTableColumns(DataSet dataSet) {
        inputTable.getColumns().clear();
        inputTable.getItems().clear();
        inputTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        for (Attribute attribute : dataSet.getAttributes()) {
            if (attribute.isResult())
                continue;

            TableColumn<ObservableList<Item>, String> column = new TableColumn<>(attribute.getName());
            column.setEditable(true);
            column.setSortable(false);
            column.setMaxWidth(Integer.MAX_VALUE);
            ObservableList<String> values = FXCollections.observableArrayList();
            values.addAll(attribute.getDistinctValues().stream().map(Entry::getString).collect(Collectors.toList()));
            column.setCellFactory(param -> new ComboBoxTableCell<ObservableList<Item>, String>() {
                {
                    getItems().setAll(values);
                }

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item);
                    } else {
                        setText(values.get(0));
                    }
                }
            });

            column.setOnEditCommit(event -> {
                ObservableList<Item> item = event.getRowValue();
                item.get(attribute.getIndex()).setValue(event.getNewValue());
            });

            inputTable.getColumns().add(column);
        }

        input = getDummyItem(dataSet);
        inputTable.getItems().setAll(input);
    }

    private ObservableList<Item> getDummyItem(DataSet dataSet) {
        List<Item> list = new ArrayList<>();
        for (Attribute attribute : dataSet.getAttributes()) {
            if (attribute.isResult())
                continue;
            list.add(new Item(attribute.getName(), attribute.getValues().get(0)));
        }
        return FXCollections.observableArrayList(list);
    }

    ObservableList<Item> input;

    //</editor-fold>

    private void disableTabs(boolean value) {
        dataTab.setDisable(value);
        treeTab.setDisable(value);
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
