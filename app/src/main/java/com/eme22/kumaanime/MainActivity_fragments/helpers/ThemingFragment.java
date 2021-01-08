package com.eme22.kumaanime.MainActivity_fragments.helpers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eme22.kumaanime.AppUtils.Theming;
import com.eme22.kumaanime.MainActivity_fragments.adapters.ThemingSettingsAdapter;
import com.eme22.kumaanime.MainActivity_fragments.util.NewAnimeFetcher;
import com.eme22.kumaanime.R;
import com.tingyik90.prefmanager.PrefManager;

import java.util.ArrayList;


public class ThemingFragment extends Fragment {

    PrefManager pref;
    private Theming theme;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_theming, container, false);
        theme  = new Theming(requireContext());
        pref = new PrefManager(requireContext());
        Toolbar toolbar = v.findViewById(R.id.theming_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v2 -> requireActivity().getSupportFragmentManager().popBackStack());

        ThemingSettingsAdapter adapter1 = new ThemingSettingsAdapter(getContext(), setcolorlist(), color -> {

            switch (color){
                default:{
                    if (theme.gettheme()==0){
                        alreadythemed();
                    }
                    else {
                        theme.settheme(0);
                        requireActivity().recreate();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                    break;
                }
                case 1:{
                    if (theme.gettheme()==1){
                        alreadythemed();
                    }
                    else {
                        theme.settheme(1);
                        requireActivity().recreate();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                    break;
                }
                case 2:{
                    if (theme.gettheme()==2){
                        alreadythemed();
                    }
                    else {
                        theme.settheme(2);
                        requireActivity().recreate();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                    break;
                }
                case 3:{
                    if (theme.gettheme()==3){
                        alreadythemed();
                    }
                    else {
                        theme.settheme(3);
                        requireActivity().recreate();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                    break;
                }
                case 4:{
                    if (theme.gettheme()==4){
                        alreadythemed();
                    }
                    else {
                        theme.settheme(4);
                        requireActivity().recreate();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                    break;
                }
                case 5:{
                    if (theme.gettheme()==5){
                        alreadythemed();
                    }
                    else {
                        theme.settheme(5);
                        requireActivity().recreate();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                    break;
                }
            }

        });
        RecyclerView rs = v.findViewById(R.id.theming_recycler);
        rs.setLayoutManager(new LinearLayoutManager(getContext()));
        rs.setAdapter(adapter1);
        /*
        Button button_def = (Button) v.findViewById(R.id.theme_default);
        Button button_red = (Button) v.findViewById(R.id.theme_red);
        Button button_green = (Button) v.findViewById(R.id.theme_green);
        Button button_purple = (Button) v.findViewById(R.id.theme_purple);
        Button button_orange = (Button) v.findViewById(R.id.theme_orange);
        Button button_blue = (Button) v.findViewById(R.id.theme_blue);
        button_def.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (theme.gettheme()==0){
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.same_theme), duration);
                    toast.show();
                }
                else {
                    theme.settheme(0);
                    Resources.Theme theme = getActivity().getTheme();
                    System.out.println(theme.toString());
                    getActivity().recreate();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
        button_red.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (theme.gettheme()==1){
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.same_theme), duration);
                    toast.show();
                }
                else {
                    theme.settheme(1);
                    Resources.Theme theme = getActivity().getTheme();
                    System.out.println(theme.toString());
                    getActivity().recreate();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
        button_green.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (theme.gettheme()==2){
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.same_theme), duration);
                    toast.show();
                }
                else {
                theme.settheme(2);
                Resources.Theme theme = getActivity().getTheme();
                System.out.println(theme.toString());
                getActivity().recreate();
                getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
        button_purple.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (theme.gettheme()==3){
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.same_theme), duration);
                    toast.show();
                }
                else {
                    theme.settheme(3);
                    Resources.Theme theme = getActivity().getTheme();
                    System.out.println(theme.toString());
                    getActivity().recreate();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
        button_orange.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (theme.gettheme()==4){
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.same_theme), duration);
                    toast.show();
                }
                else {
                    theme.settheme(4);
                    Resources.Theme theme = getActivity().getTheme();
                    System.out.println(theme.toString());
                    getActivity().recreate();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
        button_blue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (theme.gettheme()==5){
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.same_theme), duration);
                    toast.show();
                }
                else {
                theme.settheme(5);
                Resources.Theme theme = getActivity().getTheme();
                System.out.println(theme.toString());
                getActivity().recreate();
                getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });


         */
        return v;
    }

    private static ArrayList<NewAnimeFetcher.Tuple<String,Integer>> setcolorlist(){
        ArrayList<NewAnimeFetcher.Tuple<String, Integer>> a = new ArrayList<>();
        NewAnimeFetcher.Tuple<String,Integer> b = new NewAnimeFetcher.Tuple<>();
        b.setFirst("Cian");
        b.setSecond(R.color.teal_200);
        a.add(0, b);
        NewAnimeFetcher.Tuple<String,Integer> c = new NewAnimeFetcher.Tuple<>();
        c.setFirst("Rojo");
        c.setSecond(R.color.red);
        a.add(1, c);
        NewAnimeFetcher.Tuple<String,Integer> d = new NewAnimeFetcher.Tuple<>();
        d.setFirst("Verde");
        d.setSecond(R.color.green);
        a.add(2, d);
        NewAnimeFetcher.Tuple<String,Integer> e = new NewAnimeFetcher.Tuple<>();
        e.setFirst("Púrpura");
        e.setSecond(R.color.purple_200);
        a.add(3, e);
        NewAnimeFetcher.Tuple<String,Integer> f = new NewAnimeFetcher.Tuple<>();
        f.setFirst("Naranja");
        f.setSecond(R.color.orange);
        a.add(4, f);
        NewAnimeFetcher.Tuple<String,Integer> g = new NewAnimeFetcher.Tuple<>();
        g.setFirst("Azul");
        g.setSecond(R.color.blue);
        a.add(5, g);
        return a;
    }

    void alreadythemed(){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(requireContext(), getString(R.string.same_theme), duration);
        toast.show();
    }
}