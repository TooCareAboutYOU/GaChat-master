package com.gachat.main.base;

import com.gachat.main.beans.ErrorBean;

import java.io.Serializable;


public class BaseBean<T> implements Serializable {


    private int code;
    private T result;
    private ErrorBean error;

    public BaseBean() { }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"code\":")
                .append(code);
        sb.append(",\"result\":")
                .append(result);
        sb.append(",\"error\":")
                .append(error);
        sb.append('}');
        return sb.toString();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }
}
