package org.kyberpunk.alarmclock.ui;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.kyberpunk.alarmclock.AlarmFiredActivity;
import org.kyberpunk.alarmclock.R;
import org.kyberpunk.alarmclock.db.entity.AlarmEntity;
import org.kyberpunk.alarmclock.schedule.RadioService;
import org.kyberpunk.alarmclock.viewmodel.AlarmFiredViewModel;
import org.kyberpunk.alarmclock.viewmodel.AlarmFiredViewModelFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlarmFiredFragment extends Fragment {

    private AlarmFiredViewModel viewModel;
    private Intent radioServiceIntent;
    private ExecutorService executor;

    public static AlarmFiredFragment newInstance() {
        return new AlarmFiredFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        int alarmId = (int) getActivity().getIntent().getExtras().get(AlarmFiredActivity.ALARM_ID);
        viewModel = ViewModelProviders.of(this, new AlarmFiredViewModelFactory(getContext(), alarmId)).get(AlarmFiredViewModel.class);
        radioServiceIntent = new Intent(getContext(), RadioService.class);
        executor = Executors.newSingleThreadExecutor();
        return inflater.inflate(R.layout.alarm_fired_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        TextView radioView = view.findViewById(R.id.view_radio);
        viewModel.getAlarm().observe(this, a -> {
            radioView.setText(a.getRadioName());
            startRadio(a);
        });
        Button cancelButton = view.findViewById(R.id.button_alarm_close);
        cancelButton.setOnClickListener(v -> {
            stopRadio();
            getActivity().finish();
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity().hasWindowFocus()) {
            stopRadio();
            getActivity().finish();
        }
    }

    private void startRadio(AlarmEntity alarm) {
        radioServiceIntent.putExtra(RadioService.RADIO_SHORTCUT, alarm.getRadioShortcut());
        getActivity().startService(radioServiceIntent);
    }

    private void stopRadio() {
        getActivity().stopService(radioServiceIntent);
    }
}
