package com.adityagupta.nxtgaruda.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.adityagupta.nxtgaruda.R;
import com.adityagupta.nxtgaruda.data.Question;

public class RPMAnswersFragment extends Fragment {


    TextView ques;
    RadioGroup group;

    Question question;
    String selectedRadio = "";

    public RPMAnswersFragment() {
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rpmques, container, false);

    }

    public static RPMAnswersFragment newInstance(Question question) {
        RPMAnswersFragment fr = new RPMAnswersFragment();
        fr.setQuestion(question);
        return fr;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ques = view.findViewById(R.id.question);
        group = view.findViewById(R.id.radioGroup);

        ques.setText(question.getQuestion());

        float density = getResources().getDisplayMetrics().density;

        LinearLayout.LayoutParams params_rb = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = (int) (10 * density);
        int padding = (int) (15 * density);
        params_rb.setMargins(margin, margin, margin, margin);

        for (int i = 1; i <= question.getAnswers().size(); i++) {
            RadioButton rdbtn = new RadioButton(getContext());
            rdbtn.setLayoutParams(params_rb);
            rdbtn.setPadding(padding, padding, padding, padding);

            rdbtn.setId((2) + i);
            rdbtn.setText(question.getAnswers().get(i - 1));
            group.addView(rdbtn);
        }

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedRadio = ((RadioButton) view.findViewById(checkedId)).getText().toString();
            }
        });
        group.check((2) + 1);
    }

    public String getSelectedRadio() {
        return selectedRadio;
    }
}
