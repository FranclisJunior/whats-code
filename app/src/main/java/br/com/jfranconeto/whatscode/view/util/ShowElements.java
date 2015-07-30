package br.com.jfranconeto.whatscode.view.util;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.github.mrengineer13.snackbar.SnackBar;

import br.com.jfranconeto.whatscode.R;

/**
 * Created by junio_000 on 27/05/2015.
 */
public class ShowElements {

    public static void showMessage(Activity activity,int msgID){
        new SnackBar.Builder(activity)
                .withMessage(activity.getResources().getString(msgID))
                .withDuration(SnackBar.LONG_SNACK)
                .withTextColorId(R.color.colorPrimary)
                .withActionMessage(activity.getResources().getString(R.string.information))
                .show();
    }

    public static void showMessageError(Activity activity, int msgID){
        new SnackBar.Builder(activity)
                .withMessage(activity.getResources().getString(msgID))
                .withDuration(SnackBar.SHORT_SNACK)
                .withTextColorId(R.color.red_500)
                .withActionMessage(activity.getResources().getString(R.string.error))
                .show();
    }

}
