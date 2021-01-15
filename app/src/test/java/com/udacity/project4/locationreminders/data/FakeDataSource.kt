package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result


//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var reminders: MutableList<ReminderDTO>? = mutableListOf()) : ReminderDataSource {


    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        reminders?.let{ return Result.Success(ArrayList(it)) }
        return Result.Error(
                "Reminders not found")
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders?.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        reminders?.forEach {
            return when(id) {
                it.id -> Result.Success(it)
                else -> Result.Error("No reminder found with id $id")
            }
        }
        return Result.Error("No reminder found with id $id")
    }

    override suspend fun deleteAllReminders() {
        reminders?.clear()
    }


}