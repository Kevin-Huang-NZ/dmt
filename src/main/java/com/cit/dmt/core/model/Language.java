package com.cit.dmt.core.model;

public class Language {
    
    private Long id;

    
    private String languageCode;

    
    private String languageName;

    
    private String isRoman;

    
    public Long getId() {
        return id;
    }

    
    public void setId(Long id) {
        this.id = id;
    }

    
    public String getLanguageCode() {
        return languageCode;
    }

    
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode == null ? null : languageCode.trim();
    }

    
    public String getLanguageName() {
        return languageName;
    }

    
    public void setLanguageName(String languageName) {
        this.languageName = languageName == null ? null : languageName.trim();
    }

    
    public String getIsRoman() {
        return isRoman;
    }

    
    public void setIsRoman(String isRoman) {
        this.isRoman = isRoman == null ? null : isRoman.trim();
    }
}
