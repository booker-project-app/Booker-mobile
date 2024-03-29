package com.example.bookingapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.bookingapp.FragmentTransition;
import com.example.bookingapp.ProfileFragment;
import com.example.bookingapp.R;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.dto.users.UserDTO;
import com.example.bookingapp.enums.ReservationStatus;
import com.example.bookingapp.fragments.AccommodationViewFragment;
import com.example.bookingapp.fragments.ReservationGuestFragment;
import com.example.bookingapp.model.Accommodation;
import com.example.bookingapp.model.AccommodationRequestDTO;
import com.example.bookingapp.model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ReservationGuestAdapter extends ArrayAdapter<Reservation> {

    private ArrayList<Reservation> reservations;
    Context context;

    public ReservationGuestAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context = context;
    }

    public ReservationGuestAdapter(Context context, ArrayList<Reservation> reservations){
        super(context, R.layout.guest_reservation_card, reservations);
        this.reservations = reservations;
        this.context = context;
    }
    /*
     * Ova metoda vraca ukupan broj elemenata u listi koje treba prikazati
     * */
    @Override
    public int getCount() {
        return reservations.size();
    }

    /*
     * Ova metoda vraca pojedinacan element na osnovu pozicije
     * */
    @Nullable
    @Override
    public Reservation getItem(int position) {
        return reservations.get(position);
    }

    /*
     * Ova metoda vraca jedinstveni identifikator, za adaptere koji prikazuju
     * listu ili niz, pozicija je dovoljno dobra. Naravno mozemo iskoristiti i
     * jedinstveni identifikator objekta, ako on postoji.
     * */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * Ova metoda popunjava pojedinacan element ListView-a podacima.
     * Ako adapter cuva listu od n elemenata, adapter ce u petlji ici
     * onoliko puta koliko getCount() vrati. Prilikom svake iteracije
     * uzece java objekat sa odredjene poziciuje (model) koji cuva podatke,
     * i layout koji treba da prikaze te podatke (view) npr R.layout.AccommodationListing_card.
     * Kada adapter ima model i view, prosto ce uzeti podatke iz modela,
     * popuniti view podacima i poslati listview da prikaze, i nastavice
     * sledecu iteraciju.
     * */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Reservation reservation = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.guest_reservation_card,
                    parent, false);
        }
        //getting accommodation
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Call<Accommodation> acc = ClientUtils.accommodationService.findAccommodation(reservation.getAccommodation());
        try{
            Response<Accommodation> response = acc.execute();
            Accommodation accommodation = (Accommodation) response.body();

            LinearLayout card = convertView.findViewById(R.id.reservation_owner_card);
            ImageView image = convertView.findViewById(R.id.accommodation_image);
            TextView title = convertView.findViewById(R.id.title);
            TextView status = convertView.findViewById(R.id.reservation_status);
            TextView dates = convertView.findViewById(R.id.reservation_dates);
            TextView totalPrice = convertView.findViewById(R.id.price_total);
            RatingBar ratingBar = convertView.findViewById(R.id.rating);
            LinearLayout cancel = convertView.findViewById(R.id.cancel);
            Button cancel_btn = convertView.findViewById(R.id.cancel_btn);


            if(reservation != null){
                //getting images
                StrictMode.setThreadPolicy(policy);
                Call<List<String>> imageCall = ClientUtils.accommodationService.getImages(accommodation.getId());
                try{
                    Response<List<String>> responseIMG = imageCall.execute();
                    List<String> images = (List<String>) responseIMG.body();
                    if(images!=null && !images.isEmpty()) {
                        byte[] bytes = Base64.decode(images.get(0), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        image.setImageBitmap(bitmap);
                    }
                }catch(Exception ex){
                    System.out.println("EXCEPTION WHILE GETTING IMAGES");
                    ex.printStackTrace();
                }
                title.setText(accommodation.getTitle());
                status.setText(reservation.getStatusFormated());
                totalPrice.setText(reservation.getPrice() + "$");
                dates.setText("from " + reservation.getFromDate() + " to " + reservation.getToDate());
                System.out.println(reservation.getStatus().toString());
                if (reservation.getStatus() == ReservationStatus.CANCELED){
                    cancel.setVisibility(View.GONE);
                }
                else {
                    cancel.setVisibility(View.VISIBLE);
                    cancel_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // cancel reservation
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date startDate = sdf.parse(reservation.getFromDate());
                                    Date deadLine = daysCalculator(startDate, -accommodation.getDeadline());
                                    if (deadLine.before(new Date()) || deadLine.equals(new Date())){
                                        Toast.makeText(context, "You can't cancel this reservation because cancellation period expired!", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        // send request
                                        Call<Boolean> canceledCall = ClientUtils.reservationService.cancelReservation(reservation.getId());
                                        try{
                                            Response<Boolean> response = canceledCall.execute();
                                            Boolean canceled = (Boolean) response.body();
                                            if (canceled) {
                                                Toast.makeText(context, "Canceled reservation!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, "Error while reservation cancellation!", Toast.LENGTH_SHORT).show();
                                            }
                                            FragmentTransition.to(ReservationGuestFragment.newInstance(), (FragmentActivity) context, false, R.id.fragment_placeholder);
                                        }catch(Exception ex){
                                            ex.printStackTrace();
                                        }
                                    }
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                        }
                    });
                }
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransition.to(AccommodationViewFragment.newInstance(accommodation), (FragmentActivity) context, true, R.id.fragment_placeholder);
                    }
                });
                title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransition.to(AccommodationViewFragment.newInstance(accommodation), (FragmentActivity) context, true, R.id.fragment_placeholder);
                    }
                });
            }
        }catch(Exception ex){
            System.out.println("EXCEPTION WHILE GETTING ACCOMMODATION");
            ex.printStackTrace();
        }


        return convertView;
    }

    public Date daysCalculator(Date date, int days){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }
}
