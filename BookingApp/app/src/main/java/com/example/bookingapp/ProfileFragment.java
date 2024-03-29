package com.example.bookingapp;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.dto.users.GuestDTO;
import com.example.bookingapp.dto.users.UserDTO;
import com.example.bookingapp.fragments.ReportUserDialogFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static Long id;
    private UserDTO user;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public ProfileFragment(Long id){
        this.id = id;
    }

    public static ProfileFragment newInstance(Long id) {
        ProfileFragment fragment = new ProfileFragment(id);
        Bundle args = new Bundle();
        args.putLong("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Call<UserDTO> call = ClientUtils.userService.getById(id);
            try{
                Response<UserDTO> response = call.execute();
                user = (UserDTO) response.body();
            }catch(Exception ex){
                System.out.println("EXCEPTION WHILE GETTING USER");
                ex.printStackTrace();
            }
        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView bigProfilePic = view.findViewById(R.id.profile_pic);
        ImageView miniProfilePic = view.findViewById(R.id.mini_profile_pic);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Call<List<String>> imageCall = ClientUtils.userService.getImage(user.getId());
        try{
            Response<List<String>> response = imageCall.execute();
            List<String> images = (List<String>) response.body();
            if(images!=null && !images.isEmpty()) {
                byte[] bytes = Base64.decode(images.get(0), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                miniProfilePic.setImageBitmap(bitmap);
                bigProfilePic.setImageBitmap(bitmap);
            }
        }catch(Exception ex){
            System.out.println("EXCEPTION WHILE GETTING IMAGES");
            ex.printStackTrace();
        }

        TextView name = view.findViewById(R.id.name);
        name.setText(user.getName() + " " + user.getSurname());

        TextView role = view.findViewById(R.id.role);
        role.setText(user.getRole().toString());

        TextView email = view.findViewById(R.id.email_profile);
        email.setText(user.getEmail());

        TextView address = view.findViewById(R.id.address_profile);
        address.setText(user.getAddress());

        TextView phone = view.findViewById(R.id.phone_profile);
        phone.setText(user.getPhone());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ImageView icon = view.findViewById(R.id.report_icon);

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(requireContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_report, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final String report = String.valueOf(R.id.report);
                        final String itemId = String.valueOf(item.getItemId());
                        if (report.equals(itemId)){
                            //Toast.makeText(getActivity(), "Reporting user ...", Toast.LENGTH_SHORT).show();
                            ReportUserDialogFragment dialogFragment = new ReportUserDialogFragment(id);
                            dialogFragment.show(getActivity().getSupportFragmentManager(), "report_user");
                            return true;
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });


    }

}