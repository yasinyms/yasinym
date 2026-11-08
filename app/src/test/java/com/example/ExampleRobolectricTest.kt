package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.TandisData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read app name from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("تندیس", appName)
    }

    @Test
    fun `tandis initial data is valid`() {
        assertTrue(TandisData.CATEGORIES.isNotEmpty())
        assertTrue(TandisData.INITIAL_LISTINGS.isNotEmpty())
        assertTrue(TandisData.HERO_SLIDES.isNotEmpty())
        assertTrue(TandisData.CITIES.contains("تهران و حومه"))
    }
}
