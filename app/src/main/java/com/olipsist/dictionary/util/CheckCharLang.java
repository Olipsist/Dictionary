package com.olipsist.dictionary.util;

/**
 * Created by Olipsist on 1/20/2016.
 */
public class CheckCharLang {

    public static boolean checkEn(String character){
        boolean result = false;
        String[] charCheck = {"A","a","B","b","C","c","D","d","E","e","F","f","G","g","H","h","I",
                "i","J","j","K","k","L","l","M","m","N","n","O","o","P","p","Q","q","R","r","S","s",
                "T","t","U","u","V","v","W","w","X","x","Y","y","Z","z"};
        for(String checker : charCheck){
            if(character.equals(checker)){
                result = true;
            }
        }
        return result;
    }
}
