package fr.benjamin.thiebaut.nfcemulator.Utils;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static void requestPermissions(Activity activity, int requestCode, String... permissions){
        List<String> toCheck = new ArrayList<>();

        // If permissions not provided, get permission list from manifest
        if (permissions.length == 0){
            List<String> allPerm = new ArrayList<>();
            try {
                PackageInfo info = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
                if (info.requestedPermissions != null) {
                    for (String p : info.requestedPermissions) {
                        allPerm.add(p);
                    }
                }
                permissions = allPerm.toArray(new String[0]);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        // check already granted permission
        for(int i = 0; i < permissions.length; i++){
            if (ContextCompat.checkSelfPermission(activity, permissions[i]) != PackageManager.PERMISSION_GRANTED){
                toCheck.add(permissions[i]);
            }
        }

        if (toCheck.size() > 0){
            // request the permission
            ActivityCompat.requestPermissions(activity,
                    toCheck.toArray(new String[0]),
                    requestCode);
        }
    }

}
