package cash.turbine.noir

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType

class NoirTemplateContextType : TemplateContextType("Noir") {
    override fun isInContext(templateActionContext: TemplateActionContext): Boolean {
        val file = templateActionContext.file
        return file.name.endsWith(".nr")
    }
}
