package com.shengshijie.test

import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(_root_ide_package_.androidx.test.ext.junit.runners.AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = _root_ide_package_.androidx.test.platform.app.InstrumentationRegistry.getTargetContext()
        assertEquals("cloudist.cc.passwordinputview_java", appContext.packageName)
    }
}
