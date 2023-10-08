package com.oragif.jxpress.util;

import java.util.HashMap;

public class MimeTypes {
    private static HashMap<String, String> mimeTypes;

    static {
        mimeTypes = new HashMap<>();
        mimeTypes.put(".aac", "application/x-abiword");
        mimeTypes.put(".abw", "application/x-abiword");
        mimeTypes.put(".arc", "application/x-freearc");
        mimeTypes.put(".avif", "image/avif");
        mimeTypes.put(".avi", "video/x-msvideo");
        mimeTypes.put(".azw", "application/vnd.amazon.ebook");
        mimeTypes.put(".bin", "application/octet-stream");
        mimeTypes.put(".bmp", "image/bmp");
        mimeTypes.put(".bz", "application/x-bzip");
        mimeTypes.put(".bz2", "application/x-bzip2");
        mimeTypes.put(".cda", "application/x-cdf");
        mimeTypes.put(".csh", "application/x-csh");
        mimeTypes.put(".css", "text/css");
        mimeTypes.put(".csv", "text/csv");
        mimeTypes.put(".doc", "application/msword");
        mimeTypes.put(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        mimeTypes.put(".eot", "application/vnd.ms-fontobject");
        mimeTypes.put(".epub", "application/epub+zip");
        mimeTypes.put(".gz", "application/gzip");
        mimeTypes.put(".gif", "image/gif");
        mimeTypes.put(".htm", "text/html");
        mimeTypes.put(".html", "text/html");
        mimeTypes.put(".ico", "image/vnd.microsoft.icon");
        mimeTypes.put(".ics", "text/calendar");
        mimeTypes.put(".jar", "application/java-archive");
        mimeTypes.put(".jpeg", "image/jpeg");
        mimeTypes.put(".jpg", "image/jpeg");
        mimeTypes.put(".js", "text/javascript");
        mimeTypes.put(".json", "application/json");
        mimeTypes.put(".jsonld", "application/ld+json");
        mimeTypes.put(".mid", "audio/midi");
        mimeTypes.put(".midi", "audio/midi");
        mimeTypes.put(".mjs", "text/javascript");
        mimeTypes.put(".mp3", "audio/mpeg");
        mimeTypes.put(".mp4", "video/mp4");
        mimeTypes.put(".mpeg", "video/mpeg");
        mimeTypes.put(".mpkg", "application/vnd.apple.installer+xml");
        mimeTypes.put(".odp", "application/vnd.oasis.opendocument.presentation");
        mimeTypes.put(".ods", "application/vnd.oasis.opendocument.spreadsheet");
        mimeTypes.put(".odt", "application/vnd.oasis.opendocument.text");
        mimeTypes.put(".oga", "audio/ogg");
        mimeTypes.put(".ogv", "video/ogg");
        mimeTypes.put(".ogx", "application/ogg");
        mimeTypes.put(".opus", "audio/opus");
        mimeTypes.put(".otf", "font/otf");
        mimeTypes.put(".png", "image/png");
        mimeTypes.put(".pdf", "application/pdf");
        mimeTypes.put(".php", "application/x-httpd-php");
        mimeTypes.put(".ppt", "application/vnd.ms-powerpoint");
        mimeTypes.put(".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        mimeTypes.put(".rar", "application/vnd.rar");
        mimeTypes.put(".rtf", "application/rtf");
        mimeTypes.put(".sh", "application/x-sh");
        mimeTypes.put(".svg", "image/svg+xml");
        mimeTypes.put(".tar", "application/x-tar");
        mimeTypes.put(".tif", "image/tiff");
        mimeTypes.put(".tiff", "image/tiff");
        mimeTypes.put(".ts", "video/mp2t");
        mimeTypes.put(".ttf", "font/ttf");
        mimeTypes.put(".txt", "text/plain");
        mimeTypes.put(".vsd", "application/vnd.visio");
        mimeTypes.put(".wav", "audio/wav");
        mimeTypes.put(".weba", "audio/webm");
        mimeTypes.put(".webm", "video/webm");
        mimeTypes.put(".webp", "image/webp");
        mimeTypes.put(".woff", "font/woff");
        mimeTypes.put(".woff2", "font/woff2");
        mimeTypes.put(".xhtml", "application/xhtml+xml");
        mimeTypes.put(".xls", "application/vnd.ms-excel");
        mimeTypes.put(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        mimeTypes.put(".xml", "application/xml");
        mimeTypes.put(".xul", "application/vnd.mozilla.xul+xml");
        mimeTypes.put(".zip", "application/zip");
        mimeTypes.put(".3gp", "video/3gpp");
        mimeTypes.put(".3g2", "video/3gpp2");
        mimeTypes.put(".7z", "application/x-7z-compressed");
    }

    public static String getMimeType(String extension) {
        return mimeTypes.get(extension);
    }
}
