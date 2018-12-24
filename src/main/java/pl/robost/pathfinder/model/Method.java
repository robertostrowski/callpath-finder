package pl.robost.pathfinder.model;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.util.PsiTreeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class Method {
    @EqualsAndHashCode.Exclude
    PsiMethod psiMethod;
    String packageName;
    String fileName;
    String className;
    String simpleName;
    String signature;

    public Method(PsiMethod psiMethod){
        this.simpleName = psiMethod.getName();
        this.fileName = PsiTreeUtil.getParentOfType(psiMethod, PsiJavaFile.class).getName();
        this.packageName = PsiTreeUtil.getParentOfType(psiMethod, PsiJavaFile.class).getPackageName();
        this.className = PsiTreeUtil.getParentOfType(psiMethod, PsiClass.class).getName();
        this.signature = psiMethod.getSignature(PsiSubstitutor.EMPTY).toString();
        this.psiMethod = psiMethod;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(className);
        sb.append(".");
        sb.append(simpleName);
        sb.append(" | package='").append(packageName).append('\'');
        sb.append(" | signature='").append(signature).append('\'');
        return sb.toString();
    }
}
