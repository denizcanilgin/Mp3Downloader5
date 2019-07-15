package com.freemusicdownloader.mp3downloader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FavMusicsAdapter extends ArrayAdapter<FavMusic> {

    TextView tv_MusicName;
    TextView tv_MusicDuration;
    TextView tv_MusicAuthor;

    private Context mContext;
    private List<FavMusic> mSongList;
    private LayoutInflater layoutInflater;

    public FavMusicsAdapter(Context mContext, List<FavMusic> mSongList){
        super(mContext,R.layout.list_item_row,mSongList);
        this.mContext = mContext;
        this.mSongList = mSongList;
        this.layoutInflater = LayoutInflater.from(mContext);

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext, R.layout.list_item_row3, null);


        tv_MusicName = convertView.findViewById(R.id.songtext);
        tv_MusicDuration = convertView.findViewById(R.id.musictime);
        tv_MusicAuthor = convertView.findViewById(R.id.authortext);

        tv_MusicAuthor.setText(mSongList.get(position).getMusicAuthor());
        tv_MusicDuration.setText(mSongList.get(position).getMusicDuration());
        tv_MusicName.setText(mSongList.get(position).getMusicName());

        return convertView;
    }

    @Override
    public void add(FavMusic object) {
        super.add(object);
        mSongList.add(object);
        notifyDataSetChanged();
    }

    @Override
    public void remove( FavMusic object) {
        super.remove(object);
        mSongList.remove(object);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mSongList.size();
    }


}
