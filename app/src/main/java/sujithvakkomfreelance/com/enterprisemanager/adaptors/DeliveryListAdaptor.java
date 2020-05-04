package sujithvakkomfreelance.com.enterprisemanager.adaptors;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import sujithvakkomfreelance.com.components.AppLiterals;
import sujithvakkomfreelance.com.components.Util;
import sujithvakkomfreelance.com.components.localdata.dataproviders.delivery;
import sujithvakkomfreelance.com.components.models.homedelivery.DeliveryHeader;
import sujithvakkomfreelance.com.enterprisemanager.R;
import sujithvakkomfreelance.com.enterprisemanager.adaptors.interfaces.DeliveryListOnClickListener;

public class DeliveryListAdaptor extends ArrayAdapter<DeliveryHeader> {
    private final Activity activity;
    private final ArrayList<DeliveryHeader> deliveries;

    public DeliveryListAdaptor(Activity activity,
                               ArrayList<DeliveryHeader> deliveries) {
        super(activity, R.layout.delivery_list_item, deliveries);
        this.activity = activity;
        this.deliveries = deliveries;
    }

    private DeliveryListOnClickListener listner;

    public void setListner(@Nullable DeliveryListOnClickListener l) {
        listner = l;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final DeliveryHeader temp = deliveries.get(position);
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.delivery_list_item, null, true);
        AppCompatTextView textViewHeader = (AppCompatTextView) rowView.findViewById(R.id.textHeading);
        AppCompatTextView textViewAddress = (AppCompatTextView) rowView.findViewById(R.id.textCustomerLocation);
        AppCompatTextView textReceipt = (AppCompatTextView) rowView.findViewById(R.id.textBoxReceipt);
        AppCompatTextView textSchedule = (AppCompatTextView) rowView.findViewById(R.id.textSchedule);
        AppCompatTextView textQty = (AppCompatTextView) rowView.findViewById(R.id.textQty);

        textViewHeader.setText(temp.getDescription());
        textViewAddress.setText(temp.getEmirate());
        //if (textQty != null) textQty.setText(String.format("%1$,.0f", temp.getQty()) + " Nos.");
        if (textReceipt != null) textReceipt.setText(temp.getReceipt());

        if (textSchedule != null && temp.getScheduledDate() != null) {
            textSchedule.setText(AppLiterals.APPLICATION_DATE_SMALL_FORMAT.format(temp.getScheduledDate()));
            if (temp.getScheduledDate().compareTo(Util.getCurrentDate()) > 0)
                textSchedule.setTextColor(getContext().getResources().getColor(R.color.success_background));
            else
                textSchedule.setTextColor(getContext().getResources().getColor(R.color.error_background));
        }
        return rowView;
    }
}
