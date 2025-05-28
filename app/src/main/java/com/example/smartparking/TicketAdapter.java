package com.example.smartparking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartparking.Models.TicketItem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private List<TicketItem> monthTickets;
    private Context context;

    // Định dạng đầu vào ngày giờ dạng ISO: "2025-05-28T21:37:13"
    private static final DateTimeFormatter inputFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

    // Định dạng đầu ra ngày tháng theo kiểu Việt Nam: "dd/MM/yyyy"
    private static final DateTimeFormatter outputFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("vi", "VN"));

    public TicketAdapter(List<TicketItem> monthTickets, Context context) {
        this.monthTickets = monthTickets;
        this.context = context;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.tickets, viewGroup, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        TicketItem monthTicket = monthTickets.get(position);

        // Format start date
        String formattedStart = formatDateString(monthTicket.getStart());
        // Format end date
        String formattedEnd = formatDateString(monthTicket.getEnd());

        holder.tvAmount.setText(String.valueOf(monthTicket.getAmount()) + " Month");
        holder.tvStart.setText(formattedStart);
        holder.tvEnd.setText(formattedEnd);

        // Xử lý trạng thái Active/Expired và đổi màu background
        try {
            LocalDateTime endDate = LocalDateTime.parse(monthTicket.getEnd(), inputFormatter);
            LocalDateTime now = LocalDateTime.now();

            if (endDate.isAfter(now)) {
                holder.tvStatus.setText("Active");
                holder.tvStatus.setBackgroundResource(R.drawable.status_active);
                holder.tvStatus.setTextColor(Color.WHITE);
            } else {
                holder.tvStatus.setText("Expired");
                holder.tvStatus.setBackgroundResource(R.drawable.status_expired);
                holder.tvStatus.setTextColor(Color.WHITE);
            }
        } catch (DateTimeParseException e) {
            holder.tvStatus.setText("Unknown");
            holder.itemView.setBackgroundColor(Color.LTGRAY); // xám nhẹ
        }
    }

    @Override
    public int getItemCount() {
        return monthTickets != null ? monthTickets.size() : 0;
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView tvAmount, tvStart, tvEnd, tvStatus;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.amount);
            tvStart = itemView.findViewById(R.id.start);
            tvEnd = itemView.findViewById(R.id.end);
            tvStatus = itemView.findViewById(R.id.status);
        }
    }

    // Hàm tiện ích format ngày String input sang String output
    private String formatDateString(String inputDateStr) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(inputDateStr, inputFormatter);
            return dateTime.format(outputFormatter);
        } catch (DateTimeParseException e) {
            // Nếu parse lỗi thì trả về nguyên chuỗi đầu vào
            return inputDateStr;
        }
    }
}
