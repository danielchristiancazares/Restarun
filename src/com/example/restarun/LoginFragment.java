package com.example.restarun;

import java.util.Arrays;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoginFragment extends Fragment {
	private View login_view;
	private static final String TAG = "LoginFragment";
	private UiLifecycleHelper uihelper;
	private LoginButton authbutton;

	private Session.StatusCallback callback = new Session.StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStatechange(session, state, exception);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		/** Sets the current view **/
		login_view = inflater
				.inflate(R.layout.fragment_login, container, false);

		/** Find the Facebook login button to edit functionality and permissions **/
		authbutton = (LoginButton) login_view.findViewById(R.id.authButton);

		/** To use a LoginButton inside a fragment, we call setFragment on it **/
		authbutton.setFragment(this);

		/** Edit the permissions of the Login button to access Likes and Status **/
		authbutton.setReadPermissions(Arrays
				.asList("user_likes", "user_status"));

		return login_view;
	}

	private void onSessionStatechange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			Log.i(TAG, "LOGGED IN....");
		} else {
			Log.i(TAG, "LOGGED OUT....");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		uihelper = new UiLifecycleHelper(getActivity(), callback);
		uihelper.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		Session session = Session.getActiveSession();
		if ((session != null) && (session.isOpened() || session.isClosed())) {
			onSessionStatechange(session, session.getState(), null);

		}
		uihelper.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		uihelper.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uihelper.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uihelper.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uihelper.onActivityResult(requestCode, resultCode, data);
	}
}