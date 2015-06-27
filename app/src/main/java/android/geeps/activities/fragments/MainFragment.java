package android.geeps.activities.fragments;

import android.app.ListFragment;
import android.content.res.Resources;
import android.geeps.R;
import android.geeps.adapters.UserOrderAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.geeps.models.UserOrder;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends ListFragment {

    private List<UserOrder> mItems;        // ListView items list

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize the items list
        mItems = new ArrayList<UserOrder>();
        Resources resources = getResources();

        // TODO Aqui adiciona itens a lista, vindos do servidor
        mItems.add(new UserOrder(resources.getDrawable(R.drawable.icon), "Example", "Description"));
        mItems.add(new UserOrder(resources.getDrawable(R.drawable.icon), "Example2", "Description"));
        mItems.add(new UserOrder(resources.getDrawable(R.drawable.icon), "Example3", "Description"));
        mItems.add(new UserOrder(resources.getDrawable(R.drawable.icon), "Example4", "Description"));

        // initialize and set the list adapter
        setListAdapter(new UserOrderAdapter(getActivity(), mItems));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // remove the dividers from the ListView of the ListFragment
        //getListView().setDivider(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve theListView item
        UserOrder item = mItems.get(position);
        // TODO chamar o map activity and conectar com o FirebaseCliente e criar a sala a partir do id do pedido
    }

}