package com.udacity.project4.locationreminders.savereminder

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.util.MainCoroutineRule
import com.udacity.project4.locationreminders.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O_MR1])

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var saveReminderViewModel: SaveReminderViewModel
    private lateinit var datasource: FakeDataSource

    @Before
    fun setUp() {
        stopKoin()
        datasource = FakeDataSource()
        saveReminderViewModel =
            SaveReminderViewModel(ApplicationProvider.getApplicationContext(), datasource)

    }

    @Test
    fun saveReminder_checkLoadingStatus() = mainCoroutineRule.runBlockingTest {
        mainCoroutineRule.pauseDispatcher()
        val reminder = ReminderDataItem(
            "title",
            "description",
            "location",
            -33.8523341,
            151.2106085
        )
        saveReminderViewModel.saveReminder(reminder)
        assertThat(saveReminderViewModel.showLoading.getOrAwaitValue()).isTrue()

        mainCoroutineRule.resumeDispatcher()
        assertThat(saveReminderViewModel.showLoading.getOrAwaitValue()).isFalse()
    }

    @Test
    fun saveReminder_shouldReturnErrorWhenEnteredWithoutTitle() = mainCoroutineRule.runBlockingTest {

        val reminderDataItem = ReminderDataItem(
            "",
            "description",
            "location",
            -33.8523341,
            151.2106085
        )

        assertThat(saveReminderViewModel.validateEnteredData(reminderDataItem)).isFalse()
        assertThat(saveReminderViewModel.showSnackBarInt.getOrAwaitValue()).isEqualTo(R.string.err_enter_title)

    }

    @Test
    fun saveReminder_shouldReturnErrorWhenEnteredWithoutLocation() = mainCoroutineRule.runBlockingTest {

        val reminderDataItem = ReminderDataItem(
            "title",
            "description",
            "",
            -33.8523341,
            151.2106085
        )

        assertThat(saveReminderViewModel.validateEnteredData(reminderDataItem)).isFalse()
        assertThat(saveReminderViewModel.showSnackBarInt.getOrAwaitValue()).isEqualTo(R.string.err_select_location)

    }


}