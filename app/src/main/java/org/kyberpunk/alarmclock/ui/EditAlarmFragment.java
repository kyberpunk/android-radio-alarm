package org.kyberpunk.alarmclock.ui;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import org.kyberpunk.alarmclock.R;
import org.kyberpunk.alarmclock.db.AlarmDatabase;
import org.kyberpunk.alarmclock.model.Alarm;
import org.kyberpunk.alarmclock.radio.dto.Radio;
import org.kyberpunk.alarmclock.viewmodel.EditAlarmViewModel;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class EditAlarmFragment extends Fragment {
    private static final String TAG = "EditAlarmFragment";

    private EditAlarmViewModel viewModel;
    private Button testButton;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.edit_alarm_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int alarmId = EditAlarmFragmentArgs.fromBundle(getArguments()).getAlarmId();

        if (alarmId == 0) {
            viewModel = new EditAlarmViewModel(getContext());
            bindComponents(view);
        } else {
            AlarmDatabase.getInstance(getContext()).alarmDao().findByIdLive(alarmId).observe(this, a -> {
                viewModel = new EditAlarmViewModel(getContext(), a);
                bindComponents(view);
            });
        }
    }

    private void bindComponents(View view) {
        EditText eText= view.findViewById(R.id.edit_time_picker);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setText(String.format("%d:%02d", viewModel.getCurrent().getHours(), viewModel.getCurrent().getMinutes()));
        eText.setOnClickListener(v -> {
            final Alarm alarm = viewModel.getCurrent();
            // time picker dialog
            TimePickerDialog picker = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog,
                    (tp, sHour, sMinute) -> {
                        eText.setText(String.format("%d:%02d", sHour, sMinute));
                        viewModel.setTime(sHour, sMinute);
                    }, alarm.getHours(), alarm.getMinutes(), true);
            picker.show();
        });

        Spinner radioPicker = view.findViewById(R.id.spinner_radios);
        viewModel.getRadios().thenAccept(radios -> {
            List<Radio> radioList = new ArrayList<>(radios);
            Locale locale = getContext().getResources().getConfiguration().getLocales().get(0);
            Collator collator = Collator.getInstance(locale);
            radioList.sort((o1, o2) -> collator.compare(o1.getTitle(), o2.getTitle()));
            String[] radioNames = radioList.stream()
                    .map(Radio::getTitle)
                    .toArray(String[]::new);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, radioNames);
            radioPicker.setAdapter(adapter);
            Optional<Radio> matching = radioList.stream()
                    .filter(r -> r.getShortcut().equals(viewModel.getCurrent().getRadioShortcut()))
                    .findFirst();
            if (matching.isPresent()) {
                int index = radioList.indexOf(matching.get());
                radioPicker.setSelection(index);
            }
            radioPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Radio radio = radioList.get(position);
                    viewModel.setRadioName(radio.getTitle());
                    viewModel.setRadioShortCut(radio.getShortcut());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    viewModel.setRadioName(null);
                    viewModel.setRadioShortCut(null);
                }
            });
        });

        MaterialDayPicker dayPicker = view.findViewById(R.id.picker_day);
        List<MaterialDayPicker.Weekday> weekDays = viewModel.getCurrent().getDays().stream()
                .map(DayOfWeekHelper::fromCalendarDay)
                .collect(Collectors.toList());
        dayPicker.setSelectedDays(weekDays);
        dayPicker.setDaySelectionChangedListener(list -> {
            Set<Integer> days = list.stream()
                    .map(DayOfWeekHelper::fromWeekday)
                    .collect(Collectors.toSet());
            viewModel.setDays(days);
        });

        view.findViewById(R.id.button_cancel).setOnClickListener(v -> NavHostFragment.findNavController(EditAlarmFragment.this)
                .navigate(R.id.action_back_to_list));

        view.findViewById(R.id.button_save).setOnClickListener(view12 -> {
            if (!viewModel.validate())
                return;
            viewModel.save();
            NavHostFragment.findNavController(EditAlarmFragment.this)
                    .navigate(R.id.action_back_to_list);
        });

        testButton = view.findViewById(R.id.button_test);
        testButton.setOnClickListener(v -> {
            if (viewModel.isPlaying()) {
                stopRadio();
            } else {
                startRadio();
            }
        });
    }

    private void startRadio() {
        testButton.setText(getString(R.string.stop_radio));
        viewModel.play().thenAccept(result -> {
            if (!result) {
                testButton.post(() ->
                {
                    testButton.setText(getString(R.string.test_radio));
                    showRadioAlert();
                });
            }
        });
    }

    private void showRadioAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.radio_not_working));
        builder.setTitle(getString(R.string.error));
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void stopRadio() {
        viewModel.stop();
        testButton.setText(getString(R.string.test_radio));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            viewModel.delete();
            NavHostFragment.findNavController(EditAlarmFragment.this)
                    .navigate(R.id.action_back_to_list);
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit_alarm_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (viewModel != null && viewModel.isPlaying())
            stopRadio();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.releasePlayer();
    }
}
