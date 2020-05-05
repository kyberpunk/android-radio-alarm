package org.kyberpunk.alarmclock.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AlarmFiredViewModelFactory implements ViewModelProvider.Factory {
    private Context context;
    private int alarmId;

    public AlarmFiredViewModelFactory(Context context, int alarmId) {
        this.context = context;
        this.alarmId =alarmId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AlarmFiredViewModel(context, alarmId);
    }
}
