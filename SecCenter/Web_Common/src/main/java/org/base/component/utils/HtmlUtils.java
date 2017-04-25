package org.base.component.utils;

public class HtmlUtils {

    public static String html(String content) {
        if (content == null) {
            return "";
        }
        String html = content;
        html = html.replace("&apos;", "'");
        html = html.replace("&amp;", "&");
        html = html.replace("&quot;", "\""); // "
        html = html.replace("&nbsp;", " ");// 替换空格
        html = html.replace("&lt;", "<");
        html = html.replace("&gt;", ">");
        return html;
    }

}
