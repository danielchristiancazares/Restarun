package com.example.restarun.SearchActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.restarun.R;
import com.example.restarun.User.User;

public class ProfileInfoFragment extends Fragment {

    TextView usernameText, been, favorited, favoritedText;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_profile, container, false );
        usernameText = (TextView) view.findViewById( R.id.welcomeText );
        been = (TextView) view.findViewById( R.id.been );
        favorited = (TextView) view.findViewById( R.id.favorited );
        favoritedText = (TextView) view.findViewById( R.id.favoritedText );
        
        setInfo();
        return view;
    }

    private void setInfo() {
        User mUser = User.getInstance();
        usernameText.setText( "Hi, " + mUser.m_name );

        been.setText( "been to " + mUser.getBeenPlaces().size()
                + " place(s)" );
        favorited.setText( "favorited " + mUser.getFavoritedPlaces().size()
                + " place(s)" );


        favoritedText.setText( "Favorites: " );
        for ( int i = 0; i < mUser.getFavoritedPlaces().size(); ++i ) {
            favoritedText.append( mUser.getFavoritedPlaces().get( i )
                    .getName() );
            favoritedText.append( "\n" );
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
}

/* Set user profile information */


