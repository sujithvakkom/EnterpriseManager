package sujithvakkomfreelance.com.enterprisemanager.customviews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sujithvakkomfreelance.com.components.AppLiterals;
import sujithvakkomfreelance.com.components.Util;
import sujithvakkomfreelance.com.components.localdata.dataproviders.DeliveryHeaderProvider;
import sujithvakkomfreelance.com.components.localdata.dataproviders.DeliveryLineProvider;
import sujithvakkomfreelance.com.components.models.homedelivery.DeliveryHeader;
import sujithvakkomfreelance.com.components.web.IWebClient;
import sujithvakkomfreelance.com.enterprisemanager.DeliveryActivity;
import sujithvakkomfreelance.com.enterprisemanager.R;
import sujithvakkomfreelance.com.enterprisemanager.adaptors.DeliveryListAdaptor;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeliveryAcceptedJobFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeliveryAcceptedJobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeliveryAcceptedJobFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private ListView deliveryListView;
    private ArrayList<DeliveryHeader> deliveries;
    private DeliveryListAdaptor adaptor;
    private ProgressBar mProgressView;

    public DeliveryAcceptedJobFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment DeliveryAcceptedJobFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeliveryAcceptedJobFragment newInstance() {
        DeliveryAcceptedJobFragment fragment = new DeliveryAcceptedJobFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delivery_accepted_job, container, false);
        deliveryListView = view.findViewById(R.id.delivery_accepted_job_list);
        deliveryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeliveryHeader delivery = adaptor.getItem(position);
                if (delivery.Status == AppLiterals.DELIVERY_STATUS.READY) {
                    ((DeliveryActivity) getActivity()).loadReceiptJob(delivery.OrderNumber);
                } else {
                    DeliveryLineProvider provider = new DeliveryLineProvider();
                    delivery.DeliveryLines = provider.getList(getContext(), delivery.HeaderId,
                            AppLiterals.DELIVERY_STATUS.DISPATCHED
                    );

                    if (delivery.DeliveryLines != null)
                        if (delivery.DeliveryLines.size() > 0)
                            //if (deliveryStatus == AppLiterals.DELIVERED)
                            //    Toast.makeText(getContext(), "Already delivered.", Toast.LENGTH_LONG).show();
                            //else if (deliveryStatus == AppLiterals.DISPATCHED)
                            ((DeliveryActivity) getActivity()).loadDeliveryActions(delivery);

                }
            }
        });
        BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mProgressView = view.findViewById(R.id.delivery_load_job_progress);
        loadDeliveries(AppLiterals.DELIVERY_STATUS.DISPATCHED);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void loadDeliveries(int Status) {
        showProgress(true);
        switch (Status){
            case AppLiterals.DELIVERY_STATUS.DISPATCHED:
                DeliveryHeaderProvider provider = new DeliveryHeaderProvider();
                deliveries =  provider.getList(getContext(), Util.getToken(getActivity()).user_name,AppLiterals.DELIVERY_STATUS.DISPATCHED);
                Collections.sort(deliveries);
                if(deliveries!=null) {
                    adaptor = new DeliveryListAdaptor(getActivity(), deliveries);
                    adaptor.setListner(null);
                    deliveryListView.setAdapter(adaptor);
                    adaptor.notifyDataSetChanged();
                }
                else {
                    deliveryListView.setAdapter(null);
                }
                showProgress(false);
                break;
            case AppLiterals.DELIVERY_STATUS.READY:
                getScheduledDeliveries();
                break;
        }
    }

    private void getScheduledDeliveries() {
        try {
            String baseUrl = Util.getSettingById(getActivity(),R.string.pref_head_office_key);
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(
                            AppLiterals.APPLICATION_GSON_BUILDER));

            Retrofit retrofit = builder.build();

            IWebClient client = retrofit.create(IWebClient.class);

            Call<List<DeliveryHeader>> call = client.GetDeliveryJobs(
                    Util.getToken(getActivity()).vehicle_code,
                    Integer.toString(AppLiterals.DELIVERY_STATUS.READY));

            call.enqueue(new Callback<List<DeliveryHeader>>() {
                @Override
                public void onResponse(Call<List<DeliveryHeader>> call, Response<List<DeliveryHeader>> response) {

                    List<DeliveryHeader> result = response.body();
                    if(result!=null) {
                        deliveries = new ArrayList<>(result);
                        adaptor = new DeliveryListAdaptor(getActivity(),deliveries);
                        adaptor.setListner(null);
                        deliveryListView.setAdapter(adaptor);
                        adaptor.notifyDataSetChanged();
                        showProgress(false);
                    }
                    else {
                        deliveryListView.setAdapter(null);
                        Snackbar.make(getView(), "No pending deliveries.", Snackbar.LENGTH_LONG)
                                .setAction("Deliveries", null).show();
                        showProgress(false);
                    }
                }

                @Override
                public void onFailure(Call<List<DeliveryHeader>> call, Throwable t) {
                    if(t==null){
                        Snackbar snackbar = Util.SimpleSnackBar(getView(), "Try again later.", Snackbar.LENGTH_LONG);
                        View view = snackbar.getView();
                        view.setBackgroundColor(getResources().getColor(R.color.error_background));
                        ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                                getResources().getColor(R.color.error_foreground)
                        );
                        snackbar.show();
                    }
                    else {
                        Snackbar snackbar = Util.SimpleSnackBar(getView(), t.getMessage(), Snackbar.LENGTH_LONG);
                        View view = snackbar.getView();
                        view.setBackgroundColor(getResources().getColor(R.color.error_background));
                        ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                                getResources().getColor(R.color.error_foreground)
                        );
                        snackbar.show();}
                    showProgress(false);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.navigation_loaded_jobs) {
                loadDeliveries(AppLiterals.DELIVERY_STATUS.DISPATCHED);
                return true;
            } else if (id == R.id.navigation_scheduled_jobs) {
                loadDeliveries(AppLiterals.DELIVERY_STATUS.READY);
                return true;
            }
            return false;
        }
    };

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            deliveryListView.setVisibility(show ? View.GONE : View.VISIBLE);
            deliveryListView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    deliveryListView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            deliveryListView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
