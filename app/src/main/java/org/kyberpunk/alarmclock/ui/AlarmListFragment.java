package org.kyberpunk.alarmclock.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.kyberpunk.alarmclock.R;
import org.kyberpunk.alarmclock.viewmodel.AlarmsViewModel;

public class AlarmListFragment extends Fragment {

    private AlarmsViewModel viewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        viewModel = new AlarmsViewModel(getContext());
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.alarm_list_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvAlarms = view.findViewById(R.id.view_alarms);
        AlarmsAdapter adapter = new AlarmsAdapter(alarm -> {
            AlarmListFragmentDirections.ActionEditAlarm action = AlarmListFragmentDirections.actionEditAlarm(alarm.getId());
            NavHostFragment.findNavController(AlarmListFragment.this)
                    .navigate(action);
        }, (alarm, value) -> {
            alarm.setEnabled(value);
            viewModel.update(alarm);
        });
        rvAlarms.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvAlarms.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(),
                manager.getOrientation());
        rvAlarms.addItemDecoration(dividerItemDecoration);
        viewModel.getAll().observe(this, adapter::setData);

        view.findViewById(R.id.button_add).setOnClickListener(view1 -> NavHostFragment.findNavController(AlarmListFragment.this)
                .navigate(R.id.action_new_alarm));
    }
}
