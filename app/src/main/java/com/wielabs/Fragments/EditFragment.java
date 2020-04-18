package com.wielabs.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wielabs.R;

import java.util.ArrayList;

public class EditFragment extends Fragment {

    Dialog dialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_layout, container, false);
        dialog = new Dialog(view.getContext());
        RecyclerView editRecycler = view.findViewById(R.id.editRecycler);
        RecyclerView editPrivate = view.findViewById(R.id.editRecycler1);
        ArrayList<Item> items = new ArrayList<>();
        ArrayList<Item> items1 = new ArrayList<>();

        items.add(new Item("Name", "Rohan"));
        items.add(new Item("Referral code", "rohan3241"));
        items.add(new Item("Manage Address", ""));
        items.add(new Item("Change password", ""));

        items1.add(new Item("Gender", "Male"));
        items1.add(new Item("Date of birth", "01-01-1998"));

        items1.add(new Item("Mobile", "+91-8309096401"));
        items1.add(new Item("E-mail", "rohanpandey2210@gmail.com"));

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divideredit));
        editRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        editRecycler.addItemDecoration(itemDecorator);

        editPrivate.setLayoutManager(new LinearLayoutManager(view.getContext()));
        editPrivate.addItemDecoration(itemDecorator);

        EditAdapter e = new EditAdapter(view.getContext(), items);
        EditAdapter e1 = new EditAdapter(view.getContext(), items1);
        editPrivate.setAdapter(e1);
        editRecycler.setAdapter(e);

        return view;
    }

    class EditAdapter extends RecyclerView.Adapter<EditAdapter.BidHistoryViewHolder> {

        Context context;
        ArrayList<Item> heroList;

        EditAdapter(Context context, ArrayList<Item> heroList) {
            this.context = context;
            this.heroList = heroList;
        }

        @NonNull
        @Override
        public EditAdapter.BidHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new EditAdapter.BidHistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_profile_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull BidHistoryViewHolder holder, int position) {
            holder.Title.setText(heroList.get(position).getTitle());
            holder.Value.setText(heroList.get(position).getValue());
        }


        @Override
        public int getItemCount() {
            return heroList.size();
        }

        class BidHistoryViewHolder extends RecyclerView.ViewHolder {

            TextView Title;
            TextView Value;

            BidHistoryViewHolder(View itemView) {
                super(itemView);
                itemView.setBackgroundColor(getResources().getColor(R.color.white));
                Title = itemView.findViewById(R.id.editItemTitle);
                Value = itemView.findViewById(R.id.editItemValue);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (heroList.get(getAdapterPosition()).getTitle()) {

                            case "Change password":
                                dialog.setContentView(R.layout.change_password_dialog);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                dialog.show();
                                break;

                            case "Manage Address":
                                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddressFragment()).commit();
                                break;
                        }
                    }
                });
            }
        }
    }

    class Item {
        String title;
        String value;

        public Item(String title, String value) {
            this.title = title;
            this.value = value;
        }

        public String getTitle() {
            return title;
        }

        public String getValue() {
            return value;
        }
    }

}