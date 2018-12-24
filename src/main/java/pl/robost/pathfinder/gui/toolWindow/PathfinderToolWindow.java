package pl.robost.pathfinder.gui.toolWindow;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.packageDependencies.ui.TreeModel;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;

public class PathfinderToolWindow {
    private JPanel content;
    private JTree callPathsTree;

    private static PathfinderToolWindow INSTANCE;

    public PathfinderToolWindow(ToolWindow toolWindow) {
        this.callPathsTree.setModel(new TreeModel(null));
        INSTANCE = this;
        DefaultTreeCellRenderer cellRenderer = (DefaultTreeCellRenderer) callPathsTree.getCellRenderer();
        cellRenderer.setLeafIcon(AllIcons.Nodes.Method);
        cellRenderer.setClosedIcon(AllIcons.Nodes.Method);
        cellRenderer.setOpenIcon(AllIcons.Nodes.Method);
    }

    public static PathfinderToolWindow INSTANCE() {
        return INSTANCE;
    }

    public JComponent getContent() {
        return content;
    }

    public void setCallPathsTree(TreeModel tree){
        callPathsTree.setModel(tree);
    }
}
