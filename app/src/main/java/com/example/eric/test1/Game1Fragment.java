package com.example.eric.test1;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.content.Context;
import android.text.InputFilter;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Game1Fragment extends Fragment {

    private TextView question;
    private TextView record;
    private EditText answer_box;
    private ImageView image;
    private Button submit_button;
    private Button new_button;


    private String string_question;
    private String string_no_record;
    private String string_mark_statement;
    private String string_answered_question_statement;
    private String string_right;
    private String string_wrong;
    private String string_is;
    private String string_correct_rate;
    private final String fileName = "record.txt";

    private int answer = -1;
    private int mark = -1;
    private int answered_question;
    private final int MAX = 1000;


    public Game1Fragment() {
        // Required empty public constructor
    }
    // hello from mac :)
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        string_no_record = getActivity().getResources().getString(R.string.no_record);
        string_mark_statement = getActivity().getResources().getString(R.string.mark_statement);
        string_answered_question_statement = getActivity().getResources().getString(R.string.answered_question_statement);
        string_right = getActivity().getResources().getString(R.string.right);
        string_wrong = getActivity().getResources().getString(R.string.wrong);
        string_is = getActivity().getResources().getString(R.string.is);
        string_correct_rate = getActivity().getResources().getString(R.string.correct_rate);



        calculate();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game1, container, false);

        //   ------------------------------------Set View----------------------------------------
        answer_box = (EditText) view.findViewById(R.id.answer_box);
        question = (TextView) view.findViewById(R.id.question);
        record = (TextView) view.findViewById(R.id.record);
        image = (ImageView) view.findViewById(R.id.imageView1);
        submit_button = (Button) view.findViewById(R.id.button2);
        new_button = (Button) view.findViewById(R.id.button1);
        // ---------------------------------------------------------------------------------

        // NEW Btton event
        new_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // focus the answer box
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.showSoftInput(answer_box, InputMethodManager.SHOW_IMPLICIT);

                answer_box_setMaxLength(9);
                setButtonWeight(1.0f, 1.0f);
                setQuestionImageWeight(3f, 0f);
                answer_box.setText("");
                calculate();
                question.setTextSize(50);
                question.setText(string_question);
            }
        });
        // Submit Button Event
        submit_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (!answer_box.getText().toString().matches("")) {
                    // unfocus the answer box area
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(answer_box.getWindowToken(), 0);

                    setButtonWeight(2.0f, 0.0f);
                    answer_box_setMaxLength(0);
                    showAnswer();
                    answer_box.setText("");
                }

                writeDataToStorage();
            }


        });

        return view;
    } // end of onCreateView

    public void onStart(){
        super.onStart();

        // initialize mark and answered_question
        readDataFromStorage();

        answer_box.setText("");
        question.setTextSize(50);
        question.setText(string_question);

        // focus the answer box
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.showSoftInput(answer_box, InputMethodManager.SHOW_IMPLICIT);

    } // end of onStart

    public void onPause (){
        super.onPause();

        // unfocus the answer box area
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(answer_box.getWindowToken(), 0);
    }


    // ***************************************************************************

    void writeDataToStorage() {

        try {

            String content = String.format("%d\n%d",mark,answered_question);

            File file = new File(getActivity().getFilesDir(), fileName);


            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    } // end of writeDataToStorage

    void readDataFromStorage() {
        File file = new File(getActivity().getFilesDir(), fileName);
        BufferedReader reader = null;
        List<Integer> list = new ArrayList<Integer>();
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            reader = new BufferedReader(new FileReader(file));
            String text = null;

            while ((text = reader.readLine()) != null) {
                list.add(Integer.parseInt(text));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (list.size() == 0) {
            mark = 0;
            answered_question = 0;
            record.setText(string_no_record);
        } else {
            mark = list.get(0);
            answered_question = list.get(1);
            int temp = (int) (((double) mark) / answered_question * 100);
            record.setText(String.format("%s %d\n%s %d\n%s %d%%", string_mark_statement, mark, string_answered_question_statement, answered_question, string_correct_rate, temp));

        }
    } // end of readDataFromStorage



    void answer_box_setMaxLength(int length) {
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(length);
        answer_box.setFilters(fArray);
    } // end of answer_box_setMaxLength

    void setQuestionImageWeight(float questionWeight, float imageWeight) {

        LinearLayout.LayoutParams question_params = (LinearLayout.LayoutParams) question
                .getLayoutParams();
        question_params.weight = questionWeight;
        question.setLayoutParams(question_params);


        LinearLayout.LayoutParams image_params = (LinearLayout.LayoutParams) image
                .getLayoutParams();
        image_params.weight = imageWeight;
        image.setLayoutParams(image_params);

    } // end of setQuestionImageWeight


    void setButtonWeight(float new_button_weight, float submit_button_weight) {
        //  use to set layout_weight,
        // IF <0, no change
        LinearLayout.LayoutParams new_button_params = (LinearLayout.LayoutParams) new_button
                .getLayoutParams();
        new_button_params.weight = new_button_weight;
        new_button.setLayoutParams(new_button_params);

        LinearLayout.LayoutParams submit_button_params = (LinearLayout.LayoutParams) submit_button
                .getLayoutParams();
        submit_button_params.weight = submit_button_weight;
        submit_button.setLayoutParams(submit_button_params);

    } // end of setButtonWeight

    // ------------------------------------------------------------------
    public void calculate() {
        int number1, number2, type;
        Random random = new Random();
        number1 = random.nextInt(MAX);
        number2 = random.nextInt(MAX);
        type = random.nextInt(2);
        if (type == 1)
            if (number1 < number2) {
                int temp = number1;
                number1 = number2;
                number2 = temp;
            }
        string_question = String.valueOf(number1) + ((type == 0) ? " + " : " - ")
                + String.valueOf(number2);
        if (type == 0)
            answer = number1 + number2;
        else
            answer = number1 - number2;

    } // end of calculate

    public void showAnswer() {

        int num = (int) Integer.valueOf(answer_box.getText().toString());
        if (num == answer) {
            image.setImageResource(R.drawable.tick);
            question.setText(string_right);
            question.setTextSize(33);
            ++mark;
        } else {

            question.setText(String.format("%s %s %s %d", string_wrong, string_question, string_is, answer));
            question.setTextSize(21);
            image.setImageResource(R.drawable.cross);

        }

        setQuestionImageWeight(2f, 1f);

        ++answered_question;
        int temp = (int) (((double) mark) / answered_question * 100);
        record.setText(String.format("%s %d\n%s %d\n%s %d%%", string_mark_statement, mark, string_answered_question_statement, answered_question, string_correct_rate, temp));

    } // end of showAnswer


    // ***************************************************************************

}