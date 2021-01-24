package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase
    private lateinit var dao: RemindersDao

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), RemindersDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.reminderDao()
    }

    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun saveReminder() = runBlockingTest {
        val reminder = ReminderDTO("title", "description","location",
            -33.8523341, 151.2106085)
        dao.saveReminder(reminder)

        assertThat(dao.getReminders()).contains(reminder)
    }

    @Test
    fun getReminderById() = runBlockingTest {
        val reminder = ReminderDTO("title", "description","location",
            -33.8523341, 151.2106085)

        dao.saveReminder(reminder)

        val retrievedReminder = dao.getReminderById(reminder.id)

        assertThat(retrievedReminder).isNotNull()
        assertThat(retrievedReminder?.title).isEqualTo(reminder.title)
        assertThat(retrievedReminder?.description).isEqualTo(reminder.description)
        assertThat(retrievedReminder?.location).isEqualTo(reminder.location)
        assertThat(retrievedReminder?.latitude).isEqualTo(reminder.latitude)
        assertThat(retrievedReminder?.longitude).isEqualTo(reminder.longitude)

    }

    @Test
    fun getReminders() = runBlockingTest{
        val reminder1 = ReminderDTO("title", "description","location",
            -33.8523341, 151.2106085)
        val reminder2 = ReminderDTO("title", "description","location",
            -33.8523341, 151.2106085)
        val reminder3 = ReminderDTO("title", "description","location",
            -33.8523341, 151.2106085)

        dao.saveReminder(reminder1)
        dao.saveReminder(reminder2)
        dao.saveReminder(reminder3)

        val remindersList = dao.getReminders()

        assertThat(remindersList).isNotNull()
        assertThat(remindersList).isNotEmpty()
        assertThat(remindersList).contains(reminder1)
        assertThat(remindersList).contains(reminder2)
        assertThat(remindersList).contains(reminder3)

    }

    @Test
    fun deleteAllReminders() = runBlockingTest {

        val reminder = ReminderDTO("title", "description","location",
            -33.8523341, 151.2106085)

        dao.saveReminder(reminder)
        assertThat(dao.getReminders()).isNotEmpty()

        dao.deleteAllReminders()
        assertThat(dao.getReminders()).isEmpty()
    }
}