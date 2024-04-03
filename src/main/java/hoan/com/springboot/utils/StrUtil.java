package hoan.com.springboot.utils;

/**
 * @author: Admin
 * @date: 7/9/2023
 **/
public class StrUtil {
    public static final String EMPTY = "";
    public static final String RETURN_NEW_LINE = "\r\n";
    public static final String NEW_LINE = "\n";

    public static String getString(String value) {
        return getString(value, EMPTY);
    }

    public static String getString(String value, String defaultValue) {
        return get(value, defaultValue);
    }

    public static String get(String value, String defaultValue) {
        if (value != null) {
            value = value.trim();
            value = replace(value, RETURN_NEW_LINE, NEW_LINE);

            return value;
        }

        return defaultValue;
    }

    /**
     * Replace.
     *
     * @param s      the s
     * @param oldSub the old sub
     * @param newSub the new sub
     * @return the string
     */
    public static String replace(String s, String oldSub, String newSub) {
        return replace(s, oldSub, newSub, 0);
    }

    public static String replace(String s, String oldSub, String newSub, int fromIndex) {

        if (s == null) {
            return null;
        }

        if (oldSub == null || oldSub.equals(EMPTY)) {
            return s;
        }

        if (newSub == null) {
            newSub = EMPTY;
        }

        int y = s.indexOf(oldSub, fromIndex);

        if (y >= 0) {
            StringBuilder sb = new StringBuilder();

            int length = oldSub.length();
            int x = 0;

            while (x <= y) {
                sb.append(s, x, y);
                sb.append(newSub);

                x = y + length;
                y = s.indexOf(oldSub, x);
            }

            sb.append(s.substring(x));

            return sb.toString();
        } else {
            return s;
        }
    }
}
