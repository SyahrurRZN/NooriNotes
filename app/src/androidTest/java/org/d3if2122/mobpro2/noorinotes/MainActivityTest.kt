package org.d3if2122.mobpro2.noorinotes

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4 import org.junit.Test
import org.junit.runner.RunWith
import android.widget.DatePicker
import androidx.test.espresso.Espresso

import org.hamcrest.Matchers

import androidx.test.espresso.matcher.ViewMatchers.withClassName

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.Espresso.onView

import androidx.test.espresso.matcher.ViewMatchers.withClassName





@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun testInsert() {
//        val activityScenario = ActivityScenario.launch( MainActivity::class.java)

//// Lakukan aksi melihat histori
        onView(withId(R.id.historyrecycle)).perform(click())

// Cek apakah hasil sesuai yang diharapkan onView(withText(MAHASISWA_DUMMY.nim)).check(matches(isDisplayed())) onView(withText(MAHASISWA_DUMMY.nama)).check(matches(isDisplayed()))

// Tes selesai, tutup activity nya
//        activityScenario.close()
    }

    }

