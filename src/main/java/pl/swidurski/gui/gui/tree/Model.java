package pl.swidurski.gui.gui.tree;

import java.util.*;

public class Model<T extends CellContent<T>> {
    protected Cell graphParent;
    protected List<Cell<T>> allCells;
    protected List<Cell<T>> addedCells;
    protected List<Cell<T>> removedCells;
    protected List<Edge<T>> allEdges;
    protected List<Edge<T>> addedEdges;
    protected List<Edge<T>> removedEdges;

    Map<Integer, Cell<T>> cellMap;

    public Model() {
        graphParent = new Cell(null);
        clear();
    }

    public void clear() {
        allCells = new ArrayList<>();
        addedCells = new ArrayList<>();
        removedCells = new ArrayList<>();
        allEdges = new ArrayList<>();
        addedEdges = new ArrayList<>();
        removedEdges = new ArrayList<>();
        cellMap = new HashMap<>();
    }

    public void clearAddedLists() {
        addedCells.clear();
        addedEdges.clear();
    }

    public List<Cell<T>> getAddedCells() {
        return addedCells;
    }

    public List<Cell<T>> getRemovedCells() {
        return removedCells;
    }

    public List<Cell<T>> getAllCells() {
        return allCells;
    }

    public List<Edge<T>> getAddedEdges() {
        return addedEdges;
    }

    public List<Edge<T>> getRemovedEdges() {
        return removedEdges;
    }

    public List<Edge<T>> getAllEdges() {
        return allEdges;
    }

    public void addCell(T value, CellType type) {
        switch (type) {
            case LABEL:
                addCell(new LabelCell<>(value));
                break;
            default:
                throw new UnsupportedOperationException("Unsupported type: " + type);
        }
    }

    private void addCell(Cell cell) {
        addedCells.add(cell);
        cellMap.put(cell.getCellId(), cell);
    }


    public void addEdge(T source, T target, String text) {
        Cell sourceCell = getCell(target);
        Cell targetCell = getCell(source);

        if (sourceCell == null || targetCell == null) {
            return;
        }

        Edge edge = new Edge(sourceCell, targetCell, text);

        addedEdges.add(edge);
    }

    public Edge<T> getEdge(T source, T target) {
        return getEdge(getCell(source), getCell(target));
    }

    private Cell<T> getCell(T target) {
        return cellMap.get(target.hashCode());
    }


    public Edge<T> getEdge(Cell<T> source, Cell<T> target) {
        Optional<Edge<T>> first = getAllEdges().stream().filter(p -> (p.getSource().equals(source) && p.getTarget().equals(target)) ||
                (p.getSource().equals(target) && p.getTarget().equals(source))).findFirst();

        return first.get();
    }

    public void attachOrphansToGraphParent(List<Cell<T>> cellList) {
        for (Cell cell : cellList) {
            if (cell.getParentCells().size() == 0) {
                graphParent.addCellChild(cell);
            }
        }
    }

    public void disconnectFromGraphParent(List<Cell<T>> cellList) {
        for (Cell cell : cellList) {
            graphParent.removeCellChild(cell);
        }
    }

    public void merge() {
        // cells
        allCells.addAll(addedCells);
        allCells.removeAll(removedCells);
        addedCells.clear();
        removedCells.clear();

        // edges
        allEdges.addAll(addedEdges);
        allEdges.removeAll(removedEdges);
        addedEdges.clear();
        removedEdges.clear();
    }
}