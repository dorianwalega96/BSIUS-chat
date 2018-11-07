package CipherService;

public class XorCipher {
    public static String Encrypt(String key1, String key2, String text){
        String key = "";
        int i = 0;
        while (i<key1.length()&&i<key2.length()) {
            key += key1.charAt(i);
            key += key2.charAt(i);
            i++;
        }
        if(key1.length()>key2.length()){
            key+=key1.substring(i);
        } else if (key1.length()<key2.length()){
            key+=key2.substring(i);
        }

        StringBuilder stringBuilder = new StringBuilder();
        for(i = 0; i<text.length();i++) {
            char c= (char) (key.charAt(i%key.length()) ^ text.charAt(i));
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public static String Decrypt(String key1, String key2, String text){
        return Encrypt(key1, key2, text);
    }
}