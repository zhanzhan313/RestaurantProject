package com.example.myrestaurant.Controller;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.myrestaurant.R;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


public class SplashActivity extends AppCompatActivity {

    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.d(TAG, "onCreate: "+R.xml.orders);




        if (ifInOpenHour()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Next-Activity. */

                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    //finish();

                }
            }, SPLASH_DISPLAY_LENGTH);
        } else {
            Intent intent = new Intent(SplashActivity.this, ClosedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (ifInOpenHour()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Next-Activity. */

                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    //finish();

                }
            }, SPLASH_DISPLAY_LENGTH);
        } else {
            Intent intent = new Intent(SplashActivity.this, ClosedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    }

    private boolean ifInOpenHour() {
        SimpleDateFormat formatters = new SimpleDateFormat("HHmm");
        Date curDates = new Date(System.currentTimeMillis());// 获取当前时间
        String strs = formatters.format(curDates);
        int curDateInt = Integer.parseInt(strs);
        System.out.println(strs);

        int sth = 100;
        int eth = 2400;//hour
        if (sth <= curDateInt && eth >= curDateInt) {
            return true;
        } else return false;

    }

    public class DOMParser {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

        //Load and parse XML file into DOM
        public Document parse(String filePath) {
            Document document = null;
            try {
                //DOM parser instance
                DocumentBuilder builder = builderFactory.newDocumentBuilder();
                //parse an XML file into a DOM tree
                document = builder.parse(new File(filePath));
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return document;
        }


    }
    public void readOrder()
    {
        int eventType=0;
        XmlResourceParser xml = getResources().getXml(R.xml.orders);
        try {
            xml.next();
            eventType= xml.getEventType();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean inTitle = false;
        while(eventType != XmlPullParser.END_DOCUMENT) {

            //到达title节点时标记一下
            if(eventType == XmlPullParser.START_TAG) {
                if(xml.getName().equals("title")) {
                    inTitle = true;
                }
            }

            //如过到达标记的节点则取出内容
            if(eventType == XmlPullParser.TEXT && inTitle) {
                Log.d(TAG, "onCreate: "+xml.getText());

            }

            try {
                xml.next();
                eventType= xml.getEventType();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}