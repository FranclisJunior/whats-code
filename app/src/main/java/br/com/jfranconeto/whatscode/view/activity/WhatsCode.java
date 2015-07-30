package br.com.jfranconeto.whatscode.view.activity;

import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import br.com.jfranconeto.whatscode.R;
import br.com.jfranconeto.whatscode.view.fragment.MyDataFragment;
import br.com.jfranconeto.whatscode.view.fragment.PreScanFragment;

/**
 * Created by José Franco on 25/05/2015.
 */
public class WhatsCode extends ActionBarActivity implements Drawer.OnDrawerItemClickListener{

    private Toolbar toolbar;
    private Drawer.Result navagationDrawerLeft;

    private int lastMenu = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_code);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        navagationDrawerLeft = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDisplayBelowToolbar(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.scan_qrcode).withIcon(getResources().getDrawable(R.drawable.ic_library_plus)),
                        new PrimaryDrawerItem().withName(R.string.my_data_item).withIcon(getResources().getDrawable(R.drawable.ic_database)),
                        new PrimaryDrawerItem().withName(R.string.generate_qrcode).withIcon(getResources().getDrawable(R.drawable.ic_library_plus)),
                        new PrimaryDrawerItem().withName(R.string.share_qrcode).withIcon(getResources().getDrawable(R.drawable.ic_share))
                )
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.LEFT)
                .withSavedInstance(savedInstanceState)
                .build();

        navagationDrawerLeft.setOnDrawerItemClickListener(this);
        selectedOption(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_whats_code, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l, IDrawerItem iDrawerItem) {
        selectedOption(position);
    }

    private void selectedOption(int position) {
        if (lastMenu == position) return;
        lastMenu = position;
        switch (position) {
            case 0:
                changerFragment(new PreScanFragment());
                break;
            case 1:
                changerFragment(new MyDataFragment());
                break;
            default:
                break;
        }
    }

    private void changerFragment(Fragment fragment){
        getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("pilha").commit();
    }
}
