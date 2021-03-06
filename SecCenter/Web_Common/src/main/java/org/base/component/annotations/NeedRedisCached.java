/**
 * 
 */
package org.base.component.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NeedRedisCached {

}
