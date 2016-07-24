package ir.goodluckapps.vocabulary;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SendFeedbackActivity extends Activity {
	class SendFeedBackThread extends AsyncTask<Void,Void,Void>
	{
		private boolean _successfull = false;
		private String _email;
		private String _message;
		private HttpURLConnection _con;
		@Override
		protected void onPreExecute() {
			_email = ((EditText)(SendFeedbackActivity.this.findViewById(R.id.edit_text_email))).getText().toString();
			_message = ((EditText)(SendFeedbackActivity.this.findViewById(R.id.edit_text_message))).getText().toString();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
	    	try
	    	{
	    	  String query = "app=" + URLEncoder.encode("goodluckapps-vocabulary") + "&" + "inputA=" + URLEncoder.encode(_email) + "&" + "inputB=" + URLEncoder.encode(_message) ;
	          URL url = new URL("http://www.mhkhadishi.com/goodluckapps/sendfeedback");
	          _con = (HttpURLConnection)url.openConnection();
	          _con.setDoOutput(true);
	          _con.setDoInput(true);
//	          _con.setRequestProperty( "Accept-Encoding", "gzip" );
	          _con.setRequestProperty("Accept-Charset", "UTF-8");
	          _con.setRequestMethod("POST");
	          _con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
	          _con.setRequestProperty("Content-length", String.valueOf(query.length()));  

	          DataOutputStream output = new DataOutputStream(_con.getOutputStream()); 
	          output.writeBytes(query); 
	          output.close();
	          
	          BufferedReader reader = null;
	          reader = new BufferedReader(new InputStreamReader(_con.getInputStream()));
	          String line = reader.readLine();
	          if(line.equals("Done."))
	        	  _successfull = true;
	          else
	        	  _successfull = false;
	          
	          Log.d("res", line);
	    	}
	    	catch(Exception e)
	    	{
	    		try
	    		{
	    			_con.disconnect();
	    		}catch(Exception ee)
	    		{
	    			ee.printStackTrace();
		    		Log.d("inexp", ee.getMessage());					            	    			
	    		}
	    		e.printStackTrace();
	    		Log.d("exp", e.getMessage());
	    	}
			return null;
		}
		@Override
		 protected void onPostExecute(Void result) {
			try
			{
				_con.disconnect();
			}catch(Exception ee)
			{
				ee.printStackTrace();
	    		Log.d("inexp", ee.getMessage());					            	    			
			}
			if(_successfull)
				((TextView)SendFeedbackActivity.this.findViewById(R.id.text_view_serverMessage)).setText("Your message has been sent. Thank you for your feedback.");
			else
				((TextView)SendFeedbackActivity.this.findViewById(R.id.text_view_serverMessage)).setText("Sending message has been failed. Please try later.");
			
			new Handler().postDelayed(new Runnable(){
			    @Override
			    public void run() {
			    	SendFeedbackActivity.this.onBackPressed();
			    }
			}, 3000);
		}
	}
	private SendFeedBackThread _sendFeedbackThread;
	public static boolean isEmailValid(String email) {
	    boolean isValid = false;

	    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence inputStr = email;

	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches()) {
	        isValid = true;
	    }
	    return isValid;
	}
	
	@Override
	public void onBackPressed()
	{
		_sendFeedbackThread.cancel(true);
		super.onBackPressed();
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_send_feedback);
        _sendFeedbackThread = new SendFeedBackThread();
        final MediaPlayer _media_player;
        int res_id = this.getResources().getIdentifier("button_click", "raw", this.getPackageName());
        _media_player = MediaPlayer.create(this, res_id);
        Button button_send = (Button)findViewById(R.id.button_send);
        button_send.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {
	        	String email = ((EditText)(SendFeedbackActivity.this.findViewById(R.id.edit_text_email))).getText().toString();
	        	String message = ((EditText)(SendFeedbackActivity.this.findViewById(R.id.edit_text_message))).getText().toString();
	        	if(!isEmailValid(email))
	        	{
	        		final Dialog dialog = new Dialog(SendFeedbackActivity.this);
	                dialog.setContentView(R.layout.custom_dialog);
	                dialog.setTitle("Note");
	                ((TextView)dialog.findViewById(R.id.textView_term)).setText("Please enter a correct EMail.");
	                dialog.findViewById(R.id.Yes).setVisibility(View.GONE);
	                Button btn_cancel = (Button) dialog.findViewById(R.id.No);
	                btn_cancel.setText("Ok");
	                
	                btn_cancel.setOnClickListener(new View.OnClickListener() {
	        	        @Override
	        	        public void onClick(View arg0) {
	        	        	dialog.dismiss();
	        	        }
	        	    }); 
	                dialog.show();
	        	}else if(email.equals("") || message.equals(""))
	        	{
	        		final Dialog dialog = new Dialog(SendFeedbackActivity.this);
	                dialog.setContentView(R.layout.custom_dialog);
	                dialog.setTitle("Note");
	                ((TextView)dialog.findViewById(R.id.textView_term)).setText("Please fill the EMail and Message box.");
	                dialog.findViewById(R.id.Yes).setVisibility(View.GONE);
	                Button btn_cancel = (Button) dialog.findViewById(R.id.No);
	                btn_cancel.setText("Ok");
	                
	                btn_cancel.setOnClickListener(new View.OnClickListener() {
	        	        @Override
	        	        public void onClick(View arg0) {
	        	        	dialog.dismiss();
	        	        }
	        	    }); 
	                dialog.show();
	        	}
	        	else
	        	{
		        	arg0.setEnabled(false);
		        	findViewById(R.id.edit_text_email).setEnabled(false);
		        	findViewById(R.id.edit_text_message).setEnabled(false);
		        	findViewById(R.id.text_view_serverMessage).setVisibility(View.VISIBLE);
		        	((TextView)findViewById(R.id.text_view_serverMessage)).setText("Sending...");
					AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
					int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
					audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(currentVolume * 0.5), 0);
		        	_media_player.start();	        	
		        	_sendFeedbackThread.execute();
	        	}
	        }
	    }); 
	}
}
