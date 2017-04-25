package org.base.component.rmi.client;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.base.component.context.BeanDefinitionScanner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.util.Assert;

public class RmiProxyClientBean extends InstantiationAwareBeanPostProcessorAdapter implements ApplicationContextAware{
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private static ApplicationContext context;
	
	private String serviceUrl;
	
	private String BACKSLASH = "/";
	
	private String basePackage;
	
	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	private BeanDefinitionScanner beanDefinitionScanner = new BeanDefinitionScanner(true);
	
  	private DefaultListableBeanFactory defaultListableBeanFactory;
  	
  	public RmiProxyClientBean(){}
	
	@PostConstruct
	public void init() throws Exception{
		if(basePackage.equals("")){
			basePackage = "org.meibaobao.ecos.*.service.*";
		}
		
		Set<BeanDefinition> candidates = beanDefinitionScanner.doScan(basePackage);
		Assert.notNull(context);
		defaultListableBeanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
		
		for (BeanDefinition candidate : candidates) {
			registerBean(Class.forName(candidate.getBeanClassName()));
		}
		
	}
	
	@PreDestroy
	public void destroy(){}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	
    private void registerBean(Class<?> clazz){
    	
    	if(!serviceUrl.endsWith(BACKSLASH)){
    		serviceUrl = serviceUrl + BACKSLASH;
    	}
    	
    	RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
    	BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(RmiProxyFactoryBean.class);
      	beanDefinitionBuilder.addPropertyValue("serviceUrl", serviceUrl + clazz.getSimpleName());
      	beanDefinitionBuilder.addPropertyValue("serviceInterface", clazz);
        defaultListableBeanFactory.registerBeanDefinition(clazz.getSimpleName()+"ProxyBean", beanDefinitionBuilder.getBeanDefinition());	
    	
    	try{
    		rmiProxyFactoryBean.afterPropertiesSet();
    		rmiProxyFactoryBean.setLookupStubOnStartup(false);
    		rmiProxyFactoryBean.setRefreshStubOnConnectFailure(true);
        }catch(Exception e){
            logger.error("Can't get the remote RMI server: "+ serviceUrl + clazz.getSimpleName()+", because " + e.getMessage());
        }
        logger.info("Success to init RMI local proxy service for " + clazz.getSimpleName());
    }
    
	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		return super.postProcessBeforeInstantiation(beanClass, beanName);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}
	


}
	