package name.peterbukhal.android.redmine.util;

import android.text.TextUtils;

import java.math.BigInteger;
import java.security.MessageDigest;

public final class Utils {

    public static String getGravatar(String email) {
        if (TextUtils.isEmpty(email)) {
            email = "default";
        }

        return "https://www.gravatar.com/avatar/" + md5(email.toLowerCase().trim()) + "?default=identicon&size=50";
    }

    private static String md5(String string) {
        String hash = null;

        try {
            string = new BigInteger(1, MessageDigest.getInstance("md5")
                    .digest(string.getBytes())).toString(16);

            while (string.length() < 32) {
                string = "0" + string;
            }

            hash = string;
        } catch (Exception e) {
            //
        }

        return hash;
    }

}
