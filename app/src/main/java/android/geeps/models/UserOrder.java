package android.geeps.models;

import android.graphics.drawable.Drawable;

public class UserOrder {
    // Por enquanto ta icone, nome (title) e uma descricao
    public final Drawable icon;       // the drawable for the ListView item ImageView
    public final String title;        // the text for the ListView item title
    public final String description;  // the text for the ListView item description
    public final String entregador_id;

    public UserOrder(Drawable icon, String title, String description, String entregador_id) {
        this.icon = icon;
        this.title = title;
        this.description = description;
        this.entregador_id = entregador_id;
    }
}