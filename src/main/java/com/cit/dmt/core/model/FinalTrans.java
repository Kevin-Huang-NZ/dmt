package com.cit.dmt.core.model;


public class FinalTrans extends CheckAssignmentDetail {
    private String original;

    private String country;

    private String language;

    private String gec;

    private String memo;

    private String romanStatus;

    private String roman;

    private String transStatus;

    private String transliteration;

    private String freeTranslation;

    public String getOriginal() {
        return original;
    }

    
    public void setOriginal(String original) {
        this.original = original == null ? null : original.trim();
    }

    
    public String getCountry() {
        return country;
    }

    
    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    
    public String getLanguage() {
        return language;
    }

    
    public void setLanguage(String language) {
        this.language = language == null ? null : language.trim();
    }

    
    public String getGec() {
        return gec;
    }

    
    public void setGec(String gec) {
        this.gec = gec == null ? null : gec.trim();
    }

    
    public String getMemo() {
        return memo;
    }

    
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    
    public String getRomanStatus() {
        return romanStatus;
    }

    
    public void setRomanStatus(String romanStatus) {
        this.romanStatus = romanStatus == null ? null : romanStatus.trim();
    }

    
    public String getRoman() {
        return roman;
    }

    
    public void setRoman(String roman) {
        this.roman = roman == null ? null : roman.trim();
    }

    
    public String getTransStatus() {
        return transStatus;
    }

    
    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus == null ? null : transStatus.trim();
    }

    
    public String getTransliteration() {
        return transliteration;
    }

    
    public void setTransliteration(String transliteration) {
        this.transliteration = transliteration == null ? null : transliteration.trim();
    }

    
    public String getFreeTranslation() {
        return freeTranslation;
    }

    
    public void setFreeTranslation(String freeTranslation) {
        this.freeTranslation = freeTranslation == null ? null : freeTranslation.trim();
    }
}
