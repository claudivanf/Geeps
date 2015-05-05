package android.geeps.util;

import android.content.Context;
import android.geeps.R;
import android.geeps.core.UserOrder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Igor on 04/05/2015.
 */
public class UserOrderAdapter extends ArrayAdapter<UserOrder> {

    public UserOrderAdapter(Context context, List<UserOrder> items) {
        super(context, R.layout.main_fragment, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.main_fragment, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.description = (TextView) convertView.findViewById(R.id.secondLine);
            viewHolder.title = (TextView) convertView.findViewById(R.id.firstLine);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        UserOrder item = getItem(position);
        viewHolder.icon.setImageDrawable(item.icon);
        viewHolder.description.setText(item.title);
        viewHolder.title.setText(item.description);

        return convertView;
    }

    private static class ViewHolder {
        ImageView icon;
        TextView description;
        TextView title;
    }

}
