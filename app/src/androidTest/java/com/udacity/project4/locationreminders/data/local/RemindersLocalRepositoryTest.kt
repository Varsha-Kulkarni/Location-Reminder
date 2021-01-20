package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase
    private lateinit var remindersRepository : RemindersLocalRepository

    private val reminder = ReminderDTO("title", "description","location",
        -33.8523341, 151.2106085)

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()

        remindersRepository = RemindersLocalRepository(database.reminderDao(), Dispatchers.Main)
    }

    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun getReminderById_isSuccessful() = runBlocking{
        remindersRepository.saveReminder(reminder)

        val retrievedReminder = remindersRepository.getReminder(reminder.id) as Result.Success<ReminderDTO>

        assertThat(retrievedReminder).isNotNull()
        assertThat(retrievedReminder.data.title).isEqualTo(reminder.title)
        assertThat(retrievedReminder.data.description).isEqualTo(reminder.description)
        assertThat(retrievedReminder.data.location).isEqualTo(reminder.location)
        assertThat(retrievedReminder.data.latitude).isEqualTo(reminder.latitude)
        assertThat(retrievedReminder.data.longitude).isEqualTo(reminder.longitude)
    }

    @Test
    fun getReminderById_noRemindersFound() = runBlocking {

        val result = remindersRepository.getReminder(reminder.id)

        result as Result.Error
        assertThat(result.message).isEqualTo("Reminder not found!")
    }

    @Test
    fun deleteReminders_isSuccessful() = runBlocking {

        remindersRepository.saveReminder(reminder)

        remindersRepository.deleteAllReminders()

        val result = remindersRepository.getReminders() as Result.Success
        assertThat(result.data).isEmpty()
    }
}