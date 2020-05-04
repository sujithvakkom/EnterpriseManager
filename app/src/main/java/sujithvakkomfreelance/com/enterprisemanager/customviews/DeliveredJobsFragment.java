package sujithvakkomfreelance.com.enterprisemanager.customviews;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sujithvakkomfreelance.com.components.AppLiterals;
import sujithvakkomfreelance.com.components.Util;
import sujithvakkomfreelance.com.components.models.homedelivery.DeliveryHeader;
import sujithvakkomfreelance.com.components.web.IWebClient;
import sujithvakkomfreelance.com.enterprisemanager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DeliveredJobsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private View view;
    private ArrayList<DeliveryHeader> deliveries;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DeliveredJobsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DeliveredJobsFragment newInstance(int columnCount) {
        DeliveredJobsFragment fragment = new DeliveredJobsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_deliveryheader_list, container, false);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getDeliveredDeliveries();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DeliveryHeader item);
    }

    private void getDeliveredDeliveries() {
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
                    Integer.toString(AppLiterals.DELIVERY_STATUS.DELIVERED));

            call.enqueue(new Callback<List<DeliveryHeader>>() {
                @Override
                public void onResponse(Call<List<DeliveryHeader>> call, Response<List<DeliveryHeader>> response) {

                    List<DeliveryHeader> result = response.body();
                    if(result!=null) {
                        deliveries = new ArrayList<>(result);
                        // Set the adapter
                        if (view instanceof RecyclerView) {
                            Context context = view.getContext();
                            RecyclerView recyclerView = (RecyclerView) view;
                            if (mColumnCount <= 1) {
                                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            } else {
                                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                            }
                            MyDeliveryHeaderRecyclerViewAdapter adaptor = new MyDeliveryHeaderRecyclerViewAdapter(deliveries

                                    , null);
                            recyclerView.setAdapter(adaptor);
                            adaptor.notifyDataSetChanged();
                        }
                        //showProgress(false);
                    }
                    else {
                        // Set the adapter
                        if (view instanceof RecyclerView) {
                            Context context = view.getContext();
                            RecyclerView recyclerView = (RecyclerView) view;
                            if (mColumnCount <= 1) {
                                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            } else {
                                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                            }
                            recyclerView.setAdapter(null);
                        }
                        //showProgress(false);
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
                    //showProgress(false);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
