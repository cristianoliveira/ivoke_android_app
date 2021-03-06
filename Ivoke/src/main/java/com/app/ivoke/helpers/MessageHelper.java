package com.app.ivoke.helpers;

import com.app.ivoke.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.widget.Toast;

public class MessageHelper {

    public static int DIALOG_RESULT_YES    = DialogInterface.BUTTON_POSITIVE;
    public static int DIALOG_RESULT_NO     = DialogInterface.BUTTON_NEGATIVE;
    public static int DIALOG_RESULT_CANCEL = DialogInterface.BUTTON_NEUTRAL;

    public static MessageAlert errorAlert(Context pContext)
    {
        MessageAlert alert = new MessageAlert(pContext);
        alert.getAlertDialog().setIcon(android.R.drawable.ic_dialog_alert);

        return alert;
    }

    public static MessageAlert getErrorAlert(Context pContext,String pTitulo, String pMensagem)
    {
        MessageAlert alert = errorAlert(pContext);
        alert.getAlertDialog().setIcon(android.R.drawable.ic_dialog_alert);

        return alert;
    }

    public static MessageAlert infoAlert(Context pContext)
    {
        MessageAlert alert = new MessageAlert(pContext);
        alert.getAlertDialog().setIcon(android.R.drawable.ic_dialog_info);

        return alert;
    }

    public static MessageAlert infoAlert(Context pContext, String pTitulo, String pMensagem)
    {
        MessageAlert alert = infoAlert(pContext);
        alert.getAlertDialog().setTitle(pTitulo);
        alert.getAlertDialog().setMessage(pMensagem);

        return alert;
    }

    public static AlertDialog warningAlert(Context pContext)
    {
        AlertDialog alert = new AlertDialog.Builder(pContext).create();
        alert.setIcon(android.R.drawable.ic_dialog_alert);

        return alert;
    }

    public static void showAlertWhitOkButton(Context pContext, int resId, DialogInterface.OnClickListener pListener)
    {
        MessageAlert alert = infoAlert(pContext);
        alert.setMessage(resId);
        alert.getAlertDialog()
             .setButton( pContext.getString(R.string.def_btn_ok)
                       , pListener);

        alert.showDialog();

    }

    public static void askYesNoAlert(Context pContext, int resId, DialogInterface.OnClickListener pListener)
    {
        MessageAlert alert = infoAlert(pContext);
        alert.setMessage(resId);
        alert.getAlertDialog()
             .setButton( pContext.getString(R.string.def_btn_yes)
                       , pListener);

        alert.getAlertDialog()
             .setButton2( pContext.getString(R.string.def_btn_no)
                        , pListener);

        alert.showDialog();

    }

    public static void askYesNoAlert(Context pContext, String pMessage, DialogInterface.OnClickListener pListener)
    {
        MessageAlert alert = infoAlert(pContext);
        alert.setMessage(pMessage);
        alert.getAlertDialog()
             .setButton( pContext.getString(R.string.def_btn_yes)
                       , pListener);

        alert.getAlertDialog()
             .setButton2( pContext.getString(R.string.def_btn_no)
                        , pListener);

        alert.showDialog();

    }

    public static void askYesNoCancelAlert(Context pContext, int resID, DialogInterface.OnClickListener pListener)
    {
        MessageAlert alert = infoAlert(pContext);
        alert.setMessage(resID);
        alert.getAlertDialog()
          .setButton( pContext.getString(R.string.def_btn_yes)
                    , pListener);

        alert.getAlertDialog()
         .setButton2( pContext.getString(R.string.def_btn_no)
                    , pListener);

        alert.getAlertDialog()
         .setButton3( pContext.getString(R.string.def_btn_cancel)
                    , pListener);

        alert.showDialog();
    }

    public static AlertDialog.Builder getDialogWhitChoices(Activity pActivity, CharSequence[] pChoices, DialogInterface.OnClickListener pClickListener)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(pActivity);
        alert.setSingleChoiceItems(pChoices, -1, pClickListener);
        //alert.setPositiveButton("OK", pClickListener);
        return alert;
    }

    public static ProgressDialog ProgressAlert(Context pContext, int resId)
    {
        ProgressDialog dialog = ProgressDialog.show( pContext
                                                   , pContext.getString(R.string.def_title_progress_dialog)
                                                   , pContext.getString(resId)
                                                   , true);
        return dialog;
    }

    @SuppressLint("ShowToast")
    public static Toast toastMessage(Context pContext, int pStringResourceID)
    {
        return Toast.makeText(pContext, pContext.getResources().getString(pStringResourceID), 5000);
    }

    @SuppressLint("ShowToast")
    public static Toast toastMessage(Context pContext, int pStringResourceID, Object args)
    {
        String resString = pContext.getResources().getString(pStringResourceID);

        if(resString.indexOf("%")>0)
        {
            return Toast.makeText(pContext, String.format(resString, args) , 5000);
        }

        return Toast.makeText(pContext, resString , 5000);
    }

    @SuppressLint("ShowToast")
    public static Toast getToastMessage(Context pContext, String pMessage)
    {
        return Toast.makeText(pContext, pMessage, 5000);
    }

    /* Message BUILDER */
    public static class MessageAlert
    {
        private AlertDialog alertDialog;
        private Context context;
        private static Resources resource;
        private static String message;

        private int result;

           protected MessageAlert(Context pContext) {
            setAlertDialog(new AlertDialog.Builder(pContext).create());
            context = pContext;
            resource = pContext.getResources();
        }

        public void showDialog()
        {
            getAlertDialog().show();
        }

        public MessageAlert setTitle(String pTitle)
        {
            getAlertDialog().setTitle(pTitle);
            return this;
        }

        public MessageAlert setTitle(int pStringResourceID)
        {
            getAlertDialog().setTitle(resource.getString(pStringResourceID));
            return this;
        }

        public MessageAlert setMessage(String pMessage)
        {
            message = pMessage;
            getAlertDialog().setMessage(message);
            return this;
        }

        public MessageAlert setMessage(int pStringResourceID)
        {
            message = resource.getString(pStringResourceID);
            getAlertDialog().setMessage(message);
            return this;
        }

        public MessageAlert setMessage(int pStringResourceID, Object... args)
        {
            message = String.format(resource.getString(pStringResourceID), args);
            getAlertDialog().setMessage(message);
            return this;
        }

        public MessageAlert formatMessage(Object args)
        {
            getAlertDialog().setMessage(String.format(message, args));
            return this;
        }

        private AlertDialog getAlertDialog() {

            if(alertDialog==null)
            {
                alertDialog = new AlertDialog.Builder(context).create();
            }

            return alertDialog;
        }

        private void setAlertDialog(AlertDialog pAlertDialog) {
            alertDialog = pAlertDialog;
        }

        public void setResult(int pResult)
        {
            result = pResult;
        }

        public int getResult()
        {
            return result;
        }

        public MessageAlert setButtonOk(OnClickListener pListener)
        {
            new DebugHelper("MessageHelper").method("setButtonOk").var("context",context);
            alertDialog.setButton(context.getString(R.string.def_btn_ok)
                                 ,pListener);
            return this;
        }

        public MessageAlert setButtonYesNo(OnClickListener pListener)
        {
            alertDialog
             .setButton( context.getString(R.string.def_btn_yes)
                       , pListener);

            alertDialog
            .setButton2( context.getString(R.string.def_btn_no)
                        , pListener);

            return this;
        }

        public Context getContext()
        {
            return context;
        }

        public MessageAlert setDefaultButtonOk() {
            this.alertDialog.setButton("Ok", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            return this;
        }
    }

    public static void unexpectedException(Activity pActivity, Exception e) {
          final Activity act = pActivity;

          errorAlert(pActivity)
          .setMessage(R.string.def_error_msg_unexpected_exception)
          .setButtonOk(new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                act.finish();
            }
           }).showDialog();
          e.printStackTrace();
    }

    public static void showHelp(Activity pActivity, int pResId) {
         infoAlert(pActivity)
         .setMessage(pResId)
         .setDefaultButtonOk().showDialog();
    }
}
