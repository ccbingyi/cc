package com.bingyi.xiangju;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity {
	private SimpleAdapter adapter;
	private ArrayList<Map<String, Object>> list;
	private ListView listView;
	
	private OnItemClickListener itemListener = new OnItemClickListener() {

		private ProgressDialog dialog;

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Log.i("click", position+"");
			Log.i("click", list.get(position).toString());
			final String title = list.get(position).get("title").toString();
			dialog = ProgressDialog.show(MainActivity.this, "加载中...", "正在下载数据..."); 
			dialog.setCancelable(true);
			HttpUtil.post("http://vdn.apps.cntv.cn/api/getHttpVideoInfo.do?pid="+list.get(position).get("id"), new HttpUtil.JsonHandle() {
				
				@Override
				public void onSuccess(JSONObject json) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					Toast.makeText(MainActivity.this, "加载完成", Toast.LENGTH_SHORT).show(); 
					Log.i("json", json.toJSONString());
					String m3u8 = json.getString("hls_url");
//					Toast.makeText(MainActivity.this, m3u8, Toast.LENGTH_LONG).show();

					Toast.makeText(MainActivity.this, title, Toast.LENGTH_SHORT).show();
					Intent intent;
		        	intent = new Intent(Intent.ACTION_VIEW);
					
//					PackageManager pm = MainActivity.this.getPackageManager(); // 获得PackageManager对象  
//			        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);  
//			        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);  
//			        // 通过查询，获得所有ResolveInfo对象.  
//			        List<ResolveInfo> resolveInfos = pm  
//			                .queryIntentActivities(mainIntent, PackageManager.MATCH_DEFAULT_ONLY); 
//			        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序  
//			        Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(pm));  
//			        if (resolveInfos != null) {  
//			            for (ResolveInfo reInfo : resolveInfos) {  
//			            	String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label 
//			            	Log.i("app", appLabel);
////			            	if(appLabel.startsWith("猎豹")){
////			            		Log.i("liebao", appLabel);
////				                String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name  
////				                String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名  
////				                intent = new Intent();  
////				                intent.setComponent(new ComponentName(pkgName,  
////				                        activityName));  
////			            	}
//			            }
//			        }
//		        	intent = new Intent(); 
//	                intent.setComponent(new ComponentName("com.ijinshan.browser_fast",)); 
					//调用系统功能打开文件或者播放视频
			        intent.setDataAndType(Uri.parse(m3u8), "video/*");
			        try {				
			        	startActivity(intent);
					} catch (Exception e) {
						Toast.makeText(MainActivity.this, "没有找到可以打开的程序", Toast.LENGTH_SHORT).show();  
					}
				}
				
				@Override
				public void onError(String msg) {
					Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
					// TODO Auto-generated method stub
					
				}
			});
		}
	};
	private ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.list);
        listView.setOnItemClickListener(itemListener);

		getData();
        adapter = new SimpleAdapter(this,list,R.layout.listview,
        		new String[]{"title","time"},new int[]{R.id.list_title,R.id.list_time});
        listView.setAdapter(adapter);
        
	}

	private void getData(){
		list = new ArrayList<Map<String, Object>>();

		dialog = ProgressDialog.show(MainActivity.this, "加载中...", "正在下载数据..."); 
		dialog.setCancelable(true);
		HttpUtil.post("http://tv.xmtv.cn/2012/05/17/VIDA1337249400670445.shtml", new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String html) {
				// TODO Auto-generated method stub

				dialog.dismiss();
				Toast.makeText(MainActivity.this, "加载完成", Toast.LENGTH_SHORT).show();  
//				Log.i("json", html);
				//"http://p1.img.cctvpic.com/fmspic/2016/11/30/bbbdaa3e8e9c49ce8cb226be49963798.jpg"
				Pattern pattern = Pattern.compile("cctvpic\\.com/fmspic/\\d{4}/\\d{2}/\\d{2}/(\\w+).+?<h3>(.*?)斗阵来看戏[^\\d.]*([\\d.]+)");
				Matcher matcher = pattern.matcher(html);
				while(matcher.find()){
					String x = matcher.group(1);
					String x2 = matcher.group(2);
					String x3 = matcher.group(3);
					Map<String, Object> map = new HashMap<String, Object>();
					if(x2==null||x2.matches("\\s*")){
						x2 = "（无标题）";
					}
					map.put("title", x2);
					map.put("time", x3);
					map.put("id", x);
					list.add(map);
				}
				adapter.notifyDataSetChanged();
				listView.setSelection(0);
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}	/**
	 * 双击退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return true;
	}
	
}
