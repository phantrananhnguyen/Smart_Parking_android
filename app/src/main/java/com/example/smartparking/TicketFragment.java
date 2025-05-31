    package com.example.smartparking;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.ImageButton;
    import android.widget.Toast;

    import androidx.fragment.app.Fragment;
    import androidx.fragment.app.FragmentManager;
    import androidx.fragment.app.FragmentTransaction;
    import androidx.lifecycle.ViewModelProvider;
    import androidx.navigation.Navigation;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.smartparking.Models.ApiClient;
    import com.example.smartparking.Models.MonthTicket;
    import com.example.smartparking.Models.TicketCount;
    import com.example.smartparking.Models.TicketItem;
    import com.example.smartparking.Models.UserSession;

    import java.util.ArrayList;
    import java.util.List;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class TicketFragment extends Fragment {
        RecyclerView recyclerView;
        AuthApi authApi;
        String email;
        private TicketAdapter adapter;
        private List<MonthTicket> ticketsList = new ArrayList<>();
        private List<TicketItem> ticketItems = new ArrayList<>();
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_ticket, container, false);
            email = UserSession.getInstance().getEmail();
            authApi = ApiClient.getClient().create(AuthApi.class);

            recyclerView = view.findViewById(R.id.recyclerViewHistory);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new TicketAdapter(ticketItems, getContext());
            recyclerView.setAdapter(adapter);

            fetchTicket();

            ImageButton addticket = view.findViewById(R.id.add);
            addticket.setOnClickListener(v -> {
                if (UserSession.getInstance().getTicketStatus().equals("Expired")){
                    Navigation.findNavController(v).navigate(R.id.action_ticketFragment1_to_ticketFragment2);
                }
                else {
                    Toast.makeText(getContext(), "You have a valid ticket. Please wait", Toast.LENGTH_SHORT).show();
                }
            });

            return view;
        }

        private void fetchTicket() {
            if (email == null || email.isEmpty()) {
                return;
            }

            authApi.fetchHistory(email).enqueue(new Callback<List<MonthTicket>>() {
                @Override
                public void onResponse(Call<List<MonthTicket>> call, Response<List<MonthTicket>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (!isAdded()) return;
                        ticketsList.clear();
                        ticketItems.clear();
                        ticketsList.addAll(response.body());
                        for (MonthTicket ticket : ticketsList) {
                            Log.d("TicketFragment", ticket.toString());

                            ticketItems.add(new TicketItem(ticket.getMonthAmount(), ticket.getStartDate(), ticket.getEndDate()));
                        }
                        Log.d("TicketFragment", "TicketItems size: " + ticketItems.size());
                        try {
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.e("TicketFragment", "Error when updating adapter: ", e);
                        }

                    } else {
                        Log.d("TicketFragment", "Response not successful or empty");
                    }
                }

                @Override
                public void onFailure(Call<List<MonthTicket>> call, Throwable t) {
                    Log.d("TicketFragment", "Error: " + t.getMessage());
                }
            });
        }

    }