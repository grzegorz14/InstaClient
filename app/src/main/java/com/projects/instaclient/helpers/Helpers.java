package com.projects.instaclient.helpers;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.projects.instaclient.R;

import java.time.LocalDateTime;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convertDateToText(String date) {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(date);
        }
        catch (Exception ex) {
            return "";
        }

        if (now.getYear() > dateTime.getYear()) {
            int difference = now.getYear() - dateTime.getYear();
            if (difference == 1) {
                return difference + " year ago";
            }
            return difference + " years ago";
        }

        if (now.getMonthValue() > dateTime.getMonthValue()) {
            int difference = now.getMonthValue() - dateTime.getMonthValue();
            if (difference == 1) {
                return difference + " month ago";
            }
            return difference + " months ago";
        }

        if (now.getDayOfMonth() > dateTime.getDayOfMonth()) {
            int difference = now.getDayOfMonth() - dateTime.getDayOfMonth();
            if (difference == 1) {
                return "yesterday";
            }
            return difference + " days ago";
        }

        if (now.getHour() > dateTime.getHour()) {
            int difference = now.getHour() - dateTime.getHour();
            if (difference == 1) {
                return "1 hour ago";
            }
            return difference + " hours ago";
        }

        int difference = now.getMinute() - dateTime.getMinute();
        if (difference == 0) {
            return "now";
        }
        if (difference == 1) {
            return "1 minute ago";
        }
        return difference + " minutes ago";
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
