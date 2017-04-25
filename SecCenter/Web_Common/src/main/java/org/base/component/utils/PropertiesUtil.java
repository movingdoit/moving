package org.base.component.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class PropertiesUtil {

    private final Log log = LogFactory.getLog(PropertiesUtil.class);

    private Properties prop = new Properties();

    public void load(String fullFilePath) {
        this.load(new File(fullFilePath));
    }

    public void load(File file) {
        try {
            this.load(new FileInputStream(file));
        } catch (Exception e) {
            this.prop = null;
            this.log.error("ERROR: Properties文件加载失败，使用默认值!");
            this.log.error(e.getMessage(), e);
        }
    }

    public void load(InputStream in) {
        try {
            this.prop.load(in);
        } catch (Exception e) {
            this.prop = null;
            this.log.error("ERROR: Properties文件加载失败，使用默认值!");
            this.log.error(e.getMessage(), e);
        }
    }

    /**
     * 从properties文件中，根据key取出一个特定property的值，并以String返回
     * 
     * @param key
     * @return
     */
    public String getStringProperty(String key) {
        return this.getStringProperty(key, "");
    }

    /**
     * 从properties文件中，根据key取出一个特定property的值，并以String返回。当取不到此key的属性值时，返回一个默认值。
     * 
     * @param key
     * @return
     */
    public String getStringProperty(String key, String defaultValue) {
        if (this.prop == null || this.prop.getProperty(key) == null) {
            return defaultValue;
        }
        return this.prop.getProperty(key);
    }

    /**
     * 从properties文件中，根据key取出一个特定property的值，并以int返回
     * 
     * @param key
     * @return
     */
    public int getIntProperty(String key) {
        return this.getIntProperty(key, 0);
    }

    /**
     * 从properties文件中，根据key取出一个特定property的值，并以int返回。当取不到此key的属性值时，返回一个默认值。
     * 
     * @param key
     * @return
     */
    public int getIntProperty(String key, int defaultValue) {
        if (this.prop == null || this.prop.getProperty(key) == null) {
            return defaultValue;
        }
        int res = defaultValue;
        try {
            res = Integer.parseInt(this.prop.getProperty(key).trim());
        } catch (Exception e) {
            this.log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 从properties文件中，根据key取出一个特定property的值，并以long返回
     * 
     * @param key
     * @return
     */
    public long getLongProperty(String key) {
        return this.getLongProperty(key, 0);
    }

    /**
     * 从properties文件中，根据key取出一个特定property的值，并以long返回。当取不到此key的属性值时，返回一个默认值。
     * 
     * @param key
     * @return
     */
    public long getLongProperty(String key, long defaultValue) {
        if (this.prop == null || this.prop.getProperty(key) == null) {
            return defaultValue;
        }
        long res = defaultValue;
        try {
            res = Long.parseLong(this.prop.getProperty(key).trim());
        } catch (Exception e) {
            this.log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 从properties文件中，根据key取出一个特定property的值，并以double返回
     * 
     * @param key
     * @return
     */
    public double getDoubleProperty(String key) {
        return this.getDoubleProperty(key, 0.0);
    }

    /**
     * 从properties文件中，根据key取出一个特定property的值，并以double返回。当取不到此key的属性值时，返回一个默认值。
     * 
     * @param key
     * @return
     */
    public double getDoubleProperty(String key, double defaultValue) {
        if (this.prop == null || this.prop.getProperty(key) == null) {
            return defaultValue;
        }
        double res = defaultValue;
        try {
            res = Double.parseDouble(this.prop.getProperty(key).trim());
        } catch (Exception e) {
            this.log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 从properties文件中，根据key取出一个特定property的值，并以boolean返回
     * 
     * @param key
     * @return
     */
    public boolean getBooleanProperty(String key) {
        return this.getBooleanProperty(key, false);
    }

    /**
     * 从properties文件中，根据key取出一个特定property的值，并以boolean返回。当取不到此key的属性值时，返回一个默认值。
     * 
     * @param key
     * @return
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        if (this.prop == null || this.prop.getProperty(key) == null) {
            return defaultValue;
        }
        boolean res = defaultValue;
        String value = this.prop.getProperty(key).toLowerCase().trim();
        res = value.equals("true") || value.equals("1"); // 属性值为true或1的，都可以返回true
        return res;
    }

}
