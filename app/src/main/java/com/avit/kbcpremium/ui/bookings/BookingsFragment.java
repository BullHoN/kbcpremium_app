package com.avit.kbcpremium.ui.bookings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.avit.kbcpremium.R;
import com.avit.kbcpremium.ui.booking.BookingDetailsFragment;
import com.avit.kbcpremium.ui.home.BookingItem;

import java.util.ArrayList;

public class BookingsFragment extends Fragment {

    private LinearLayout itemsView;
    private ViewGroup viewGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookings,container,false);
        viewGroup = container;

        itemsView = root.findViewById(R.id.items);

        ArrayList<BookingItem> bookingItems = getAllBookingItems();
        addItemsToView(bookingItems);

        return root;
    }

    private void addItemsToView(ArrayList<BookingItem> bookingItems){
        for(int i=0;i<bookingItems.size()-1;i+=2){

            LinearLayout nwView = new LinearLayout(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(24, 0, 24, 22);
            nwView.setLayoutParams(lp);

            for(int j=i;j<=i+1;j++) {
                BookingItem first = bookingItems.get(j);

                View bigItemView = getLayoutInflater().inflate(R.layout.big_booking_item, viewGroup, false);
                ImageView imageView = bigItemView.findViewById(R.id.booking_icon);
                imageView.setImageResource(first.getResourceId());

                TextView textView = bigItemView.findViewById(R.id.booking_heading);
                textView.setText(first.getTitle());

                final Bundle bundle = new Bundle();
                bundle.putString("name",bookingItems.get(j).getTitle());
                bundle.putStringArray("items",bookingItems.get(j).getItems());

                bigItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment bookingDetailsFragment = new BookingDetailsFragment();
                        bookingDetailsFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment
                                        ,bookingDetailsFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });

                nwView.addView(bigItemView);
            }

            itemsView.addView(nwView);
        }
    }

    ArrayList<BookingItem> getAllBookingItems(){
        ArrayList<BookingItem> bookingItems = new ArrayList<>();

        String hairTreatment[] = {"Hair Treatment","Spa"};
        String BleachCleanUp[] = {"Bleach","CleanUp"};
        String colorHighlight[] = {"Color","Highlighting"};
        String hairRebounding[] = {"Hair Rebounding","Styling"};
        String bridalMakeUp[] = {"MakeUp"};
        String kbcSpecial[] = {"KBC Special"};
        String Mehandi[] = {"Mehandi"};
        String thread[] = {"Threading","Waxing"};
        String nailCare[] = {"Skin"};

        bookingItems.add(new BookingItem("Hair \nTreatment", R.drawable.ic_hair_salon,hairTreatment));
        bookingItems.add(new BookingItem("Bleach & Cleanup",R.drawable.ic_makeup,BleachCleanUp));
        bookingItems.add(new BookingItem("Color & Highlight",R.drawable.ic_hair,colorHighlight));
        bookingItems.add(new BookingItem("Mehandi & Touchups",R.drawable.ic_henna,Mehandi));

        bookingItems.add(new BookingItem("Hair\nRebounding", R.drawable.ic_hair_cut,hairRebounding));
        bookingItems.add(new BookingItem("KBC\nSpecial",R.drawable.ic_magic_wand,kbcSpecial));
        bookingItems.add(new BookingItem("Make-Up\n(Bridal,Party)",R.drawable.ic_wedding,bridalMakeUp));
        bookingItems.add(new BookingItem("Thread & Waxing",R.drawable.ic_wax,thread));
        bookingItems.add(new BookingItem("Skin & Nail Care",R.drawable.ic_fashion,nailCare));

        return bookingItems;
    }

}
