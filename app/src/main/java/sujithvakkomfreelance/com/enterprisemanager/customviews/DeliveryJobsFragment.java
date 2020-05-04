package sujithvakkomfreelance.com.enterprisemanager.customviews;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sujithvakkomfreelance.com.enterprisemanager.R;
import sujithvakkomfreelance.com.enterprisemanager.adaptors.DeliveryPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeliveryJobsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeliveryJobsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeliveryJobsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public DeliveryJobsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DeliveryJobsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeliveryJobsFragment newInstance() {
        DeliveryJobsFragment fragment = new DeliveryJobsFragment();
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
        View view = inflater.inflate(R.layout.fragment_delivery_jobs, container, false);


        ViewPager mViewPager = view.findViewById(R.id.delivery_job_viewpager);
        DeliveryPagerAdapter mAdaptor = new DeliveryPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdaptor);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
