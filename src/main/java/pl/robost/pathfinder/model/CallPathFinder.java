package pl.robost.pathfinder.model;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import lombok.Data;

import java.util.*;

@Data
public class CallPathFinder {
    private List<CallPath> callPaths = new ArrayList<>();
    private PsiMethod methodToFind;
    private String packageName;

    public CallPathFinder(PsiMethod methodToFind, String packageNameEndCondition) {
        this.methodToFind = methodToFind;
        this.packageName = packageNameEndCondition;
    }

    public List<CallPath> findMethodCallPaths() {
        CallPath callPath = new CallPath();
        callPath.add(new Method(methodToFind));
        return this.findMethodCallPaths(this.methodToFind, this.packageName, callPath);
    }

    private List<CallPath> findMethodCallPaths(PsiMethod psiMethod, String packageName, CallPath currentCallPath) {
        Collection<PsiReference> references = ReferencesSearch.search(psiMethod).findAll();
        references.forEach(reference -> {
            Optional<PsiMethod> containingMethod = findContainingMethod(reference.getElement());
            if (containingMethod.isPresent()) {
                PsiMethod method = containingMethod.get();
                CallPath callPath = new CallPath(currentCallPath);
                boolean addSuccess = callPath.add(new Method(method));
                if (!addSuccess){
                    // loop detected, method was already called in this path, ignore this branch
                    return;
                }
                if (findPackageName(method).startsWith(packageName)) {
                    // found full valid path
                    callPaths.add(callPath);
                } else {
                    findMethodCallPaths(method, packageName, callPath);
                }
            }
        });
        return callPaths;
    }

    private Optional<PsiMethod> findContainingMethod(PsiElement psiElement) {
        PsiMethod containingMethod = PsiTreeUtil.getParentOfType(psiElement, PsiMethod.class);
        return Optional.ofNullable(containingMethod);
    }

    private String findPackageName(PsiElement element) {
        PsiJavaFile javaFile = PsiTreeUtil.getParentOfType(element, PsiJavaFile.class);
        return Objects.isNull(javaFile) ? null : javaFile.getPackageName();
    }
}
