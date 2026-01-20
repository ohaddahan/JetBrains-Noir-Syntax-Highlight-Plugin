package cash.turbine.noir

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import cash.turbine.noir.settings.NoirSettings
import cash.turbine.noir.settings.NoirProjectSettings

class NoirSettingsTest : BasePlatformTestCase() {

    fun testDefaultSettings() {
        val settings = NoirSettings.getInstance()
        assertNotNull(settings)
        assertTrue(settings.enableLsp)
        assertFalse(settings.lightweightMode)
        assertEquals("", settings.nargoPath)
    }

    fun testSettingsModification() {
        val settings = NoirSettings.getInstance()
        val originalPath = settings.nargoPath
        val originalLsp = settings.enableLsp

        try {
            settings.nargoPath = "/custom/path/to/nargo"
            settings.enableLsp = false

            assertEquals("/custom/path/to/nargo", settings.nargoPath)
            assertFalse(settings.enableLsp)
        } finally {
            settings.nargoPath = originalPath
            settings.enableLsp = originalLsp
        }
    }

    fun testProjectSettings() {
        val projectSettings = NoirProjectSettings.getInstance(project)
        assertNotNull(projectSettings)
        assertEquals("", projectSettings.nargoPathOverride)
    }

    fun testEffectiveNargoPath() {
        val settings = NoirSettings.getInstance()
        val projectSettings = NoirProjectSettings.getInstance(project)

        val originalGlobal = settings.nargoPath
        val originalProject = projectSettings.nargoPathOverride

        try {
            settings.nargoPath = "/global/nargo"
            projectSettings.nargoPathOverride = ""
            assertEquals("/global/nargo", projectSettings.getEffectiveNargoPath())

            projectSettings.nargoPathOverride = "/project/nargo"
            assertEquals("/project/nargo", projectSettings.getEffectiveNargoPath())
        } finally {
            settings.nargoPath = originalGlobal
            projectSettings.nargoPathOverride = originalProject
        }
    }
}
