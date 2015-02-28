package com.cs646.ted.assignment3;

import android.app.Activity;
import android.os.Bundle;


public class ProfessorDetailActivity extends Activity {

    public static final String EXTRA_PROF_SELECTED  = "extraprofselected";

    ProfessorDetailFragment mDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_detail);

        int professorSelected = getIntent().getIntExtra(EXTRA_PROF_SELECTED, 0);

        mDetailFragment = ProfessorDetailFragment.newInstance(professorSelected);

        getFragmentManager().beginTransaction()
                .add(R.id.detail_fragment_container, mDetailFragment).commit();
    }
}
