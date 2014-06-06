package com.example.restarun.LoginActivity;

import java.util.Arrays;

import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.restarun.R;
import com.facebook.widget.LoginButton;

public class LoginFragment extends Fragment {

    private static View loginView;


    private static Button guestButton;
    private static ImageView foodImageView;
    private static LoginButton authButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        loginView = inflater
                .inflate( R.layout.fragment_login, container, false );

        /* Load assets */
        Typeface titleFont = Typeface.createFromAsset( getActivity()
                .getAssets(), "fonts/Roboto-Light.ttf" );
        Typeface font = Typeface.createFromAsset( getActivity().getAssets(),
                "fonts/Roboto-Regular.ttf" );
        Drawable guestIcon = getResources().getDrawable( R.drawable.guest );
        Drawable foodIcon = getResources().getDrawable( R.drawable.food );

        /* Store references to the buttons */
        authButton = (LoginButton) loginView.findViewById( R.id.authButton );
        /* Grant Facebook permissions */
        authButton.setReadPermissions( Arrays.asList( "public_profile",
                "user_likes", "user_status" ) );
        authButton.setBackgroundResource( R.drawable.buttons );
        authButton.setTypeface( font );
        
        guestButton = (Button) loginView.findViewById( R.id.guestButton );
        foodImageView = (ImageView) loginView.findViewById( R.id.foodImageView );
        TextView title = (TextView) loginView.findViewById( R.id.title );

        /* Allow the Facebook button to be embeded within a fragment */
        // authButton.setFragment( this );

        /* Workaround to change the look of the Facebook button */
        guestButton.setTextColor( Color.WHITE );

        guestButton.setTypeface( font );
        title.setTextColor( Color.WHITE );
        title.setTypeface( titleFont );

        guestIcon.setColorFilter( new LightingColorFilter( Color.WHITE,
                Color.WHITE ) );
        foodIcon.setColorFilter( new LightingColorFilter( Color.WHITE,
                Color.WHITE ) );

        guestIcon.setBounds( 0, 0, 40, 40 );
        foodIcon.setBounds( 0, 0, 40, 40 );

        guestButton.setCompoundDrawables( guestIcon, null, null, null );
        foodImageView.setImageDrawable( foodIcon );

        EditText username = (EditText) loginView.findViewById( R.id.username );
        EditText password = (EditText) loginView.findViewById( R.id.password );

        username.setTypeface( font );
        password.setTypeface( font );
        password.setTransformationMethod( new PasswordTransformationMethod() );

        return loginView;
    }

}