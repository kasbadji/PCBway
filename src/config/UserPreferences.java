package config;

import java.util.prefs.Preferences;

public class UserPreferences {
    private static final String PREF_EMAIL = "rememberedEmail";
    private static final String PREF_REMEMBER = "rememberMe";
    private static final Preferences prefs = Preferences.userNodeForPackage(UserPreferences.class);

    public static void saveRememberedEmail(String email) {
        prefs.put(PREF_EMAIL, email);
        prefs.putBoolean(PREF_REMEMBER, true);
    }

    public static String getRememberedEmail() {
        return prefs.get(PREF_EMAIL, "");
    }

    public static boolean shouldRememberUser() {
        return prefs.getBoolean(PREF_REMEMBER, false);
    }

    public static void clearRememberedEmail() {
        prefs.remove(PREF_EMAIL);
        prefs.putBoolean(PREF_REMEMBER, false);
    }
}
