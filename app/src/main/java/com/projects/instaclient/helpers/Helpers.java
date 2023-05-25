package com.projects.instaclient.helpers;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.projects.instaclient.R;

public class Helpers {

    public static String convertLikesToText(int likes) {
        if (likes == 1) {
            return "1 like";
        }
        else if (likes < 1000) {
            return likes + " likes";
        }
        else {
            int thousands = likes / 1000;
            int hundreds = (likes % 1000) / 100;

            if (hundreds == 0) {
                return thousands + "k likes";
            }
            else {
                return thousands + "." + hundreds + "k likes";
            }
        }
    }

    public static void replaceMainFragment(FragmentManager fragmentManager, Fragment fragment) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.mainFrameLayout, fragment)
                .addToBackStack(null)
                .commit();
    }

    public static void replaceNavigationFragment(FragmentManager fragmentManager, Fragment fragment) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.navigationFrameLayout, fragment)
                .addToBackStack(null)
                .commit();
    }
}
