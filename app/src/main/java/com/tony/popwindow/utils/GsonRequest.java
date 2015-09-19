package com.tony.popwindow.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;


public class GsonRequest<T> extends Request<T> {
	private final Class<T> clazz;
	private final Listener<T> mListener;
	private Context mContext;

	public GsonRequest(int method, String uri, Class<T> clazz,
			Listener<T> listener, ErrorListener errorListener, Context context) {
		super(method, uri, errorListener);
		mContext = context;
		this.clazz = clazz;
		this.mListener = listener;
		setRetryPolicy(getMyOwnDefaultRetryPolicy());

	}

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		String parsed;
		try {
			parsed = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		}
		Response<T> gsonResponse = Response.success(new Gson().fromJson(parsed, clazz),
				HttpHeaderParser.parseCacheHeaders(response));
		return gsonResponse;

	}

	public RetryPolicy getMyOwnDefaultRetryPolicy() {
		RetryPolicy retryPolicy = new DefaultRetryPolicy(5000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		return retryPolicy;
	}

	public static HashMap<String, String> wrapHashMap(
			HashMap<String, String> hashMap, String uri) {
		return hashMap;
	}

}
