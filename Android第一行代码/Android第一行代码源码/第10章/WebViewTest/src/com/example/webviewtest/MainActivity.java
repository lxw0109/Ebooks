package com.example.webviewtest;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		webView = (WebView) findViewById(R.id.web_view);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 根据传入的参数再去加载新的网页
				view.loadUrl(url);
				// 表示当前WebView可以处理打开新网页的请求，不用借助系统浏览器
				return true;
			}
		});
		webView.loadUrl("http://www.baidu.com");
	}

}
