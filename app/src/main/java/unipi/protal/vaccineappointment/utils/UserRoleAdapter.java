package unipi.protal.vaccineappointment.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import unipi.protal.vaccineappointment.R;
import unipi.protal.vaccineappointment.entities.Appointment;
import unipi.protal.vaccineappointment.entities.Claims;

public class UserRoleAdapter extends RecyclerView.Adapter<UserRoleAdapter.UserViewHolder> {
    private List<Claims> claimsList;
    private int numOfUsers;
    private Context context;
    final private SwitchClickListener switchClickListener;

    public interface SwitchClickListener {
        void onSwitchClicked(int clickedItemIndex);
    }

    public UserRoleAdapter(List<Claims> claims,SwitchClickListener listener) {
        claimsList=claims;
        numOfUsers = claimsList.size();
        switchClickListener = listener;
    }

    @NonNull
    @Override
    public UserRoleAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.user_role_view, parent, false);
        UserRoleAdapter.UserViewHolder viewHolder = new UserRoleAdapter.UserViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserRoleAdapter.UserViewHolder holder, int position) {
        String first = context.getResources().getString(R.string.first_dose);
        String second = context.getResources().getString(R.string.second_dose);
        holder.userDisplayName.setText(claimsList.get(position).getDisplay_name());
        holder.userEmail.setText(claimsList.get(position).getEmail());
        int backgroundColorForViewHolder = ColorUtils.getViewHolderBackgroundColor(context,position);
        holder.itemView.setBackgroundColor(backgroundColorForViewHolder);
    }

    @Override
    public int getItemCount() {
        return claimsList.size();
    }


    class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView userDisplayName, userEmail,doctorSwitch;

        public UserViewHolder(View itemView) {
            super(itemView);
            userDisplayName = (TextView) itemView.findViewById(R.id.role_user_name);
            userEmail = (TextView) itemView.findViewById(R.id.role_user_email);
            doctorSwitch = (Switch) itemView.findViewById(R.id.user_role_switch);
            doctorSwitch.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            switchClickListener.onSwitchClicked(clickedPosition);
        }
    }
}


