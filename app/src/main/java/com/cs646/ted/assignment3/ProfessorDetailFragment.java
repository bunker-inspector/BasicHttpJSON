package com.cs646.ted.assignment3;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfessorDetailFragment extends Fragment {

    public static final String SPECIFIC_PROF_URL    = "http://bismarck.sdsu.edu/rateme/instructor/",
                               RATING_POST_URL      = "http://bismarck.sdsu.edu/rateme/rating/",
                               COMMENT_POST_URL     = "http://bismarck.sdsu.edu/rateme/comment/",
                               SEL_PROFESSOR_ID     = "idofprofessorselected";

    private int mProfessorId;

    public static ProfessorDetailFragment newInstance(int idOfProfSelected) {
        ProfessorDetailFragment fragment = new ProfessorDetailFragment();
        Bundle args = new Bundle();
        args.putInt(SEL_PROFESSOR_ID, idOfProfSelected);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfessorDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProfessorId = getArguments().getInt(SEL_PROFESSOR_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_professor_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}
