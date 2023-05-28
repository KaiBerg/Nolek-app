package com.nolek.application.data.network.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nolek.application.data.model.PLCBundle
import kotlinx.coroutines.flow.Flow

@Dao
interface PlcDao {
    @Query("SELECT COUNT(*) FROM bundle")
    fun getCount(): Flow<Int>

    @Query("SELECT * FROM bundle")
    fun getAll(): Flow<List<PLCBundle>>

    @Query("SELECT * FROM bundle ORDER BY id DESC LIMIT 1")
    fun getLastBundle(): Flow<PLCBundle>

    @Insert
    fun insert(vararg bundle: PLCBundle)

    @Delete
    fun delete(bundle: PLCBundle)

    @Query("DELETE FROM bundle")
    fun nuke()
}