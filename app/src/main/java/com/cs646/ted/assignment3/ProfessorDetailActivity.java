package com.cs646.ted.assignment3;

import android.app.Activity;
import android.os.Bundle;


public class ProfessorDetailActivity extends Activity {

    public static final String EXTRA_PROF_SELECTED  = "extraprofselected",
                        SPECIFIC_PROF_URL    = "http://bismarck.sdsu.edu/rateme/instructor/",
                        RATING_POST_URL      = "http://bismarck.sdsu.edu/rateme/rating/",
                        COMMENT_POST_URL     = "http://bismarck.sdsu.edu/rateme/comment/";

    ProfessorDetailFragment mDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_detail);

        int professorSelected = getIntent().getIntExtra(EXTRA_PROF_SELECTED, 0);

        mDetailFragment = ProfessorDetailFragment.newInstance(professorSelected, SPECIFIC_PROF_URL,
                RATING_POST_URL, COMMENT_POST_URL);

        getFragmentManager().beginTransaction()
                .add(R.id.detail_fragment_container, mDetailFragment).commit();
    }
}
