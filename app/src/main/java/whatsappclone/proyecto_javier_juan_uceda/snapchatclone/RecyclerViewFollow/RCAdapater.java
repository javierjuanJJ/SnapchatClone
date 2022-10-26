package whatsappclone.proyecto_javier_juan_uceda.snapchatclone.RecyclerViewFollow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import whatsappclone.proyecto_javier_juan_uceda.snapchatclone.R;

public class RCAdapater extends RecyclerView.Adapter<RCViewHolders> {
    private List<UsersObject> usersList;
    private Context context;

    public RCAdapater (List<UsersObject> usersList, Context context){
        this.usersList = usersList;
        this.context = context;
    }
    @Override
    public RCViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylerview_followers_item, null);
        RCViewHolders rcv = new RCViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RCViewHolders holder, int position) {
        holder.mEmail.setText(usersList.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}
