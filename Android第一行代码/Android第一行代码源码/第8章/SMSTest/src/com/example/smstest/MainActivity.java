package com.example.smstest;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private TextView sender;

	private TextView content;

	private EditText to;

	private EditText msgInput;

	private Button send;

	private IntentFilter receiveFilter;

	private MessageReceiver messageReceiver;

	private IntentFilter sendFilter;

	private SendStatusReceiver sendStatusReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sender = (TextView) findViewById(R.id.sender);
		content = (TextView) findViewById(R.id.content);
		to = (EditText) findViewById(R.id.to);
		msgInput = (EditText) findViewById(R.id.msg_input);
		send = (Button) findViewById(R.id.send);
		receiveFilter = new IntentFilter();
		receiveFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		receiveFilter.setPriority(100);
		messageReceiver = new MessageReceiver();
		registerReceiver(messageReceiver, receiveFilter);
		sendFilter = new IntentFilter();
		sendFilter.addAction("SENT_SMS_ACTION");
		sendStatusReceiver = new SendStatusReceiver();
		registerReceiver(sendStatusReceiver, sendFilter);
		send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SmsManager smsManager = SmsManager.getDefault();
				Intent sentIntent = new Intent("SENT_SMS_ACTION");
				PendingIntent pi = PendingIntent.getBroadcast(
						MainActivity.this, 0, sentIntent, 0);
				smsManager.sendTextMessage(to.getText().toString(), null,
						msgInput.getText().toString(), pi, null);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(messageReceiver);
		unregisterReceiver(sendStatusReceiver);
	}

	class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			Object[] pdus = (Object[]) bundle.get("pdus");
			SmsMessage[] messages = new SmsMessage[pdus.length];
			for (int i = 0; i < messages.length; i++) {
				messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
			}
			String address = messages[0].getOriginatingAddress();
			String fullMessage = "";
			for (SmsMessage message : messages) {
				fullMessage += message.getMessageBody();
			}
			sender.setText(address);
			content.setText(fullMessage);
			abortBroadcast();
		}

	}

	class SendStatusReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (getResultCode() == RESULT_OK) {
				Toast.makeText(context, "Send succeeded", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(context, "Send failed", Toast.LENGTH_LONG)
						.show();
			}
		}

	}

}
