package procedo.phonegap;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements View.OnTouchListener, Handler.Callback {

    String mText = "";
    Boolean showed = false;
    Boolean clicked = false;

    private final Handler handler = new Handler(this);
    private WebView web;
    Button button;

    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout;

    private static final int CLICK_ON_WEBVIEW = 1;
    private static final int CLICK_ON_URL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        web = (WebView) findViewById(R.id.webView);
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl("file:///android_asset/codemirror2/webkit/home.html");
        web.addJavascriptInterface(new JavascriptInterface(this), "AndroidCode");
        web.requestFocus(View.FOCUS_DOWN);
        web.setOnTouchListener(this);

        button = (Button) findViewById(R.id.showHideButton);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                clicked = !clicked;
                showed = !showed;

                new Runnable() {
                    public void run() {
                        web.loadUrl("javascript:getEditorText()");
                    }
                }.run();
            }
        });
    }

    private void callShowHide() {
        //Toast.makeText(getApplicationContext(), "SHOWED STATUS: " + showed + "\n CLICKED STATUS: " + clicked, Toast.LENGTH_LONG).show();
        if(showed)
        {
            if(clicked)
            {
                hideKeyBoard();
            }
        }
        else
        {
            showKeyBoard();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class JavascriptInterface {

        private Context mCtx;

        JavascriptInterface(Context ctx) {

            mCtx = ctx;
        }

        public void toastIt(String text) {

            //Toast.makeText(mCtx, text, Toast.LENGTH_LONG).show();
        }

        public void setText(String text) {

            mText = text;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.webView && event.getAction() == MotionEvent.ACTION_DOWN){
            handler.sendEmptyMessageDelayed(CLICK_ON_WEBVIEW, 500);

            callShowHide();
        }
        return false;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == CLICK_ON_URL){
            handler.removeMessages(CLICK_ON_WEBVIEW);
            return true;
        }
        if (msg.what == CLICK_ON_WEBVIEW){
            //Toast.makeText(this, "WebView clicked", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void showKeyBoard() {
        /*InputMethodManager show = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        show.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        Toast.makeText(this, "Showed keyboard", Toast.LENGTH_SHORT).show();*/

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(web, 0);

        button.setFocusableInTouchMode(false);
        button.clearFocus();

        web.setFocusableInTouchMode(true);
        web.requestFocus(View.FOCUS_DOWN);
        web.setOnTouchListener(this);

        //button.setFocusable(false);
    }

    public void hideKeyBoard() {
        /*InputMethodManager hide = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        hide.hideSoftInputFromWindow(web.getWindowToken(),0);
        Toast.makeText(this, "Hidden keyboard", Toast.LENGTH_SHORT).show();*/

        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            web.clearFocus();
            web.setFocusableInTouchMode(false);

            button.setFocusableInTouchMode(true);
            //button.setFocusable(true);
            button.requestFocus(View.FOCUS_DOWN);
        }
    }
}
