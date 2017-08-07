package com.bingyi.xiangju;

import org.apache.http.Header;
import com.alibaba.fastjson.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
 
@SuppressWarnings("deprecation")
public class HttpUtil {
    private static AsyncHttpClient client =new AsyncHttpClient();    //实例话对象
    static
    {
        client.setTimeout(1000*180);   //设置链接超时180s，如果不设置，默认为10s
    }
    public static void post(String urlString,AsyncHttpResponseHandler res)    //用一个完整url获取一个string对象
    {
        client.post(urlString, res);
    }
    public static void post(String urlString,RequestParams params,AsyncHttpResponseHandler res)   //url里面带参数
    {
        client.post(urlString, params,res);
    }
    public static void post(String urlString,JsonHttpResponseHandler res)   //不带参数，获取json对象或者数组
    {
        client.post(urlString, res);
    }
    public static void post(String urlString,RequestParams params,JsonHttpResponseHandler res)   //带参数，获取json对象或者数组
    {
        client.post(urlString, params,res);
    }
    public static void post(String uString, BinaryHttpResponseHandler bHandler)   //下载数据使用，会返回byte数据
    {
        client.post(uString, bHandler);
    }
    public static AsyncHttpClient postClient()
    {
        return client;
    }
    public static abstract class JsonHandle extends BaseJsonHttpResponseHandler<JSONObject>{
    	public abstract void onSuccess(JSONObject json);
    	public abstract void onError(String msg);
		@Override
		public void onFailure(int arg0, Header[] arg1, Throwable arg2, String arg3, JSONObject arg4) {
			
			
		}

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2, JSONObject json) {
			onSuccess(json);
			
		}

		@Override
		protected JSONObject parseResponse(String arg0, boolean arg1) throws Throwable {
			return JSONObject.parseObject(arg0);
		}
		
	}
}
