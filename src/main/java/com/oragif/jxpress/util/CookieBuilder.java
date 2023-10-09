package com.oragif.jxpress.util;

import java.time.LocalDateTime;

public class CookieBuilder {
    private String key;
    private String value;
    private boolean httpOnly;
    private boolean secure;
    private boolean partitioned;
    private LocalDateTime expires;
    private Integer maxAge;
    private String domain;
    private String path;
    private SameSite sameSite;

    {
        this.httpOnly = false;
        this.secure = false;
        this.partitioned = false;
    }

    public CookieBuilder(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public CookieBuilder HTTPOnly() {
        this.httpOnly = true;
        return this;
    }

    public CookieBuilder secure() {
        this.secure = true;
        return this;
    }

    public CookieBuilder partitioned() {
        this.partitioned = true;
        return this;
    }

    public CookieBuilder expires(LocalDateTime dateTime) {
        this.expires = dateTime;
        return this;
    }

    public CookieBuilder maxAge(int maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public CookieBuilder domain(String domain) {
        this.domain = domain;
        return this;
    }

    public CookieBuilder path(String path) {
        this.path = path;
        return this;
    }

    public CookieBuilder sameSite(SameSite sameSite) {
        this.sameSite = sameSite;
        return this;
    }

    private String addOption(String key, String value) {
        return " " + key + "=" + value + ";";
    }

    public String build() {
        String cookieString = this.key + "=" + this.value + ";";

        if(this.expires != null)  cookieString += this.addOption("Expires"  , this.expires.toString());
        if(this.maxAge != null)   cookieString += this.addOption("Max-Age"  , this.maxAge.toString());
        if(this.domain != null)   cookieString += this.addOption("Domain"   , this.domain);
        if(this.path != null)     cookieString += this.addOption("Path"     , this.path);
        if(this.sameSite != null) cookieString += this.addOption("SameSite" , this.getSameSiteString(this.sameSite));

        if(this.secure == true)      cookieString += " Secure;";
        if(this.httpOnly == true)    cookieString += " HttpOnly;";
        if(this.partitioned == true) cookieString += " Partitioned;";

        return cookieString;
    }

    private String getSameSiteString(SameSite sameSite) {
        switch (sameSite) {
            case LAX -> { return "Lax"; }
            case STRICT -> { return "Strict"; }
            default -> { return "None"; }
        }
    }

    public enum SameSite {
        STRICT,
        LAX,
        NONE
    }
}
