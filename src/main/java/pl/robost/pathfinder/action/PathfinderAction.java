package pl.robost.pathfinder.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.packageDependencies.ui.TreeModel;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import pl.robost.pathfinder.gui.packagePicker.PackagePickerDialog;
import pl.robost.pathfinder.gui.toolWindow.PathfinderToolWindow;
import pl.robost.pathfinder.model.CallPath;
import pl.robost.pathfinder.model.CallPathFinder;
import pl.robost.pathfinder.model.converter.CallPathToTreeModelConverter;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;
import java.util.Optional;

public class PathfinderAction extends AnAction {

    public PathfinderAction() {
        super("Method pathfinder");
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        // Setup
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);
        Project project = event.getProject();

        if (editor == null || psiFile == null || project == null){
            Messages.showErrorDialog("Unexpected error, please try again", "Error");
            return;
        }
        Optional<PsiMethod> optionalMethod = getMethodAtCaret(editor, psiFile);
        if (!optionalMethod.isPresent()){
            Messages.showErrorDialog("Unexpected error, please try again", "Error");
            return;
        }
        PsiMethod psiMethod = optionalMethod.get();
        String packageName = getPackageNameByDialog(psiMethod);

        // Find call paths
        CallPathFinder callPathFinder = new CallPathFinder(psiMethod, packageName);
        List<CallPath> methodCallPaths = callPathFinder.findMethodCallPaths();

        if (methodCallPaths.isEmpty()){
            Messages.showInfoMessage("No paths found", "Pathfinder Info");
            PathfinderToolWindow.INSTANCE().setCallPathsTree(new TreeModel(new DefaultMutableTreeNode("No paths found")));
        } else {
            // Display call paths in UI
            ToolWindowManager.getInstance(project).getToolWindow("Pathfinder call paths").activate(() -> {});
            PathfinderToolWindow.INSTANCE().setCallPathsTree(CallPathToTreeModelConverter.convertToTreeModel(methodCallPaths));
        }
    }

    @Override
    public void update(AnActionEvent e) {
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        boolean visible = getMethodAtCaret(editor, psiFile).isPresent();
        e.getPresentation().setEnabledAndVisible(visible);
    }

    private Optional<PsiMethod> getMethodAtCaret(Editor editor, PsiFile psiFile) {
        int offset = editor.getCaretModel().getOffset();
        PsiElement element = psiFile.findElementAt(offset);
        PsiMethod containingMethod = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
        return Optional.ofNullable(containingMethod);
    }

    private String getPackageNameByDialog(PsiMethod psiMethod){
        PackagePickerDialog packagePickerDialog = new PackagePickerDialog();
        DialogBuilder dialogBuilder = new DialogBuilder();
        dialogBuilder.centerPanel(packagePickerDialog.getContent());
        dialogBuilder.title(psiMethod.getName() + ": Pick package name to seek call paths from");
        dialogBuilder.addOkAction();
        dialogBuilder.show();
        return packagePickerDialog.getPackageName();
    }
}