package com.cit.dmt.core.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

public class CtCommon {
    
    @ExcelIgnore
    private Long id;

    
    @ExcelIgnore
    private String countryCode;

    
    @ExcelIgnore
    private String languageCode;


    @ExcelProperty(index = 0)
    private String original;


    @ExcelProperty(index = 1)
    private String originalAbbr;


    @ExcelProperty(index = 2)
    private String originalType;


    @ExcelProperty(index = 4)
    private String roman;


    @ExcelProperty(index = 5)
    private String matchWay;


    @ExcelProperty(index = 7)
    private String matchParams;


    @ExcelProperty(index = 8)
    private String transliteration;


    @ExcelProperty(index = 9)
    private String freeTranslation;

    
    public Long getId() {
        return id;
    }

    
    public void setId(Long id) {
        this.id = id;
    }

    
    public String getCountryCode() {
        return countryCode;
    }

    
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode == null ? null : countryCode.trim();
    }

    
    public String getLanguageCode() {
        return languageCode;
    }

    
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode == null ? null : languageCode.trim();
    }

    
    public String getOriginal() {
        return original;
    }

    
    public void setOriginal(String original) {
        this.original = original == null ? null : original.trim();
    }

    
    public String getOriginalAbbr() {
        return originalAbbr;
    }

    
    public void setOriginalAbbr(String originalAbbr) {
        this.originalAbbr = originalAbbr == null ? null : originalAbbr.trim();
    }

    
    public String getOriginalType() {
        return originalType;
    }

    
    public void setOriginalType(String originalType) {
        this.originalType = originalType == null ? null : originalType.trim();
    }

    
    public String getRoman() {
        return roman;
    }

    
    public void setRoman(String roman) {
        this.roman = roman == null ? null : roman.trim();
    }

    
    public String getMatchWay() {
        return matchWay;
    }

    
    public void setMatchWay(String matchWay) {
        this.matchWay = matchWay == null ? null : matchWay.trim();
    }

    
    public String getMatchParams() {
        return matchParams;
    }

    
    public void setMatchParams(String matchParams) {
        this.matchParams = matchParams == null ? null : matchParams.trim();
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
