package com.freemusicdownloader.mp3downloader;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/* Fragment used as page 3 */
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

            case  R.id.card_acustic: Log.i("PUSHED","" + view.getId()); break;
            case  R.id.card_bass: Log.i("PUSHED","" + view.getId()); break;
            case  R.id.card_classical: Log.i("PUSHED","" + view.getId()); break;
            case  R.id.card_jazz: Log.i("PUSHED","" + view.getId()); break;
            case  R.id.card_metal: Log.i("PUSHED","" + view.getId()); break;
            case  R.id.card_Rock: Log.i("PUSHED","" + view.getId()); break;
            case  R.id.card_relax: Log.i("PUSHED","" + view.getId()); break;


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
