package com.example.mplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

public class NewListAdpter extends ArrayAdapter {
    private String item[];
    private Context context;
    public NewListAdpter(@NonNull Context context, int resource, @NonNull String[] item) {
        super(context, resource, item);
        this.context = context;
        this.item = item;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return item[position];
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.song_home_app, parent, false);
        TextView t = convertView.findViewById(R.id.textView2);
        t.setText(item[position]);
        return convertView;
    }

}
