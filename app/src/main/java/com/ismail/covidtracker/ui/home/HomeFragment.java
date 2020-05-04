package com.ismail.covidtracker.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ismail.covidtracker.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private TextView tvTotalConfirmed, tvTotalDeaths, tvTotalRecovered, tvLastUpdated,tvTodayCases,tvTotalActive,tvTotalCritical,tvTodayDeaths,tvTotalTests;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // call view
        tvTotalConfirmed = root.findViewById(R.id.tvTotalConfirmed);
        tvTotalDeaths = root.findViewById(R.id.tvTotalDeaths);
        tvTotalRecovered = root.findViewById(R.id.tvTotalRecovered);
        tvLastUpdated = root.findViewById(R.id.tvLastUpdated);
        tvTodayCases= root.findViewById(R.id.tvTodayCases);
        tvTotalActive= root.findViewById(R.id.tvTotalActive);
        tvTotalCritical= root.findViewById(R.id.tvTotalCritical);
        tvTodayDeaths= root.findViewById(R.id.tvTodayDeaths);
        tvTotalTests= root.findViewById(R.id.tvTotalTests);
        progressBar = root.findViewById(R.id.progress_circular_home);



        // call Volley
        getData();

        return root;
    }

    private String getDate(long milliSecond){
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss aaa");

        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(milliSecond);
        return formatter.format(calendar.getTime());
    }

    private void getData() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = getString(R.string.BackendAPI_Overall);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    tvTotalConfirmed.setText(jsonObject.getString("cases"));
                    tvTotalDeaths.setText(jsonObject.getString("deaths"));
                    tvTotalRecovered.setText(jsonObject.getString("recovered"));
                    tvTodayCases.setText(jsonObject.getString("todayCases"));
                    tvTotalActive.setText(jsonObject.getString("active"));
                    tvTotalCritical.setText(jsonObject.getString("critical"));
                    tvTodayDeaths.setText(jsonObject.getString("todayDeaths"));
                    tvTotalTests.setText(jsonObject.getString("tests"));
                    tvLastUpdated.setText("Last Updated"+"\n"+getDate(jsonObject.getLong("updated")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.d("Error Response", error.toString());
            }
        });

        queue.add(stringRequest);
    }
}