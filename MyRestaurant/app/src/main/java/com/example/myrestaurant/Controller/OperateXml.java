//package com.example.myrestaurant.Controller;
//
//import android.content.res.XmlResourceParser;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//
//import com.example.myrestaurant.R;
//
//import org.w3c.dom.Document;
//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlPullParserException;
//
//import java.io.IOException;
//
//import javax.xml.parsers.DocumentBuilderFactory;
//
///**
// * Created by 站站 on 2017/12/6.
// */
//
//public class OperateXml extends AppCompatActivity {
//    int eventType;
//    private static final String TAG = "OperateXml";
//    public void readOrder()
//    {
//
////        XmlResourceParser xml = getResources().getXml(R.xml.orders);
//        try {
//            xml.next();
//
//
//            eventType= xml.getEventType();
//        } catch (XmlPullParserException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        boolean inTitle = false;
//        while(eventType != XmlPullParser.END_DOCUMENT) {
//
//            //到达title节点时标记一下
//            if(eventType == XmlPullParser.START_TAG) {
//                if(xml.getName().equals("title")) {
//                    inTitle = true;
//                }
//            }
//
//            //如过到达标记的节点则取出内容
//            if(eventType == XmlPullParser.TEXT && inTitle) {
//                Log.d(TAG, "onCreate: "+xml.getText());
//
//            }
//
//            try {
//                xml.next();
//                eventType= xml.getEventType();
//
//            } catch (XmlPullParserException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void adder()
//    {try{
//        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(R.xml.);
//        Element root = document.getDocumentElement();
//
//        Element newChild = document.createElement("project");
//        newChild.setAttribute("id", "NP001");// 添加id属性
//
//        Element nelement = document.createElement("name");// 元素节点
//        nelement.setTextContent("New Project");
//        newChild.appendChild(nelement);
//        Element selement = document.createElement("start-date");
//        selement.setTextContent("March 20 1999");
//        newChild.appendChild(selement);
//        Element eelement = document.createElement("end-date");
//        eelement.setTextContent("July 30 2004");
//        newChild.appendChild(eelement);
//
//        root.appendChild(newChild);
//    }catch (XmlPullParserException e) {
//        e.printStackTrace();
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//
//    }
//
//}
