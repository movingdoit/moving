package org.base.component.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @Document							说明该注解将被包含在javadoc中
 * @Retention(RetentionPolicy.RUNTIME)	注解会在class字节码文件中存在，在运行时可以通过反射获取到
 * @Target(ElementType.TYPE)	 		接口、类、枚举、注解
 * @Inherited							说明子类可以继承父类中的该注解
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)	
@Target(ElementType.TYPE)	 
@Inherited	
public @interface RequireLogin {

}
