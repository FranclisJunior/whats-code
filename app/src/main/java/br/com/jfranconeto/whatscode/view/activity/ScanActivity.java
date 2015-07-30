package br.com.jfranconeto.whatscode.view.activity;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.Data;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.zxing.Result;
import java.util.ArrayList;

import br.com.jfranconeto.whatscode.R;
import br.com.jfranconeto.whatscode.model.Contact;
import br.com.jfranconeto.whatscode.view.util.Constants;
import br.com.jfranconeto.whatscode.view.util.ShowElements;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import static br.com.jfranconeto.whatscode.view.util.Constants.*;
/**
 * Created by José Franco on 24/05/2015.
 */
public class ScanActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        mScannerView.setAutoFocus(false);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(final Result result) {
        mScannerView.stopCamera();
        if (result != null) {
            try{
                final String [] datas = result.getText().split(";");
                final String checkCode = datas[0];
                final Contact contact = new Contact(datas[1], datas[2]);
                if (checkCode.equals(CHECKCODE)) {
                    new MaterialDialog.Builder(this)
                        .title(R.string.title_dialog_scaner)
                        .content("O número " + contact.getNumber() + " será adicionado a sua lista de contatos como "+contact.getName()+". Deseja adiciona-lo agora?")
                        .positiveText(R.string.confirm_dialog_scaner)
                        .positiveColorRes(R.color.colorPrimary)
                        .negativeText(R.string.cancel_dialog_scaner)
                        .negativeColorRes(R.color.grey_600)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                new ProcessContact().execute(contact);
                            }
                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                mScannerView.startCamera();
                            }
                        }).show();
                }else{
                    ShowElements.showMessageError(this,R.string.invalid_qrcode);
                    mScannerView.startCamera();
                }
            }catch (Exception e){
                ShowElements.showMessageError(this, R.string.invalid_qrcode);
                mScannerView.startCamera();
            }
        }
    }

    public void openWhatsApp(String number){
        Uri uri = Uri.parse("smsto:" + number);
        Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
        intent.setPackage(Constants.WHATSAPPPACKAGE);
        startActivity(intent);
    }
    public void addToContact(Contact contact){
        try {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            int rawContactInsertIndex = ops.size();
            ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                    .withValue(RawContacts.ACCOUNT_TYPE, null)
                    .withValue(RawContacts.ACCOUNT_NAME, null).build());
            ops.add(ContentProviderOperation
                    .newInsert(Data.CONTENT_URI)
                    .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName())
                    .build());
            ops.add(ContentProviderOperation
                    .newInsert(Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getNumber())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); // Type of mobile number

            ContentProviderResult[] res = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        }catch (Exception e) {
            ShowElements.showMessageError(this, R.string.msg_erro);
        }
    }

    class ProcessContact extends AsyncTask<Contact, Void, String> {
        MaterialDialog progress;

        @Override
        protected void onPreExecute() {
            progress = new MaterialDialog.Builder(ScanActivity.this).content(R.string.wait).progress(true, 0).show();
        }

        @Override
        protected String doInBackground(Contact... contact) {
            try {
                addToContact(contact[0]);
                Thread.sleep(2000);
                return contact[0].getNumber();
            } catch (InterruptedException e) {
                return contact[0].getNumber();
            }
        }

        @Override
        protected void onPostExecute(String numberContact) {
            progress.dismiss();
            openWhatsApp(numberContact);
            finish();
        }
    }
}

