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
import unipi.protal.vaccineappointment.entities.Appointment;
import unipi.protal.vaccineappointment.entities.Hospital;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
    private List<Appointment> appointmentList;
    final private AppointmentAdapter.ListItemClickListener mOnClickListener;
    private int numOfHospitals;
    private Context context;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public AppointmentAdapter(List<Appointment> appointments, AppointmentAdapter.ListItemClickListener listener) {
        mOnClickListener = listener;
        appointmentList=appointments;
        numOfHospitals = appointmentList.size();
    }

    @NonNull
    @Override
    public AppointmentAdapter.AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.hospital_view, parent, false);
        AppointmentAdapter.AppointmentViewHolder viewHolder = new AppointmentAdapter.AppointmentViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.AppointmentViewHolder holder, int position) {
        holder.hospitalTitle.setText(appointmentList.get(position).getLastName());
        holder.hospitalDescription.setText(appointmentList.get(position).getFirstDose());
        int backgroundColorForViewHolder = ColorUtils.getViewHolderBackgroundColor(context,position);
        holder.itemView.setBackgroundColor(backgroundColorForViewHolder);

    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }


    class AppointmentViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView hospitalTitle, hospitalDescription;

        public AppointmentViewHolder(View itemView) {
            super(itemView);
            hospitalTitle = (TextView) itemView.findViewById(R.id.hospital_title);
            hospitalDescription = (TextView) itemView.findViewById(R.id.hospital_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}

