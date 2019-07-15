package com.freemusicdownloader.mp3downloader;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.freemusicdownloader.mp3downloader.Genres.AcousticFragment;
import com.freemusicdownloader.mp3downloader.Genres.BassFragment;
import com.freemusicdownloader.mp3downloader.Genres.ClassicalFragment;
import com.freemusicdownloader.mp3downloader.Genres.JazzFragment;
import com.freemusicdownloader.mp3downloader.Genres.MetalFragment;
import com.freemusicdownloader.mp3downloader.Genres.RelaxFragment;
import com.freemusicdownloader.mp3downloader.Genres.RockFragment;

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

            case  R.id.card_Rock:
                RockFragment nextRock= new RockFragment();
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.genres_layout, nextRock, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            break;

            case  R.id.card_jazz:
                JazzFragment nextJazz= new JazzFragment();
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.genres_layout, nextJazz, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
                break;

            case  R.id.card_acustic:

                AcousticFragment nextAcoustic = new AcousticFragment();
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.genres_layout, nextAcoustic, "findThisFragment")
                        .addToBackStack(null)
                        .commit();

            break;

            case  R.id.card_classical:

                ClassicalFragment nextClassical = new ClassicalFragment();
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.genres_layout, nextClassical, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
                break;

            case  R.id.card_relax:

                RelaxFragment nextRelax = new RelaxFragment();
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.genres_layout, nextRelax, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
                break;

            case  R.id.card_metal:

                MetalFragment nextMetal = new MetalFragment();
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.genres_layout, nextMetal, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
                break;
            case  R.id.card_bass:

                BassFragment nextBass = new BassFragment();
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.genres_layout, nextBass, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
                break;







        }

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
