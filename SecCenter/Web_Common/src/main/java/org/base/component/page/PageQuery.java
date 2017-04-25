package org.base.component.page;

import java.io.Serializable;

public class PageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    private int pageNo = 1;

    private int pageSize = 10;

    public int getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
