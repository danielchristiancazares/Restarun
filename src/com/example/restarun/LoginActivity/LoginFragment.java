package com.example.restarun.LoginActivity;

import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.restarun.R;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

public class LoginFragment extends Fragment {

	private View loginView;

	private UiLifecycleHelper uihelper;

	private LoginButton authButton;

	private Session.StatusCallback callback = new Session.StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStatechange(session, state, exception);
		}
	};

	/**
	 * @author danielcazares
	 * @function_name: onCreateView();
	 * @description: onCreateView() is a superclass function override that is
	 *               called upon instantiation of the view. Additionally, it
	 *               sets the current view and gives access to layout objects
	 *               defined within the fragment's layout.
	 **/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		/** Sets the current view **/
		loginView = inflater.inflate(R.layout.fragment_login, container, false);

		/** Find the Facebook login button to edit functionality and permissions **/
		authButton = (LoginButton) loginView.findViewById(R.id.authButton);

		/** To use a LoginButton inside a fragment, we call setFragment on it **/
		authButton.setFragment(this);

		/** Edit the permissions of the Login button to access Likes and Status **/
		authButton.setReadPermissions(Arrays
				.asList("user_likes", "user_status"));

		return loginView;
	}

	/**
	 * @author danielcazares
	 * @function_name: onSessionStatechange();
	 * @description: onSessionStatechange() provides access to perform extra
	 *               functionality upon Facebook session state changes, such as
	 *               logging in or out.
	 **/
	private void onSessionStatechange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			Log.i("SESSION STATE CHANGE", "LOGGED IN.");
		} else {
			Log.i("SESSION STATE CHANGE", "LOGGED OUT.");
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