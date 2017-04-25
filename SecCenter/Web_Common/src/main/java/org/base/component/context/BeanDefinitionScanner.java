package org.base.component.context;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.base.component.annotations.RmiInterface;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

public class BeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

	public BeanDefinitionScanner(boolean useDefaultFilters) {
		super(useDefaultFilters);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void registerDefaultFilters() {
		super.addIncludeFilter(new AnnotationTypeFilter(RmiInterface.class));
		super.addExcludeFilter(new AnnotationTypeFilter(Component.class));
		ClassLoader cl = ClassPathScanningCandidateComponentProvider.class.getClassLoader();
		try{
			super.addIncludeFilter(new AnnotationTypeFilter(((Class<? extends Annotation>) cl.loadClass("javax.annotation.ManagedBean")), false));
		}catch (ClassNotFoundException ex) {
			logger.error("JSR-250 1.1 API (as included in Java EE 6) not available - simply skip"+ex);
		}
		
		try{
			super.addIncludeFilter(new AnnotationTypeFilter(((Class<? extends Annotation>) cl.loadClass("javax.inject.Named")), false));
		}catch (ClassNotFoundException ex) {
			logger.error("JSR-330 API not available - simply skip"+ex);
		}
	}

	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return (true && beanDefinition.getMetadata().isIndependent());
	}

	
	
	public Set<BeanDefinition> doScan(String basePackage) throws Exception {
		return findCandidateComponents(basePackage);
	}
	
}
