package pl.robost.pathfinder.model.converter;

import com.intellij.packageDependencies.ui.TreeModel;
import lombok.experimental.UtilityClass;
import pl.robost.pathfinder.model.CallPath;
import pl.robost.pathfinder.model.Method;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

@UtilityClass
public class CallPathToTreeModelConverter {
    public TreeModel convertToTreeModel(Collection<CallPath> callPaths){
        TreeNode root = new DefaultMutableTreeNode("Call paths:");
        for (CallPath callPath : callPaths) {
            ((DefaultMutableTreeNode) root).add((DefaultMutableTreeNode) createBranch(callPath));
        }
        return new TreeModel(root);
    }

    private TreeNode createBranch(CallPath callPath){
        LinkedHashSet<Method> path = callPath.getPath();
        Iterator<Method> iterator = path.iterator();
        Method lastChild = iterator.next();
        TreeNode childNode = new DefaultMutableTreeNode(lastChild.toString());
        TreeNode parentNode = null;
        while (iterator.hasNext()){
            Method parent = iterator.next();
            parentNode = new DefaultMutableTreeNode(parent.toString());
            ((DefaultMutableTreeNode) parentNode).add((DefaultMutableTreeNode) childNode);
            childNode = parentNode;
        }
        return parentNode;
    }
}
