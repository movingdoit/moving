package org.base.component.utils;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;


/** 
 * Bean工具类——提供了常用的bean操作，继承自Spring的org.springframework.beans.BeanUtils，并添加了一些新的方法
 *  
 */

public class BeanUtils extends org.springframework.beans.BeanUtils {

	private static Mapper dozerMapper = new DozerBeanMapper(); 
	
	/** 
	 * 将一个对象的内容，深拷贝到一个新对象（如果仅仅是浅拷贝时，请使用BeanUtils.copyProperties方法，性能会更好）
	 * @param sourceObject 源对象
	 * @param destinationClass 目标对象的Class
	 * @return  拷贝好的目标对象
	 * @throws 
	 */
	public static <T> T deepCopy(Object sourceObject, Class<T> destinationClass) {
		return dozerMapper.map(sourceObject, destinationClass);
	}
	
	/** 
	 * 将一个对象的内容，深拷贝到一个新对象（如果仅仅是浅拷贝时，请使用BeanUtils.copyProperties方法，性能会更好）
	 * @param sourceObject 源对象
	 * @param destinationObject  目标对象
	 * @throws 
	 */
	public static void deepCopy(Object sourceObject, Object destinationObject) {
		dozerMapper.map(sourceObject, destinationObject);
	}
	
	/**
	 * 将多个对象的内容，深拷贝到一个新对象（如果仅仅是浅拷贝时，请使用BeanUtils.copyProperties方法，性能会更好）
	 * @param <T>
	 * @param sourceObject
	 * @param genericsClass
	 * @return
	 */
	public static <T> List<T> deepCopys(List sourceObject, Class<T> genericsClass){
		
		List<T> result = null;
		if(null != sourceObject) {
			result = new ArrayList<T>();
			for(Object obj : sourceObject) {
				result.add(deepCopy(obj, genericsClass));
			}
		}
		return result;
	}
}