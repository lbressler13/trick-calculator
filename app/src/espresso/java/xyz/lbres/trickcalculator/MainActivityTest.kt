package xyz.lbres.trickcalculator

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import xyz.lbres.kotlinutils.general.ternaryIf

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val buildSuffix = ternaryIf(ProductFlavor.devMode, ".dev", "")
        assertEquals("xyz.lbres.trickcalculator$buildSuffix", appContext.packageName)
    }
}
