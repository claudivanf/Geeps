package android.geeps.core;

import android.graphics.drawable.Drawable;

/**
 * Created by Igor on 04/05/2015.
 */
public class UserOrder {
    // Por enquanto ta icone, nome (title) e uma descricao
    public final Drawable icon;       // the drawable for the ListView item ImageView
    public final String title;        // the text for the ListView item title
    public final String description;  // the text for the ListView item description

    public UserOrder(Drawable icon, String title, String description) {
        this.icon = icon;
        this.title = title;
        this.description = description;
    }
}
