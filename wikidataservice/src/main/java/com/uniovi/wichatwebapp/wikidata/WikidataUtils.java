package com.uniovi.wichatwebapp.wikidata;

public class WikidataUtils {
    public final static String LINKCONCAT = "#* &%";
    public final static String[] allowedExtensions = {"png", "jpg", "jpeg", "gif", "svg"};

    private WikidataUtils() {
    }

    public static boolean isEntityName(String label){
        boolean isEntityName = true; // Check if it is like Q232334
        if (label.startsWith("Q") ){
            for (int i=1; i<label.length(); i++){
                if (!Character.isDigit(label.charAt(i))){
                    isEntityName = false;
                }
            }
            if (isEntityName){
                return true;
            }
        }
        return false;
    }

    public static boolean notAllowedExtension(String link){
        for (String s : allowedExtensions){
            if (link.endsWith(s)){
                return false;
            }
        }
        return true;
    }
}
