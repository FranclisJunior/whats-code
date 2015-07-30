package br.com.jfranconeto.whatscode.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import br.com.jfranconeto.whatscode.R;
import br.com.jfranconeto.whatscode.view.activity.ScanActivity;

/**
 * Created by junio_000 on 25/05/2015.
 */
public class MyDataFragment extends Fragment implements View.OnClickListener {
    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.my_data_item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_my_data, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setType(FloatingActionButton.TYPE_NORMAL);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
