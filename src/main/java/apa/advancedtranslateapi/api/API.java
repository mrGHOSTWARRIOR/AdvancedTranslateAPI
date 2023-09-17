package apa.advancedtranslateapi.api;


import apa.translateapi.AdvancedTranslateAPI;

public class API {


    private final AdvancedTranslateAPI advancedTranslateAPI;


    public API(AdvancedTranslateAPI advancedTranslateAPI) {
        this.advancedTranslateAPI = advancedTranslateAPI;
    }

    public String translate(String text, String sourceLang, String targetLang) {
        return advancedTranslateAPI.getTranslate().translate(text, sourceLang, targetLang);
    }

    public String getLanguage(String text) {
        return advancedTranslateAPI.getTranslate().getLanguage(text);
    }


}
