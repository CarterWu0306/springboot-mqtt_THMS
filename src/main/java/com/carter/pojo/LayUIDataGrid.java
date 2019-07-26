package com.carter.pojo;

import java.util.List;

public class LayUIDataGrid {
    private List<?> data;
    private long count;

    private Integer code=0;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long total) {
        this.count = total;
    }
}
