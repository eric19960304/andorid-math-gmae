package com.example.eric.test1;


import android.os.Bundle;
import android.view.LayoutInflater;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class Game2Fragment extends Fragment {


    public Game2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game2, container, false);
    }

}