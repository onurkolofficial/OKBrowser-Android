package com.onurkol.app.browser;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.onurkol.app.browser.controller.ContextController;
import com.onurkol.app.browser.controller.PreferenceController;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.onurkol.app.browser", appContext.getPackageName());
    }

    @Test
    public void getPreferenceController(){
        // To get PreferenceController, Context must be taken.
        ContextController.setBaseContext(InstrumentationRegistry.getInstrumentation().getContext());
        assertNotNull(PreferenceController.getController());
    }
}