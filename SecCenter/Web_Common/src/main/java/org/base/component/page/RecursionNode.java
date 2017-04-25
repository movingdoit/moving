package org.base.component.page;

import java.io.Serializable;
import java.util.List;

/**
 * 用于树形递归节点：即节点下面还有子节点链表
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 * @date 2013-6-28 下午03:15:21
 * @param <T>
 */
public class RecursionNode<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private T item;

    private List<RecursionNode<T>> nodes;

    private int level;

    public T getItem() {
        return this.item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public List<RecursionNode<T>> getNodes() {
        return this.nodes;
    }

    public void setNodes(List<RecursionNode<T>> nodes) {
        this.nodes = nodes;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
