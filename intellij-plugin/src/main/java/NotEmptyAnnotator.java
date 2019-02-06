import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class NotEmptyAnnotator implements Annotator {
    private static final String NOT_EMPTY_FQN = "com.ns.annotation.NotEmpty";
    private static final String EMPTY_STRING = "\"\"";
    private static final String NULL_STRING = "null";

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof PsiAssignmentExpression) {
            PsiAssignmentExpression assignmentExpression = (PsiAssignmentExpression) element;
            PsiExpression lExpression = assignmentExpression.getLExpression();
            IElementType opSign = assignmentExpression.getOperationSign().getTokenType();

            if (lExpression instanceof PsiReferenceExpression && opSign == JavaTokenType.EQ) {
                PsiReferenceExpression reference = (PsiReferenceExpression) lExpression;
                PsiElement resolvedElement = reference.resolve();

                if (resolvedElement instanceof PsiField) {
                    PsiExpression rExpression = assignmentExpression.getRExpression();
                    PsiField field = (PsiField) resolvedElement;

                    if (field.getAnnotation(NOT_EMPTY_FQN) != null && isNullOrEmptyLiteral(rExpression)) {
                        TextRange range = new TextRange(rExpression.getTextRange().getStartOffset(),
                                rExpression.getTextRange().getEndOffset());

                        holder.createErrorAnnotation(range, "Cannot be null or empty");
                    }
                }
            }
        }
    }

    private boolean isNullOrEmptyLiteral(PsiExpression expr) {
        return expr instanceof PsiLiteralExpression &&
                (expr.getText().equals(EMPTY_STRING) || expr.getText().equals(NULL_STRING));
    }
}
