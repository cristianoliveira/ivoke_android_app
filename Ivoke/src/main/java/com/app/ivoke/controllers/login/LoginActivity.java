package com.app.ivoke.controllers.login;

import org.json.JSONObject;

import com.app.ivoke.Common;
import com.app.ivoke.R;
import com.app.ivoke.Router;
import com.app.ivoke.helpers.DeviceHelper;
import com.app.ivoke.helpers.MessageHelper;
import com.app.ivoke.helpers.DebugHelper;
import com.app.ivoke.libraries.GCMManager;
import com.app.ivoke.models.FacebookModel;
import com.app.ivoke.models.UserModel;
import com.app.ivoke.objects.UserIvoke;
import com.app.ivoke.objects.defaults.DefaultBackgroudWorker;
import com.app.ivoke.objects.defaults.DefaultOkListener;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;

public class LoginActivity extends android.support.v4.app.FragmentActivity {

    public static final String PE_FACEBOOK_SESSION = "LoginActivity.FacebookSession";
    public static final String PE_FACEBOOK_USER_JSON = "LoginActivity.FacebookUser";
    static DebugHelper debug = new DebugHelper("LoginActivity");

    private FacebookModel faceModel = new FacebookModel();
    private UserModel userModel = new UserModel();

    private Session   fbSession;
    private GraphUser fbUser;
    private UserIvoke userIvoke;

    private LoginBackground loginBackground;

    MessageHelper.MessageAlert msgError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        debug.method("onCreate").par("savedInstanceState", savedInstanceState);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }

        getExtras();
        doLoginOnIvoke();

    }

    public void showUserNotError()
    {
        MessageHelper.errorAlert(this).setMessage(R.string.login_msg_error_user_not_found).showDialog();
    }

    public static class PlaceholderFragment extends Fragment {
         @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.login_fragment, container, false);
            return rootView;
        }
    }


    private void getExtras()
    {
        debug.method("getExtras");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fbSession = (Session) extras.getSerializable(PE_FACEBOOK_SESSION);

            String jsonFbUser = extras.getString(PE_FACEBOOK_USER_JSON);

             debug.var("jsonFbUser" , jsonFbUser);
            try {
                JSONObject jsonObj = new JSONObject(jsonFbUser);
                fbUser = GraphObject.Factory.create(jsonObj, GraphUser.class);
            } catch (Exception e) {
                debug.exception(e);
                MessageHelper.errorAlert(this)
                             .setMessage(R.string.check_msg_error_get_fb_user)
                             .setButtonOk(new DefaultOkListener(this) {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Router.gotoFacebookLogin(this.getActivity());
                                }
                            }).showDialog();
            }

        }
        debug.var("fbSession",fbSession);

    }

    private void doLoginOnIvoke()
    {
        debug.method("tryToDoLoginOnIvoke");
        loginBackground = new LoginBackground(this);
        loginBackground.execute();

    }

    private void goToChecking() {

        Router.gotoChecking(this, fbSession, userIvoke, fbUser);
        this.finish();

    }


    private class LoginBackground extends DefaultBackgroudWorker
    {
        LoginActivity loginActivity;

        public LoginBackground(Activity pActivity) {
            super(pActivity);
            loginActivity = (LoginActivity) pActivity;
        }

        @Override
        protected Object doInBackground(Object... params) {
            debug.method("doInBackground");

            if(!DeviceHelper.hasInternetConnection())
            {
                setException(new Exception(loginActivity.getString(R.string.def_error_msg_whitout_internet_connection)));
                return false;
            }

            if(fbUser!=null)
            {
                try {
                    userIvoke = userModel.requestIvokeUser(fbUser.getId());

                    if(userIvoke == null)
                    {
                        userIvoke = userModel.createOnServer(fbUser.getName(), fbUser.getId());
                    }

                    String regid = new GCMManager().getRegistrationId(loginActivity);

                    debug.log("regid "+regid);
                    userModel.asyncRegisterDevice(userIvoke, regid);

                } catch (Exception e) {
                    debug.exception(e);
                    setException(new Exception(loginActivity.getString(R.string.def_error_msg_ws_server_not_responding)));
                }
            }
            else
            {
                setException(new Exception(loginActivity.getString(R.string.login_error_msg_facebookuser_not_found)));
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            debug.method("onPostExecute"+result);
            if(inError())
            {
                MessageHelper.errorAlert(loginActivity)
                 .setMessage(getException().getMessage())
                 .setButtonOk(new OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            loginActivity.finish();
                                        }
                                    }).showDialog();
            }
            else if(userIvoke!=null)
            {
                loginActivity.goToChecking();
            }
            else
            {
                MessageHelper.errorAlert(loginActivity)
                             .setMessage(R.string.login_msg_error_user_not_found_ask_try_again)
                             .setButtonYesNo(new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if(which == MessageHelper.DIALOG_RESULT_YES)
                                    {
                                        loginBackground.execute();
                                    }
                                    else
                                    {
                                        finish();
                                    }
                                }
                            }).showDialog();
            }
        }
    }
}