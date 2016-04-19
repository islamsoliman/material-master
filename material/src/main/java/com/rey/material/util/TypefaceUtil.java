package com.rey.material.util;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by Rey on 12/23/2014.
 */
public class TypefaceUtil {

    private static final HashMap<String, Typeface> sCachedFonts = new HashMap<String, Typeface>();
    private static final String PREFIX_ASSET = "asset:";

    private TypefaceUtil() {
    }

    /**
     * @param familyName if start with 'asset:' prefix, then load font from asset folder.
     * @return
     */
    public static Typeface load(Context context, String familyName, int style) {
        if(familyName != null && familyName.startsWith(PREFIX_ASSET))
            synchronized (sCachedFonts) {
                try {
                    if (!sCachedFonts.containsKey(familyName)) {
                        final Typeface typeface = Typeface.createFromAsset(context.getAssets(), familyName.substring(PREFIX_ASSET.length()));
                        sCachedFonts.put(familyName, typeface);
                        return typeface;
                    }
                } catch (Exception e) {
                    return Typeface.DEFAULT;
                }

                return sCachedFonts.get(familyName);
            }

        return Typeface.create(familyName, style);
    }

    public static String getArNum(String number) {
        if (!LocaleUtil.getLocale().getLanguage().equalsIgnoreCase("ar")) {
            return number;
        }

        char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        try {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < number.length(); i++) {
                if (Character.isDigit(number.charAt(i))) {
                    builder.append(arabicChars[(int) (number.charAt(i)) - 48]);
                } else {
                    builder.append(number.charAt(i));
                }
            }
            return builder.toString();
        } catch (Exception e) {

            return number;
        }
    }

    public static String getArNum(int numberText) {
        String number = String.valueOf(numberText);
        if (!LocaleUtil.getLocale().getLanguage().equalsIgnoreCase("ar")) {
            return number;
        }
        char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < number.length(); i++) {
            if (Character.isDigit(number.charAt(i))) {
                int arabicNumber = (int) (number.charAt(i)) - 48;
                if (arabicNumber < arabicChars.length) {
                    builder.append(arabicChars[arabicNumber]);
                } else {
                    return number;
                }
            } else {
                builder.append(number.charAt(i));
            }
        }
        return builder.toString();
    }

}

