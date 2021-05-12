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


public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
    private List<Appointment> appointmentList;
    private int numOfAppointments;
    private Context context;

    public AppointmentAdapter(List<Appointment> appointments) {
        appointmentList=appointments;
        numOfAppointments = appointmentList.size();
    }

    @NonNull
    @Override
    public AppointmentAdapter.AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.appointment_view, parent, false);
        AppointmentAdapter.AppointmentViewHolder viewHolder = new AppointmentAdapter.AppointmentViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.AppointmentViewHolder holder, int position) {
        String first = context.getResources().getString(R.string.first_dose);
        String second = context.getResources().getString(R.string.second_dose);
        holder.patientName.setText(appointmentList.get(position).getLastName()+" "+appointmentList.get(position).getName());
        holder.appointmentFirstDose.setText(first +" "+appointmentList.get(position).getFirstDose()+" "+appointmentList.get(position).getTime());
        holder.appointmentSecondDose.setText(second +" "+appointmentList.get(position).getSecondDose()+" "+appointmentList.get(position).getTime());
        int backgroundColorForViewHolder = ColorUtils.getViewHolderBackgroundColor(context,position);
        holder.itemView.setBackgroundColor(backgroundColorForViewHolder);
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }


    class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView patientName, appointmentFirstDose,appointmentSecondDose;

        public AppointmentViewHolder(View itemView) {
            super(itemView);
            patientName = (TextView) itemView.findViewById(R.id.patient_name);
            appointmentFirstDose = (TextView) itemView.findViewById(R.id.appointment_first_dose);
            appointmentSecondDose = (TextView) itemView.findViewById(R.id.appointment_second_dose);
        }


    }
}

