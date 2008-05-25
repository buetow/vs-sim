package utils;


public final class VSTools {
    public static String getTimeString(long time) {
        String ret = ""+time;

        while (ret.length() < 6)
            ret = "0" + ret;

        return ret + "ms";
    }
    public static long getStringTime(String string) {
        try {
            /* Ignore the "ms" postfix */
            Long longValue = Long.valueOf(string.substring(0, string.length()-2));
            return longValue.longValue();
        } catch (NumberFormatException e) {
        }

        return 0;
    }
}
