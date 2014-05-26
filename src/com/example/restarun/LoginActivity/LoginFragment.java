package com.example.restarun.LoginActivity;

import java.util.Arrays;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.restarun.R;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

public class LoginFragment extends Fragment {

	private static View loginView;

	private static UiLifecycleHelper uihelper;

	private static LoginButton authButton;
	private static Button guestButton;
	private static ImageView foodImageView;

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
		loginView = inflater.inflate(R.layout.fragment_login, container, false);

		// Store references to the buttons
		authButton = (LoginButton) loginView.findViewById(R.id.authButton);
		guestButton = (Button) loginView.findViewById(R.id.guestButton);
		foodImageView = (ImageView) loginView.findViewById(R.id.foodImageView);
        TextView title = (TextView) loginView.findViewById(R.id.title);


		// Allow the Facebook button to be embeded within a fragment
		authButton.setFragment(this);

		// Edit the permissions of the Login button to access Likes and Status
		authButton.setReadPermissions(Arrays
				.asList("user_likes", "user_status"));
		
		// Workaround to change the look of the Facebook button
		authButton.setBackgroundResource(R.drawable.buttons);

		guestButton.setTextColor(Color.WHITE);

		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/Roboto-Regular.ttf");
		authButton.setTypeface(font);
		guestButton.setTypeface(font);

		title.setTextColor(Color.WHITE);
		Typeface titleFont = Typeface.createFromAsset(
				getActivity().getAssets(), "fonts/Roboto-Light.ttf");
		title.setTypeface(titleFont);

		Drawable guestIcon = getResources().getDrawable(R.drawable.guest);
		guestIcon.setColorFilter(new LightingColorFilter(Color.WHITE,
				Color.WHITE));
		guestIcon.setBounds(0, 0, 40, 40);
		guestButton.setCompoundDrawables(guestIcon, null, null, null);

		Drawable foodIcon = getResources().getDrawable(R.drawable.food);
		foodIcon.setColorFilter(new LightingColorFilter(Color.WHITE,
				Color.WHITE));
		foodIcon.setBounds(0, 0, 40, 40);
		foodImageView.setImageDrawable(foodIcon);
		
		EditText username = (EditText) loginView.findViewById(R.id.username);
		EditText password = (EditText) loginView.findViewById(R.id.password);
		
		username.setTypeface(font);
		password.setTypeface(font);
		password.setTransformationMethod(new PasswordTransformationMethod());
		
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