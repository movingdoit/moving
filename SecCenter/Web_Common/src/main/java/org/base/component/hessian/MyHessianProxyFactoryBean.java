package org.base.component.hessian;

import org.springframework.remoting.caucho.HessianProxyFactoryBean;

public class MyHessianProxyFactoryBean  extends HessianProxyFactoryBean {

    private MyHessianProxyFactory proxyFactory = new MyHessianProxyFactory();

    private int readTimeOut = 10000;

    private int connectTimeOut = 10000;

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public int getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public void afterPropertiesSet() {
        proxyFactory.setReadTimeout(readTimeOut);
        proxyFactory.setConnectTimeOut(connectTimeOut);
        setProxyFactory(proxyFactory);
        super.afterPropertiesSet();
    }
}
