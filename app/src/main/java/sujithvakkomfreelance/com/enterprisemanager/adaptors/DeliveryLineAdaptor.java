package sujithvakkomfreelance.com.enterprisemanager.adaptors;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import sujithvakkomfreelance.com.components.AppLiterals;
import sujithvakkomfreelance.com.components.Util;
import sujithvakkomfreelance.com.components.models.homedelivery.DeliveryHeader;
import sujithvakkomfreelance.com.components.models.homedelivery.DeliveryLine;
import sujithvakkomfreelance.com.enterprisemanager.R;

public class DeliveryLineAdaptor extends ArrayAdapter<DeliveryLine> {
    private final Activity activity;
    private final List<DeliveryLine> deliveries;


    public DeliveryLineAdaptor(@NonNull Activity activity,
                               int resource,
                               @NonNull List<DeliveryLine> objects) {
        super(activity, resource, objects);

        this.activity = activity;
        this.deliveries = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final DeliveryLine temp = deliveries.get(position);
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.delivery_list_item, null, true);

        AppCompatTextView textViewHeader = (AppCompatTextView) rowView.findViewById(R.id.textHeading);
        AppCompatTextView textViewAddress = (AppCompatTextView) rowView.findViewById(R.id.textCustomerLocation);
        AppCompatTextView textReceipt = (AppCompatTextView) rowView.findViewById(R.id.textBoxReceipt);
        AppCompatTextView textSchedule = (AppCompatTextView) rowView.findViewById(R.id.textSchedule);
        AppCompatTextView textQty = (AppCompatTextView) rowView.findViewById(R.id.textQty);

        textViewHeader.setText(temp.Description);
        textViewAddress.setText(temp.Emirate);
        if (textQty != null) textQty.setText(String.format("%1$,.0f", temp.OrderQuantity) + " Nos.");
        if (textReceipt != null) textReceipt.setText(temp.ItemCode);

        if (textSchedule != null && temp.ScheduledDate != null) {
            textSchedule.setText(AppLiterals.APPLICATION_DATE_SMALL_FORMAT.format(temp.ScheduledDate));
            if (temp.ScheduledDate.compareTo(Util.getCurrentDate()) > 0)
                textSchedule.setTextColor(getContext().getResources().getColor(R.color.success_background));
            else
                textSchedule.setTextColor(getContext().getResources().getColor(R.color.error_background));
        }
        return rowView;
        //return super.getView(position, convertView, parent);
    }
}
