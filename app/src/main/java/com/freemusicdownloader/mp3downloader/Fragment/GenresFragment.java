package com.freemusicdownloader.mp3downloader.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.freemusicdownloader.mp3downloader.Constans.Constants;
import com.freemusicdownloader.mp3downloader.Constans.GlobalData;
import com.freemusicdownloader.mp3downloader.R;


public class GenresFragment extends Fragment implements View.OnClickListener {

    private CardView cv_jazz;
    private CardView cv_drumm;
    private CardView cv_relax;
    private CardView cv_classical;
    private CardView cv_rock;
    private CardView cv_acoustic;
    private CardView cv_metal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page3, container, false);

        define(rootView);

        return rootView;
    }


    @Override
    public void onClick(View view) {


        switch (view.getId()){

            case  R.id.card_Rock: passToHome(Constants.GENRES_ROCK);break;
            case  R.id.card_jazz: passToHome(Constants.GENRES_JAZZ);break;
            case  R.id.card_acustic: passToHome(Constants.GENRES_ACOUSTIC);break;
            case  R.id.card_classical: passToHome(Constants.GENRES_CLASSICAL);break;
            case  R.id.card_relax: passToHome(Constants.GENRES_RELAX);break;
            case  R.id.card_metal: passToHome(Constants.GENRES_METAL); break;
            case  R.id.card_bass: passToHome(Constants.GENRES_BASSDRUM);break;

        }

    }

    public void passToHome(String decoder){
        GlobalData globalData = new GlobalData();
        globalData.setGenresDecoder(decoder);

        GenresMusicListFragment genresMusicListFragment = new GenresMusicListFragment();
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.genres_layout, genresMusicListFragment, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }

    public void define(View rootView){

        cv_acoustic = rootView.findViewById(R.id.card_acustic);
        cv_classical = rootView.findViewById(R.id.card_classical);
        cv_drumm = rootView.findViewById(R.id.card_bass);
        cv_jazz = rootView.findViewById(R.id.card_jazz);
        cv_metal = rootView.findViewById(R.id.card_metal);
        cv_relax = rootView.findViewById(R.id.card_relax);
        cv_rock = rootView.findViewById(R.id.card_Rock);

        cv_acoustic.setOnClickListener(this);
        cv_classical.setOnClickListener(this);
        cv_drumm.setOnClickListener(this);
        cv_jazz.setOnClickListener(this);
        cv_metal.setOnClickListener(this);
        cv_relax.setOnClickListener(this);
        cv_rock.setOnClickListener(this);

    }
}
