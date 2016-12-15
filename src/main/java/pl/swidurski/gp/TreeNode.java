package pl.swidurski.gp;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Author: Krystian Świdurski
 */
public class TreeNode<T extends TreeNode> {
    @Getter
    protected final List<T> children = new ArrayList<>();

    @Getter
    @Setter
    protected T parent;

    @Getter
    @Setter
    private int depth = 0;

    public T getChild(int index) {
        return children.get(index);
    }

    public void add(T child) {
        children.add(child);
        if (child.getParent() != null){
            child.getParent().remove(child);
        }
        child.setParent(this);
        child.setDepth(depth + 1);
    }

    public int size() {
        return children.size();
    }


    public void remove(T child) {
        children.remove(child);
        child.setParent(null);
    }
}
