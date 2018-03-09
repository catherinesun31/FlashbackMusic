package com.android.flashbackmusicv000;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


/**
 * Created by Chelsea on 3/9/18.
 *
 * User adapter to create concrete contacts
 */

public class UserAdapter extends BaseAdapter{

    //array list to hold references read from each contact
    // after sign in.
    private ArrayList<User> arrayListUsers;

    private final LayoutInflater linf;

    public UserAdapter(Context context, ArrayList<User> users){
        arrayListUsers = users;
        linf = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayListUsers.size();
    }

    @Override
    public User getItem(int position) {
        return arrayListUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        UserViewHolder viewHolder;

        if(convertView == null){
            convertView = linf.inflate(R.layout.row, parent, false);

            viewHolder = new UserViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        //otherwise get stored reference
        else{
            viewHolder = (UserViewHolder) convertView.getTag();
        }

        viewHolder.setData(position, (User) getItem(position));

        return convertView;
    }
}
