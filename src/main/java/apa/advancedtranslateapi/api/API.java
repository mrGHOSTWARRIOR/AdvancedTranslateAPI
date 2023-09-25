package apa.advancedtranslateapi.api;


import apa.translateapi.AdvancedTranslateAPI;

public class API {


    private final AdvancedTranslateAPI advancedTranslateAPI;


    public API(AdvancedTranslateAPI advancedTranslateAPI) {
        this.advancedTranslateAPI = advancedTranslateAPI;
    }

    public String translateGoogle(String text, String sourceLang, String targetLang) {
        return advancedTranslateAPI.getTranslate().translate(text, sourceLang, targetLang);
    }

    public String getLanguageGoogle(String text) {
        return advancedTranslateAPI.getTranslate().getLanguage(text);
    }

    public String translateGPT3(String text, String sourceLang, String targetLang) {
        return advancedTranslateAPI.getGpt3Translator().translateGPT3(text, sourceLang, targetLang);
    }


}
