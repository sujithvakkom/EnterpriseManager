package sujithvakkomfreelance.com.enterprisemanager.adaptors.interfaces;

import android.view.View;

public interface DeliveryListOnClickListener {
    void onClick(View v, String customerPhone, String receipt, String driver, int id);
}
