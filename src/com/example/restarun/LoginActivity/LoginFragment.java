package com.example.restarun.LoginActivity;

import java.util.Arrays;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
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

	private View loginView;

	private UiLifecycleHelper uihelper;

	private LoginButton authButton;
	private Button guestButton;
	private ImageView foodImageView;


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
		guestButton = (Button) loginView.findViewById(R.id.guestButton);
		foodImageView = (ImageView) loginView.findViewById(R.id.foodImageView);


		/** To use a LoginButton inside a fragment, we call setFragment on it **/
		authButton.setFragment(this);

		/** Edit the permissions of the Login button to access Likes and Status **/
		authButton.setReadPermissions(Arrays
				.asList("user_likes", "user_status"));
		authButton.setBackgroundResource(R.drawable.buttons);

		guestButton.setTextColor(Color.WHITE);

		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/Roboto-Regular.ttf");
		authButton.setTypeface(font);
		guestButton.setTypeface(font);

		TextView title = (TextView) loginView.findViewById(R.id.title);
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
		
/*		
		ImageView img1 = (ImageView) loginView.findViewById(R.id.logo);

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.app_icon);

		Bitmap output = Bitmap.createBitmap(200,
				200, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, 160, 160);
        final RectF rectF = new RectF(rect);
        final float roundPx = 20;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

		img1.setImageBitmap(output);*/
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