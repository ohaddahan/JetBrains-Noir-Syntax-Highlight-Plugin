package cash.turbine.noir

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType

class NoirTestLineMarkerProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        if (element.containingFile?.name?.endsWith(".nr") != true) return null
        if (element.elementType != NoirTokenTypes.ATTRIBUTE) return null

        val text = element.text
        if (!isTestAttribute(text)) return null

        val fnElement = findFollowingFunction(element) ?: return null
        val fnName = extractFunctionName(fnElement.text)

        return LineMarkerInfo(
            element,
            element.textRange,
            NoirIcons.TEST,
            { "Test: $fnName" },
            null,
            GutterIconRenderer.Alignment.LEFT,
            { "Noir test function" }
        )
    }

    private fun isTestAttribute(text: String): Boolean {
        val normalized = text.replace("\\s".toRegex(), "")
        return normalized == "#[test]" ||
               normalized.startsWith("#[test(") ||
               normalized.contains("test")  && normalized.startsWith("#[")
    }

    private fun findFollowingFunction(element: PsiElement): PsiElement? {
        var sibling = element.nextSibling
        while (sibling != null) {
            if (sibling.elementType == NoirTokenTypes.KEYWORD && sibling.text == "fn") {
                return sibling
            }
            if (sibling.elementType == NoirTokenTypes.ATTRIBUTE) {
                sibling = sibling.nextSibling
                continue
            }
            if (sibling.elementType != NoirTokenTypes.WHITESPACE) {
                break
            }
            sibling = sibling.nextSibling
        }
        return null
    }

    private fun extractFunctionName(fnText: String): String {
        return fnText.removePrefix("fn").trim().takeWhile { it.isLetterOrDigit() || it == '_' }
    }
}
