package unipi.protal.vaccineappointment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder> {
    private List<Hospital> hospitalList;
    final private ListItemClickListener mOnClickListener;
    private int numOfHospitals;
    private static int viewHolderCount;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public HospitalAdapter(int items, ListItemClickListener listener) {
        mOnClickListener = listener;
        numOfHospitals = items;
        viewHolderCount = 0;
    }

    @NonNull
    @Override
    public HospitalAdapter.HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.hospital_view, parent, false);
        HospitalViewHolder viewHolder = new HospitalViewHolder(view);

        viewHolder.viewHolderIndex.setText("ViewHolder index: " + viewHolderCount);

//        int backgroundColorForViewHolder = ColorUtils
//                .getViewHolderBackgroundColorFromInstance(context, viewHolderCount);
//        viewHolder.itemView.setBackgroundColor(backgroundColorForViewHolder);

//        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "
//                + viewHolderCount);
        viewHolderCount++;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalAdapter.HospitalViewHolder holder, int position) {
        holder.viewHolderIndex.setText(String.valueOf(position));

    }

    @Override
    public int getItemCount() {
        return 10;
    }


    class HospitalViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView viewHolderIndex;

        public HospitalViewHolder(View itemView) {
            super(itemView);
            viewHolderIndex = (TextView) itemView.findViewById(R.id.hospital_title);
            itemView.setOnClickListener(this);
        }

//        /**
//         * A method we wrote for convenience. This method will take an integer as input and
//         * use that integer to display the appropriate text within a list item.
//         * @param listIndex Position of the item in the list
//         */
//        void bind(int listIndex) {
//            listItemNumberView.setText(String.valueOf(listIndex));
//        }


        // COMPLETED (6) Override onClick, passing the clicked item's position (getAdapterPosition()) to mOnClickListener via its onListItemClick method

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
