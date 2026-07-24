package com.cws.std.storage

import androidx.test.core.app.ApplicationProvider
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class PreferencesTest : BasePreferencesTest() {
    override val preferences: Preferences = Preferences(
        context = ApplicationProvider.getApplicationContext(),
        name = name,
    )
}