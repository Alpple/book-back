package com.book.beans;

public class Result<T> {
	T result;
	int code;
	String msg;
	public final static int HTTP_OK = 200;
	public final static int UN_LOGIN = -1;
	public final static int ERROR = 0;
	public final static int UNK_ERROR = 0;

	public Result(T result, int code) {
		this.result = result;
		this.code = code;
	}

	public Result(T result, int code, String msg) {
		this.result = result;
		this.code = code;
		this.msg = msg;
	}

	public Result(T result) {
		this.result = result;
		this.code = HTTP_OK;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static <T> Result<T> error(T t,String msg) {
		return new Result<>(t, ERROR, msg);
	}
	public static <T> Result<T> error(T t,int code,String msg) {
		return new Result<>(t, code, msg);
	}
	public static <T> Result<T> success(T t,String msg) {
		return new Result<>(t, HTTP_OK,msg);
	}

	@Override
	public String toString() {
		return "Result{" +
				"result=" + result +
				", code=" + code +
				'}';
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
