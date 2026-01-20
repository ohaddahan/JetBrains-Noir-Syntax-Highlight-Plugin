package cash.turbine.noir

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class NoirFileTypeTest : BasePlatformTestCase() {

    fun testFileTypeRecognition() {
        val psiFile = myFixture.configureByText("test.nr", "fn main() {}")
        assertEquals(NoirFileType, psiFile.fileType)
        assertEquals("Noir", psiFile.fileType.name)
    }

    fun testFileExtension() {
        assertEquals("nr", NoirFileType.defaultExtension)
    }

    fun testLanguageAssociation() {
        assertEquals(NoirLanguage, NoirFileType.language)
    }

    fun testFileDescription() {
        assertEquals("Noir source file", NoirFileType.description)
    }

    fun testFileIcon() {
        assertNotNull(NoirFileType.icon)
    }
}
