package com.caoyang.tapon.Utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by Administrator on 2016/06/14.
 */
public class PermissionsUtils {
    private final Context context;

    public PermissionsUtils(Context context) {
        this.context = context;
    }

    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    private boolean lacksPermission(String permission) {
        int isLack = ContextCompat.checkSelfPermission(context, permission);
        return isLack == PackageManager.PERMISSION_DENIED;
    }

}
