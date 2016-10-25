package pl.swidurski.gui.tree;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;

public class Edge<T extends CellContent<T>> extends Group {

    private final String text;

    protected Cell<T> source;
    protected Cell<T> target;

    protected Label label;
    protected Line line;

    public Edge(Cell<T> source, Cell<T> target) {
        this(source, target, "");
    }

    public Edge(Cell<T> source, Cell<T> target, String text) {
        this.text = text;
        this.source = source;
        this.target = target;

        source.addCellChild(target);
        target.addCellParent(source);

        line = new Line();
        line.getStyleClass().add("edge");
        line.startXProperty().bind(source.layoutXProperty().add(source.widthProperty().divide(2.0)));
        line.startYProperty().bind(source.layoutYProperty().add(source.heightProperty().divide(2.0)));
        line.endXProperty().bind(target.layoutXProperty().add(target.widthProperty().divide(2.0)));
        line.endYProperty().bind(target.layoutYProperty().add(target.heightProperty().divide(2.0)));
        getChildren().add(line);

        label = new Label(text);
        label.getStyleClass().add("edge");
        label.layoutXProperty().bind(target.layoutXProperty().add(source.layoutXProperty()).divide(2));
        label.layoutYProperty().bind(target.layoutYProperty().add(source.layoutYProperty()).divide(2));
        getChildren().add(label);
    }

    private final static String SELECTED = "selected";

    public void select(boolean select) {
        if (select) {
            getStyleClass().setAll(SELECTED);
        } else {
            getStyleClass().clear();
        }
    }

    public Cell getSource() {
        return source;
    }

    public Cell getTarget() {
        return target;
    }

}