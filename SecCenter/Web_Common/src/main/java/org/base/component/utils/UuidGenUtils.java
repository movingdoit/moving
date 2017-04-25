package org.base.component.utils;

import java.util.UUID;

/**
 * UUID生成工具
 * 
 */

public final class UuidGenUtils {

    /**
     * 生成一个UUID串（32个字符，其中的字母为小写）
     * @return
     * @throws
     */
    public static String genUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
