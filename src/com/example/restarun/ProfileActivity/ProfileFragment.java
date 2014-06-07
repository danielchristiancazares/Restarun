package com.example.restarun.ProfileActivity;

import info.androidhive.actionbar.model.SpinnerNavItem;
import info.androidhive.info.actionbar.adapter.TitleNavigationAdapter;

import java.util.ArrayList;

import com.example.restarun.R;
import com.example.restarun.User.User;
import com.facebook.widget.ProfilePictureView;

import android.os.Bundle;
import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileFragment extends Fragment implements
		ActionBar.OnNavigationListener {
	private TextView usernameText, been, favorited, favoritedText;
	private ProfilePictureView profilePictureView;
	private ActionBar actionBar;
	private ArrayList<SpinnerNavItem> navSpinner;
	private TitleNavigationAdapter adapter;
	private User mUser = User.getInstance();
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_profile, container,
				false);
		usernameText = (TextView) view.findViewById(R.id.welcomeText);

		been = (TextView) view.findViewById(R.id.been);
		favorited = (TextView) view.findViewById(R.id.favorited);
		favoritedText = (TextView) view.findViewById(R.id.favoritedText);
		profilePictureView = (ProfilePictureView) view
				.findViewById(R.id.selection_profile_pic);
		profilePictureView.setCropped(true);

		actionBar = getActivity().getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		navSpinner = new ArrayList<SpinnerNavItem>();
		navSpinner.add(new SpinnerNavItem("Profile", R.drawable.user));
		navSpinner.add(new SpinnerNavItem("Favorites", R.drawable.star));
		navSpinner.add(new SpinnerNavItem("Check-ins", R.drawable.check));

		adapter = new TitleNavigationAdapter(getActivity()
				.getApplicationContext(), navSpinner);
		actionBar.setListNavigationCallbacks(adapter, this);

		usernameText.setText(mUser.m_name);
		profilePictureView.setProfileId(mUser.m_photo);
		
		return view;
	}

	private void setInfo() {
		User mUser = User.getInstance();
		usernameText.setText("Hi, " + mUser.m_name);

		been.setText("been to " + mUser.getBeenPlaces().size() + " place(s)");
		favorited.setText("favorited " + mUser.getFavoritedPlaces().size()
				+ " place(s)");

		profilePictureView.setProfileId(mUser.m_photo);
		favoritedText.setText("Favorites: ");
		for (int i = 0; i < mUser.getFavoritedPlaces().size(); ++i) {
			favoritedText.append(mUser.getFavoritedPlaces().get(i).getName());
			favoritedText.append("\n");
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		FragmentTransaction transaction = getActivity()
				.getSupportFragmentManager().beginTransaction();
		switch (itemPosition) {
		case 0:
			break;
		case 1:
			transaction.replace(R.id.container, new CheckInFragment());
			break;

		case 2:
			transaction.replace(R.id.container, new FavoriteFragment());
			break;

		}
		transaction.commit();
		return false;
	}
}
