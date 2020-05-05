package org.kyberpunk.alarmclock.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Spannable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.kyberpunk.alarmclock.R;
import org.kyberpunk.alarmclock.db.entity.AlarmEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AlarmsAdapter extends RecyclerView.Adapter<AlarmsAdapter.ViewHolder> {

    private List<AlarmEntity> alarmList;
    private Context context;
    private View.OnClickListener listener;
    private OnItemClickListener onItemClickListener;
    private OnSwicthChangedListener onSwitchChangedListener;

    public AlarmsAdapter(OnItemClickListener onItemClickListener, OnSwicthChangedListener onSwitchClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.onSwitchChangedListener = onSwitchClickListener;
        this.alarmList = new ArrayList<>();
    }

    public void setData(List<AlarmEntity> alarmList) {
        this.alarmList = alarmList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View alarmView = inflater.inflate(R.layout.alarm_item, parent, false);
        return new ViewHolder(alarmView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AlarmEntity alarm = alarmList.get(position);
        holder.timeTextView.setText(createTimeString(alarm.getHours(), alarm.getMinutes()));
        holder.daysTextView.setText(createDaysText(alarm.getDays()));
        holder.radioTextView.setText(alarm.getRadioName());
        holder.alarmSwitch.setChecked(alarm.isEnabled());
        holder.alarmSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            onSwitchChangedListener.onChanged(alarm, isChecked);
        });
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClicked(alarm));
    }

    private Spannable createDaysText(Set<Integer> days) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorPrimary });
        int color = a.getColor(0, 0);

        DaysStringBuilder builder = new DaysStringBuilder();
        return builder.setDays(days)
                .setLocale(context.getResources().getConfiguration().getLocales().get(0))
                .setActiveColor(color)
                .build();
    }

    private String createTimeString(int hours, int minutes) {
        return String.format("%d:%02d", hours, minutes);
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView timeTextView;
        public TextView daysTextView;
        public TextView radioTextView;
        public Switch alarmSwitch;

        public ViewHolder(View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.text_alarm_time);
            daysTextView = itemView.findViewById(R.id.text_alarm_days);
            radioTextView = itemView.findViewById(R.id.text_alarm_radio);
            alarmSwitch = itemView.findViewById(R.id.switch_alarm);
        }
    }
}
