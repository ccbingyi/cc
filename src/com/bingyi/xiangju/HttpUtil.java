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
    private static AsyncHttpClient client =new AsyncHttpClient();    //ʵ��������
    static
    {
        client.setTimeout(1000*180);   //�������ӳ�ʱ180s����������ã�Ĭ��Ϊ10s
    }
    public static void post(String urlString,AsyncHttpResponseHandler res)    //��һ������url��ȡһ��string����
    {
        client.post(urlString, res);
    }
    public static void post(String urlString,RequestParams params,AsyncHttpResponseHandler res)   //url���������
    {
        client.post(urlString, params,res);
    }
    public static void post(String urlString,JsonHttpResponseHandler res)   //������������ȡjson�����������
    {
        client.post(urlString, res);
    }
    public static void post(String urlString,RequestParams params,JsonHttpResponseHandler res)   //����������ȡjson�����������
    {
        client.post(urlString, params,res);
    }
    public static void post(String uString, BinaryHttpResponseHandler bHandler)   //��������ʹ�ã��᷵��byte����
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
