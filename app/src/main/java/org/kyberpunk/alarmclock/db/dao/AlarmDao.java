package org.kyberpunk.alarmclock.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.kyberpunk.alarmclock.db.entity.AlarmEntity;

import java.util.List;

@Dao
public interface AlarmDao {
    @Query("SELECT * FROM alarms")
    LiveData<List<AlarmEntity>> findAllLive();

    @Query("SELECT * FROM alarms")
    List<AlarmEntity> findAll();

    @Query("SELECT * FROM alarms WHERE id = :id")
    LiveData<AlarmEntity> findByIdLive(int id);

    @Query("SELECT * FROM alarms WHERE id = :id")
    AlarmEntity findById(int id);

    @Query("SELECT * FROM alarms WHERE enabled = 1")
    List<AlarmEntity> findAllEnabled();

    @Insert
    long insert(AlarmEntity alarm);

    @Update
    void update(AlarmEntity alarm);

    @Delete
    void delete(AlarmEntity alarm);
}
