package org.acm.sviet.schedulerama.adapters;

import android.content.Context;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.acm.sviet.schedulerama.BuildConfig;
import org.acm.sviet.schedulerama.R;
import org.acm.sviet.schedulerama.models.Schedule;

import java.util.Collections;
import java.util.List;

/**
 * Created by Anurag on 28-01-2016.
 *
 * To Use an Adapter for our List View to produce a ViewGroup, We Created a custom baseAdapter for the Model used in the listView.
 */
public class ScheduleAdapter extends BaseAdapter {

    List<Schedule> scheduleList = Collections.emptyList();

    private final Context context;

    public ScheduleAdapter(Context context){this.context = context;}

    public void fillSchedules(List<Schedule> schedules){
        ThreadPreconditions.checkOnMainThread();
        this.scheduleList = schedules;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return scheduleList.size();
    }

    @Override
    public Schedule getItem(int position) {
        return scheduleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView lectureNoView;
        TextView courseNameView;
        TextView facultyNameView;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.schedule_list_view_layout,parent,false);
            lectureNoView = (TextView) convertView.findViewById(R.id.textViewLectureNo);
            courseNameView = (TextView) convertView.findViewById(R.id.textViewCourseName);
            facultyNameView = (TextView) convertView.findViewById(R.id.textViewFacultyName);
            convertView.setTag(new ViewHolder(lectureNoView,courseNameView,facultyNameView));
        }else{
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            lectureNoView = viewHolder.lectureNoView;
            courseNameView = viewHolder.courseNameView;
            facultyNameView = viewHolder.facultyNameView;
        }

        Schedule schedule = getItem(position);
        lectureNoView.setText(String.valueOf(schedule.getLectureNo()));
        courseNameView.setText(schedule.getCourse().getName());
        facultyNameView.setText(schedule.getCourse().getFaculty());

        return convertView;
    }

    //Custom ViewHolder.
    private class ViewHolder {
        private TextView lectureNoView;
        private TextView courseNameView;
        private TextView facultyNameView;

        public ViewHolder(TextView lectureNoView,TextView courseNameView,TextView facultyNameView){
            this.courseNameView = courseNameView;
            this.facultyNameView = facultyNameView;
            this.lectureNoView = lectureNoView;
        }
    }

    private static class ThreadPreconditions {
        public static void checkOnMainThread() {
            if (BuildConfig.DEBUG) {
                if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
                    throw new IllegalStateException("This method should be called from the Main Thread");
                }
            }
        }
    }



}