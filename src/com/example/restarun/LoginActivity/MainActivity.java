package com.example.restarun.LoginActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.example.restarun.R;
import com.example.restarun.SearchActivity.SearchActivity;
import com.example.restarun.User.User;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

public class MainActivity extends FragmentActivity {

	public User mUser = User.getInstance();
	
	private static final int SPLASH = 0;
	private static final int SELECTION = 1;
	private Fragment loginFragment;
	private boolean isResumed = false;
	
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = 
	    new Session.StatusCallback() {
	    @Override
	    public void call(Session session, 
	            SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};

	public void guestLogin(View view) {
		/*FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.hide(loginFragment);
		transaction.commit();*/
		mUser.m_name = "Guest";
		mUser.m_photo = "";
		startSearch();
	}
	
	private void startSearch() {
		Intent intent = new Intent(this, SearchActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.activity_main);
	    
	    FragmentManager fm = getSupportFragmentManager();
	    loginFragment = fm.findFragmentById(R.id.loginFragment);
	    FragmentTransaction transaction = fm.beginTransaction();
	    transaction.hide(loginFragment);
	    transaction.commit();
	}
	
	private void showFragment(int fragmentIndex) {
	    FragmentManager fm = getSupportFragmentManager();
	    FragmentTransaction transaction = fm.beginTransaction();
	    if(fragmentIndex == SPLASH) {
	    	transaction.show(loginFragment);
	    	transaction.commit();
	    }
	    else {
	    	startSearch();
	    }
	}
	
	private void makeMeRequest(final Session session) {
	    // Make an API call to get user data and define a 
	    // new callback to handle the response.
	    Request request = Request.newMeRequest(session, 
	            new Request.GraphUserCallback() {
	    	
	    	@Override
	        public void onCompleted(GraphUser user, Response response) {
	            // If the response is successful
	            if (session == Session.getActiveSession()) {
	                if (user != null) {
	                    // Set the id for the ProfilePictureView
	                    // view that in turn displays the profile picture.
	                    mUser.m_photo = user.getId();
	                    // Set the Textview's text to the user's name.
	                    mUser.m_name = user.getName();
	                }
	            }
	            if (response.getError() != null) {
	                // Handle errors, will do so later.
	            }
	        }
	    });
	    request.executeAsync();
	} 

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    // Only make changes if the activity is visible
	    if (isResumed) {
	        if (state.isOpened()) {
	            // If the session state is open:
	            // Show the authenticated fragment
	        	makeMeRequest(session);
		    	Log.d("DEBUG", "onSessionStateChangeOpen");
	            showFragment(SELECTION);
	        } else if (state.isClosed()) {
	            // If the session state is closed:
	            // Show the login fragment
		    	Log.d("DEBUG", "onSessionStateChangeClosed");
	            showFragment(SPLASH);
	        }
	    }
	}
	
	@Override
	protected void onResumeFragments() {
	    super.onResumeFragments();
	    Session session = Session.getActiveSession();

	    if (session != null && session.isOpened()) {
	        // if the session is already open,
	        // try to show the selection fragment
	    	makeMeRequest(session);
	    	Log.d("DEBUG", "onResumeFragmentsOpen");
	        showFragment(SELECTION);
	    } else {
	        // otherwise present the splash screen
	        // and ask the person to login.
	    	Log.d("DEBUG", "onResumeFragmentsClosed");
	        showFragment(SPLASH);
	    }
	}

	
	@Override
	public void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	    isResumed = true;
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	    isResumed = false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
}