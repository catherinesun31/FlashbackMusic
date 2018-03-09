package com.android.flashbackmusicv000;
import java.lang.ref.WeakReference;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.flashbackmusicv000.R;


/**
 * Created by Chelsea on 3/9/18.
 */

public class UserViewHolder {
    private final ImageView picture;
    private final TextView name;
    private final TextView profileUrl;

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
        picture = (ImageView) layoutRow.findViewById(R.id.imageView_contactPicture);
        name = (TextView) layoutRow.findViewById(R.id.textView_contactName);
        profileUrl = (TextView) layoutRow.findViewById(R.id.textView_contactProfileUrl);
    }


    /**
     * It returns the ImageView object referred by the view holder
     */
    public ImageView getPicture() {
        return picture;
    }

    /**
     * It returns a reference to the last task thrown to download a bitmap for
     * the ImageView object referred by the view holder.
     */

}
