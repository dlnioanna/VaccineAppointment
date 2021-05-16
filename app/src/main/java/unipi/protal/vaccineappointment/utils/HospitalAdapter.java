package unipi.protal.vaccineappointment.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import unipi.protal.vaccineappointment.R;
import unipi.protal.vaccineappointment.entities.Hospital;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder> {
    private List<Hospital> hospitalList;
    final private ListItemClickListener mOnClickListener;
    private Context context;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public HospitalAdapter(List<Hospital> hospitals, ListItemClickListener listener) {
        mOnClickListener = listener;
        hospitalList = hospitals;
    }

    //called right when the adapter is created and is used to initialize viewholder.
    @NonNull
    @Override
    public HospitalAdapter.HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.hospital_view, parent, false);
        HospitalViewHolder viewHolder = new HospitalViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    /*
     update the RecyclerView.ViewHolder contents with the item
     at the given position and also sets up some private fields to be used by RecyclerView.
     */
    @Override
    public void onBindViewHolder(@NonNull HospitalAdapter.HospitalViewHolder holder, int position) {
        holder.hospitalTitle.setText(hospitalList.get(position).getTitle());
        holder.hospitalDescription.setText(hospitalList.get(position).getDescription());
        int backgroundColorForViewHolder = ColorUtils.getViewHolderBackgroundColor(context, position);
        holder.itemView.setBackgroundColor(backgroundColorForViewHolder);

    }

    // Returns the total number of hospitals to be held by the adapter.
    @Override
    public int getItemCount() {
        return hospitalList.size();
    }


    /**
     * Children views for a list item.
     */
    class HospitalViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView hospitalTitle, hospitalDescription;

        /**
         * Constructor for ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         */
        public HospitalViewHolder(View itemView) {
            super(itemView);
            hospitalTitle = (TextView) itemView.findViewById(R.id.hospital_title);
            hospitalDescription = (TextView) itemView.findViewById(R.id.hospital_description);
            itemView.setOnClickListener(this);
        }


        /**
         * Called whenever a user clicks on an item in the list.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
