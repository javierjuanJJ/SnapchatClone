package whatsappclone.proyecto_javier_juan_uceda.snapchatclone.RecyclerViewReceiver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import whatsappclone.proyecto_javier_juan_uceda.snapchatclone.R;

public class ReceiverAdapater extends RecyclerView.Adapter<ReceiverViewHolder> {

    private List<ReceiverObject> usersList;
    private Context context;

    public ReceiverAdapater(List<ReceiverObject> usersList, Context context){
        this.usersList = usersList;
        this.context = context;
    }
    @Override
    public ReceiverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_receiver_item, null);
        ReceiverViewHolder rcv = new ReceiverViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final ReceiverViewHolder holder, int position) {
        holder.mEmail.setText(usersList.get(position).getEmail());
        holder.mReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean receiveState = !usersList.get(holder.getLayoutPosition()).getReceive();
                usersList.get(holder.getLayoutPosition()).setReceive(receiveState);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.usersList.size();
    }

}
