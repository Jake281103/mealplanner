package com.example.mealmate.ui.grocerylist;

import android.Manifest;  // FIX: Import correct Android Manifest
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mealmate.GroceryListAdapter;
import com.example.mealmate.RecipeDatabaseHandler;
import com.example.mealmate.databinding.FragmentGrocerylistBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Set;

public class GroceryListFragment extends Fragment {

    private FragmentGrocerylistBinding binding;
    private GroceryListAdapter adapter;
    private ArrayList<String> groceryList;
    private RecipeDatabaseHandler databaseHandler;
    private Button goToMap, sendSMS;
    private String phoneNumberToSend;  // FIX: Store phone number globally

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGrocerylistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        databaseHandler = new RecipeDatabaseHandler(requireContext());

        // Set up RecyclerView
        groceryList = new ArrayList<>();
        binding.groceryListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GroceryListAdapter(getContext(), groceryList);
        binding.groceryListRecyclerView.setAdapter(adapter);

        // Load grocery list from Firestore
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = (mAuth.getCurrentUser() != null) ? mAuth.getCurrentUser().getUid() : null;

        if (userId == null) {
            Log.e("UserRecipesActivity", "User is not logged in.");
        } else {
            databaseHandler.getAllIngredient(userId, new RecipeDatabaseHandler.GroceryListUpdateListener() {
                @Override
                public void onGroceryListUpdated(ArrayList<String> groceryList) {
                    adapter.updateData(groceryList);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        }

        goToMap = binding.goToMap;
        goToMap.setOnClickListener(view -> openNavigationApp());

        sendSMS = binding.sendMessage;
        sendSMS.setOnClickListener(view -> showPhoneNumberDialog());

        return root;
    }

    private void openNavigationApp() {
        Uri gmmIntentUri = Uri.parse("geo:16.816514, 96.167949?q=Grocery Stores Near Me");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(getContext(), "Google Maps not installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPhoneNumberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter Phone Number");

        final EditText input = new EditText(getContext());
        input.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
        builder.setView(input);

        builder.setPositiveButton("Send", (dialog, which) -> {
            phoneNumberToSend = input.getText().toString().trim();
            if (phoneNumberToSend.isEmpty()) {
                Toast.makeText(getContext(), "Phone number cannot be empty!", Toast.LENGTH_SHORT).show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)
                            == PackageManager.PERMISSION_GRANTED) {
                        sendSms(phoneNumberToSend);
                    } else {
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                    }
                } else {
                    sendSms(phoneNumberToSend);
                }
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void sendSms(String phoneNumber) {
        Set<String> selectedItems = adapter.getSelectedItems();
        if (selectedItems.isEmpty()) {
            Toast.makeText(getContext(), "No items selected!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert selected items into a comma-separated list
        StringBuilder message = new StringBuilder("Grocery List: ");
        for (String item : selectedItems) {
            message.append("\n- ").append(item);
        }

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message.toString(), null, null);
            Toast.makeText(getContext(), "SMS sent successfully!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "SMS failed to send!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSms(phoneNumberToSend);  // FIX: Pass the stored phone number
            } else {
                Toast.makeText(requireContext(), "Permission denied to send SMS.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
