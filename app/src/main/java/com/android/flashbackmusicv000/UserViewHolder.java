package com.android.flashbackmusicv000;
import java.lang.ref.WeakReference;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.flashbackmusicv000.R;

import org.w3c.dom.Text;


/**
 * Created by Chelsea on 3/9/18.
 */

public class UserViewHolder {
    //private final TextView name;
    // It handles how to set bitmaps for the ImageView object referred by the
    // view holder

    // It holds a weak reference to the last task thrown to download a bitmap
    // for the ImageView object referred by the view holder

    /**
     * It finds the references for each element in the entry row layout.
     *
     * @param layoutRow
     *            row layout object that refers one line of the list view object
     *            { @see listView_contactsList } defined by
     *            activity_friends_list layout.
     */
    public UserViewHolder(View layoutRow) {

    }


    /**
     * It returns the ImageView object referred by the view holder
     */

    public void setData(int position, User contact) {
        //name.setText(contact.getUsername());

    }


}
