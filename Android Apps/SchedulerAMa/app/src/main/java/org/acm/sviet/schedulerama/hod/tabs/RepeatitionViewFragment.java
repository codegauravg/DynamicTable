package org.acm.sviet.schedulerama.hod.tabs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.acm.sviet.schedulerama.R;
import org.acm.sviet.schedulerama.adapters.ScheduleAdapter;
import org.acm.sviet.schedulerama.interfaces.Refresh;
import org.acm.sviet.schedulerama.models.Schedule;
import org.acm.sviet.schedulerama.utility.ApiUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class RepeatitionViewFragment extends Fragment  implements Spinner.OnItemSelectedListener{

    private RecyclerView repeatitionRecycleView;
    private final String TAG = "/RepeatitionViewFragment";
    private Spinner deptSpinner;
    private Spinner semSpinner;
    private Spinner sectionSpinner;
    private Spinner weekDaySpinner;

    public RepeatitionViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_repeatition_view, container, false);

        deptSpinner = (Spinner) rootView.findViewById(R.id.repeatitionDeptSpinner);
        semSpinner = (Spinner) rootView.findViewById(R.id.repeatitionSemSpinner);
        sectionSpinner = (Spinner) rootView.findViewById(R.id.repeatitionSectionSpinner);
        weekDaySpinner = (Spinner) rootView.findViewById(R.id.repeatitionWeekDaySpinner);

        String deptArray[] = getResources().getStringArray(R.array.rep_dept_array);
        String semArray[] = getResources().getStringArray(R.array.rep_sem_array);
        String sectionArray[] = getResources().getStringArray(R.array.rep_section_array);
        Integer weekDayArray[] = {1,2,3,4,5};

        List<String> deptData = new ArrayList<>(Arrays.asList(deptArray));
        List<String> semData = new ArrayList<>(Arrays.asList(semArray));
        List<String> sectionData = new ArrayList<>(Arrays.asList(sectionArray));
        List<Integer> weekDayData = new ArrayList<>(Arrays.asList(weekDayArray));

        ArrayAdapter<String> deptAdapter = new ArrayAdapter<>(getActivity(), R.layout.edit_spinner_layout, R.id.textViewEditScheduleListLayout, deptData);
        ArrayAdapter<String> semAdapter = new ArrayAdapter<>(getActivity(), R.layout.edit_spinner_layout, R.id.textViewEditScheduleListLayout, semData);
        ArrayAdapter<String> sectionAdapter = new ArrayAdapter<>(getActivity(), R.layout.edit_spinner_layout, R.id.textViewEditScheduleListLayout, sectionData);
        ArrayAdapter<Integer> weekDayAdapter = new ArrayAdapter<>(getActivity(),R.layout.edit_spinner_layout,R.id.textViewEditScheduleListLayout,weekDayData);

        deptSpinner.setAdapter(deptAdapter);
        semSpinner.setAdapter(semAdapter);
        sectionSpinner.setAdapter(sectionAdapter);
        weekDaySpinner.setAdapter(weekDayAdapter);


        repeatitionRecycleView = (RecyclerView) rootView.findViewById(R.id.repeatitionViewRecycleView);

        repeatitionRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        deptSpinner.setOnItemSelectedListener(this);
        semSpinner.setOnItemSelectedListener(this);
        sectionSpinner.setOnItemSelectedListener(this);
        weekDaySpinner.setOnItemSelectedListener(this);

        return rootView;
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        doTask();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do Nothing..
    }

    public void doTask(){
        ApiUtil.sync(getActivity(),
                //callback token
                new Refresh() {
                    @Override
                    public void callback() {
                        doTask();
                    }
                },
                //task
                ApiUtil.TASK.GET_SCHEDULE_FOR_WEEKDAY,
                //view to adapt
                repeatitionRecycleView,
                //data provided for the adaptive task
                (String) deptSpinner.getSelectedItem(), (String) semSpinner.getSelectedItem(), (String) sectionSpinner.getSelectedItem(), (Integer) weekDaySpinner.getSelectedItem());

    }


}

