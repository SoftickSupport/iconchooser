package com.softick.iconchooser;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class IconChooser
{
    String TAG = "IconChooser::";
    Context ctx;
    String ci_packageName = "";
    ArrayList<String> ci_activity_aliases;

    //current launcher activity number in ci_activity_aliases
//    int launcherActivityNum = -1;

    public IconChooser(Context context)
    {
        ctx = context;
        ci_activity_aliases = new ArrayList<>();
    }
    public IconChooser(Context context, String packageName, ArrayList<String> activity_aliases)
    {
        ctx = context;
        ci_activity_aliases = new ArrayList<>(activity_aliases);
        ci_packageName = packageName;
    }

    public void Init(String packageName, ArrayList<String> activity_aliases)
    {
        ci_packageName = packageName;
        ci_activity_aliases.addAll(activity_aliases);
    }

    public int getLauncherActivityNum()
    {
        int launcherActivity = -1;

        try
        {
            ComponentName componentName = null;
            String shortClassName = "";

            PackageManager packageManager = ctx.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(ci_packageName);
            if (intent != null) componentName = intent.getComponent();
            if (componentName != null) shortClassName = componentName.getShortClassName();

//            launcherActivity = Arrays.asList(ci_activity_aliases).indexOf(shortClassName);
            launcherActivity = Collections.singletonList(ci_activity_aliases).indexOf(shortClassName);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            //launcherActivity = -1;
        }

        //launcherActivityNum = launcherActivity;
        return launcherActivity;
    }

    public void chooseIcon(int iconNumber)
    {
        int launcherActivityNum = getLauncherActivityNum();
        Log.d(TAG, "Flutter. chooseIcon from CGA for icon: " + iconNumber + " , currentIcon: " + launcherActivityNum);

        if (iconNumber < 0 || iconNumber >= ci_activity_aliases.size()) return;
        if (launcherActivityNum < 0 || launcherActivityNum >= ci_activity_aliases.size()) return;

        PackageManager packageManager = ctx.getPackageManager();
        packageManager.setComponentEnabledSetting(new ComponentName(ci_packageName, ci_packageName + ci_activity_aliases.get(iconNumber)), PackageManager.COMPONENT_ENABLED_STATE_ENABLED,0);
        packageManager.setComponentEnabledSetting(new ComponentName(ci_packageName, ci_packageName + ci_activity_aliases.get(launcherActivityNum)), PackageManager.COMPONENT_ENABLED_STATE_DISABLED,0);
    }
}
