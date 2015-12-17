package me.aupifb.perfeggtion;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

/**
 * Created by aupifb on 17/12/2015.
 */
public class LicensesDialogFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_licenses, null);


        WebView webView = (WebView) v.findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/open_source_licenses.html");

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("open source")
                .create();
    }
}
