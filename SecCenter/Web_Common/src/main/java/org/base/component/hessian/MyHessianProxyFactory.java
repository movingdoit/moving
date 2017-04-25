package org.base.component.hessian;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import com.caucho.hessian.client.HessianProxyFactory;

public class MyHessianProxyFactory extends HessianProxyFactory {
	private int connectTimeOut = 10000;

    private int readTimeOut = 10000;
    private String  _basicAuth;
    // Field descriptor #126 Ljava/lang/String;
    private java.lang.String _user;
    
    // Field descriptor #126 Ljava/lang/String;
    private java.lang.String _password;
    
    public java.lang.String get_user() {
		return _user;
	}

	public void set_user(java.lang.String _user) {
		this._user = _user;
	}

	public java.lang.String get_password() {
		return _password;
	}

	public void set_password(java.lang.String _password) {
		this._password = _password;
	}

	public int getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public String get_basicAuth() {
		return _basicAuth;
	}

	public void set_basicAuth(String _basicAuth) {
		this._basicAuth = _basicAuth;
	}

	protected URLConnection openConnection(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        if (this.connectTimeOut > 0) {
            conn.setConnectTimeout(this.connectTimeOut);
        }
        if (this.readTimeOut > 0) {
            conn.setReadTimeout(this.readTimeOut);
        }
        conn.setRequestProperty("Content-Type", "x-application/hessian");
            if (_basicAuth != null)
                  conn.setRequestProperty("Authorization", _basicAuth);
            else if (_user != null && _password != null) {
                  _basicAuth = "Basic " + base64(_user + ":" + _password);
                  conn.setRequestProperty("Authorization", _basicAuth);
            }
        return conn;
    }
	
	
		private String base64(String value)
	   /*     */   {
	   /* 484 */     StringBuffer cb = new StringBuffer();
	   /*     */ 
	   /* 486 */     int i = 0;
	   /* 487 */     for (i = 0; i + 2 < value.length(); i += 3) {
	   /* 488 */       long chunk = value.charAt(i);
	   /* 489 */       chunk = (chunk << 8) + value.charAt(i + 1);
	   /* 490 */       chunk = (chunk << 8) + value.charAt(i + 2);
	   /*     */ 
	   /* 492 */       cb.append(encode(chunk >> 18));
	   /* 493 */       cb.append(encode(chunk >> 12));
	   /* 494 */       cb.append(encode(chunk >> 6));
	   /* 495 */       cb.append(encode(chunk));
	   /*     */     }
	   /*     */ 
	   /* 498 */     if (i + 1 < value.length()) {
	   /* 499 */       long chunk = value.charAt(i);
	   /* 500 */       chunk = (chunk << 8) + value.charAt(i + 1);
	   /* 501 */       chunk <<= 8;
	   /*     */ 
	   /* 503 */       cb.append(encode(chunk >> 18));
	   /* 504 */       cb.append(encode(chunk >> 12));
	   /* 505 */       cb.append(encode(chunk >> 6));
	   /* 506 */       cb.append('=');
	   /*     */     }
	   /* 508 */     else if (i < value.length()) {
	   /* 509 */       long chunk = value.charAt(i);
	   /* 510 */       chunk <<= 16;
	   /*     */ 
	   /* 512 */       cb.append(encode(chunk >> 18));
	   /* 513 */       cb.append(encode(chunk >> 12));
	   /* 514 */       cb.append('=');
	   /* 515 */       cb.append('=');
	   /*     */     }
	   /*     */ 
	   /* 518 */     return cb.toString();
	   /*     */   }
}
