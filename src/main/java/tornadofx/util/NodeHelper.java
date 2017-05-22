package tornadofx.util;

import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeHelper {

    public static <T> T findParentOfType( Node node, Class<T> type ){
        if( node == null ) return null;
        Parent parent = node.getParent();
        if( parent == null ) return null;
        if( type.isAssignableFrom(parent.getClass()) ) return (T)parent;
        return findParentOfType( parent, type );
    }

    public static <T> List<T> findChildrenOfType(Pane parent, Class<T> type) {
        List<T> elements = new ArrayList<>();
        for (Node node : parent.getChildren()) {
            if (type.isAssignableFrom(node.getClass())) {
                elements.add((T) node);
            } else if (node instanceof Pane) {
                elements.addAll(findChildrenOfType((Pane) node, type));
            }
        }
        return Collections.unmodifiableList(elements);
    }

    public static void addPseudoClass( Node node, String className ){
        PseudoClass pseudoClass = PseudoClass.getPseudoClass( className );
        node.pseudoClassStateChanged( pseudoClass, true );
    }
}
