package CipherService;

public class VigenereCipher {
    public static String Encrypt(String key1, String key2, String text) {
        String key = key(key1, key2);
        int i = 0;

        int shift = 0;
        int offset = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (i = 0; i < text.length(); i++) {
            if(text.charAt(i) >= 'a' && text.charAt(i) <= 'z') {
                shift = ((int)(Character.toLowerCase(key.charAt(i%key.length())))) - (int)'a';
                offset = ((int)(text.charAt(i))) - (int)'a';
                stringBuilder.append((char)((offset+shift)%26+'a'));
            } else if (text.charAt(i) >= 'A' && text.charAt(i) <= 'Z') {
                shift = ((int)(Character.toUpperCase(key.charAt(i%key.length())))) - (int)'A';
                offset = ((int)(text.charAt(i))) - (int)'A';
                stringBuilder.append((char)((offset+shift)%26+(int)'A'));
            } else {
                stringBuilder.append(text.charAt(i));
            }
        }
        return stringBuilder.toString();
    }

    public static String Decrypt(String key1, String key2, String text) {
        String key = key(key1, key2);
        int i = 0;
        int shift = 0;
        int offset = 0;
        int modShiftOffset = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (i = 0; i < text.length(); i++) {
            if(text.charAt(i) >= 'a' && text.charAt(i) <= 'z') {
                shift = ((int)(Character.toLowerCase(key.charAt(i%key.length())))) - (int)'a';
                offset = ((int)(text.charAt(i))) - (int)'a' + 26;
                modShiftOffset = (offset-shift)%26;
                stringBuilder.append((char)(modShiftOffset+'a'));
            } else if (text.charAt(i) >= 'A' && text.charAt(i) <= 'Z') {

                shift = ((int)(Character.toUpperCase(key.charAt(i%key.length())))) - (int)'A';
                offset = ((int)(text.charAt(i))) - (int)'A' + 26;
                //if (offset-shift<0)
                modShiftOffset = (offset-shift)%26;

                stringBuilder.append((char)(modShiftOffset+'A'));
            } else {
                stringBuilder.append(text.charAt(i));
            }
        }
        return stringBuilder.toString();
    }

    private static String key(String key1, String key2) {
        String key = "";
        int i = 0;
        while (i < key1.length() && i < key2.length()) {
            key += key1.charAt(i);
            key += key2.charAt(i);
            i++;
        }
        if (key1.length() > key2.length()) {
            key += key1.substring(i);
        } else if (key1.length() < key2.length()) {
            key += key2.substring(i);
        }

        for(i=0; i<key.length();i++) {
            if((key.charAt(i) <= 'a' || key.charAt(i) >= 'z') &&
                    (key.charAt(i) <= 'A' || key.charAt(i) >= 'Z') ) {
                if(i+1==key.length())
                    key = key.substring(0, i) + 'a';
                else if (i==0)
                    key = 'a' + key.substring(1);
                else
                    key = key.substring(0, i) + 'a' + key.substring(i+1);
            }
        }
        return key;
    }
}