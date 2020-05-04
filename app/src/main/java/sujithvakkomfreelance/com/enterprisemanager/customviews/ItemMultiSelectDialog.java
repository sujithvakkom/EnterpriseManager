package sujithvakkomfreelance.com.enterprisemanager.customviews;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import sujithvakkomfreelance.com.components.AppLiterals;
import sujithvakkomfreelance.com.components.models.homedelivery.DeliveryHeader;
import sujithvakkomfreelance.com.components.models.homedelivery.DeliveryLine;
import sujithvakkomfreelance.com.enterprisemanager.R;

public class ItemMultiSelectDialog extends AppCompatDialogFragment {
    private DeliveryHeader delivery;
    private OnItemSelectAcceptListner onItemSelectAcceptListner;

    public void setDelivery(DeliveryHeader delivery) {
        this.delivery = delivery;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null)
            this.delivery = (DeliveryHeader) savedInstanceState.getSerializable(AppLiterals.INSTANCE_STATE_KEYS.DELIVERY_DETAILS);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                R.style.CustomDialog
                );
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.item_multi_select_dialog, null);
        LayoutInflater inflaterCheckBox = getActivity().getLayoutInflater();

        AppCompatCheckBox temp;
        LinearLayoutCompat layout = view.findViewById(R.id.parent_item_select);
        int i=0;
        for(DeliveryLine line : delivery.DeliveryLines){
            temp =
                    (AppCompatCheckBox)inflater.inflate(R.layout.checkbox, null);
            temp.setText(line.Description);
            temp.setEnabled(line.Status == AppLiterals.DELIVERY_STATUS.DISPATCHED);
            temp.setEllipsize(TextUtils.TruncateAt.END);
            temp.setTag(i);
            temp.setId(line.LineId);
            temp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ItemMultiSelectDialog.this.itemCheked((int)buttonView.getTag(),isChecked);
                    setProgressLabel();
                }
            });
            layout.addView(temp);
            i++;
        }
        AppCompatButton cancelButton =  view.findViewById(R.id.cancel_action);
        AppCompatButton acceptButton =  view.findViewById(R.id.accept_action);
        AppCompatButton selectAllButton =  view.findViewById(R.id.select_all_action);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemSelectAcceptListner!=null)
                    onItemSelectAcceptListner.onItemSelectAccpted(ItemMultiSelectDialog.this.delivery);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        selectAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((AppCompatButton)v).getText()==getString(R.string.select_all)) {
                    LinearLayoutCompat layout =getDialog().findViewById(R.id.parent_item_select);
                    for (DeliveryLine line : ItemMultiSelectDialog.this.delivery.DeliveryLines) {
                        ((AppCompatCheckBox) layout.findViewById(line.LineId)).setChecked(true);
                        line.IsChecked = true;
                        ((AppCompatButton) v).setText(R.string.deselect_all);
                    }
                }else  if (((AppCompatButton)v).getText()==getString(R.string.deselect_all)) {
                    LinearLayoutCompat layout = getDialog().findViewById(R.id.parent_item_select);
                    for (DeliveryLine line : ItemMultiSelectDialog.this.delivery.DeliveryLines) {
                        ((AppCompatCheckBox) layout.findViewById(line.LineId)).setChecked(false);
                        line.IsChecked = false;
                        ((AppCompatButton) v).setText(R.string.select_all);
                    }
                }
                setProgressLabel();
            }
        });
        builder.setView(view);
        builder.setTitle(R.string.select_items);
        return builder.create();

    }

    @Override
    public void onStart() {
        super.onStart();

        setProgressLabel();
    }

    private void setProgressLabel() {
        ContentLoadingProgressBar progressBar = getDialog().findViewById(R.id.progress_bar_select);

        int countTotal = 0, coutSel = 0;
        for (DeliveryLine line : this.delivery.DeliveryLines) {
            countTotal++;
            if (line.IsChecked) coutSel++;
        }
        progressBar.setMax(countTotal);
        progressBar.setProgress(coutSel);
        getDialog().setTitle(getString(R.string.select_items) +": "+Integer.valueOf(coutSel) + " / " + Integer.valueOf(countTotal));
    }

    private void itemCheked(int tag, boolean isChecked) {
        this.delivery.DeliveryLines.get(tag).IsChecked = isChecked;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(AppLiterals.INSTANCE_STATE_KEYS.DELIVERY_DETAILS, this.delivery);
        super.onSaveInstanceState(outState);
    }

    public void setOnItemSelectAcceptListner(ItemMultiSelectDialog.OnItemSelectAcceptListner onItemSelectAccept) {
        this.onItemSelectAcceptListner = onItemSelectAccept;
    }

    public interface OnItemSelectAcceptListner{
        public void onItemSelectAccpted(DeliveryHeader delivery);
    }
}