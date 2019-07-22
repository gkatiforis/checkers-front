package com.katiforis.checkers.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.katiforis.checkers.R;
import com.katiforis.checkers.activities.MenuActivity;
import com.katiforis.checkers.util.AudioPlayer;
import com.katiforis.checkers.util.LocalCache;

import info.hoang8f.widget.FButton;

import static com.katiforis.checkers.util.CachedObjectProperties.SOUND_ENABLED;

public class SettingsFragment extends DialogFragment {
    private static SettingsFragment INSTANCE = null;
    private Switch soundSwitch;
    private FButton logout;
    private Button closeButton;

    public static SettingsFragment getInstance() {
            synchronized(SettingsFragment.class) {
                INSTANCE = new SettingsFragment();
            }
        return INSTANCE;
    }

    public SettingsFragment(){
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_settings_layout,  null);
        AudioPlayer.getInstance(MenuActivity.INSTANCE).playPopup();
        soundSwitch = view.findViewById(R.id.soundSwitch);
        String soundEnabled = LocalCache.getInstance().getString(SOUND_ENABLED);
        if(soundEnabled == null || soundEnabled.equals("true")){
            soundSwitch.setChecked(true);
        }else{
            soundSwitch.setChecked(false);
        }
        closeButton = view.findViewById(R.id.closeButton);
        logout = view.findViewById(R.id.logoutButton);
        logout.setButtonColor(getResources().getColor(R.color.fbutton_color_silver2));


        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    LocalCache.getInstance().saveString(SOUND_ENABLED, "true");
                }else{
                    LocalCache.getInstance().saveString(SOUND_ENABLED, "false");
                }
                AudioPlayer.getInstance( MenuActivity.INSTANCE).mute(!isChecked);
            }
        });

        logout.setOnClickListener(c ->{
            AudioPlayer.getInstance( MenuActivity.INSTANCE).playClickButton();
            HomeFragment.getInstance().logout();
        });

        closeButton.setOnClickListener(c ->{
            AudioPlayer.getInstance( MenuActivity.INSTANCE).playPopup();
            this.dismiss();
        });

        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }
}