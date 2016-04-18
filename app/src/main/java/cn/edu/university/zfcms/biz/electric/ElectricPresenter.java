package cn.edu.university.zfcms.biz.electric;

import android.graphics.Bitmap;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.university.zfcms.biz.login.LoginContract;
import cn.edu.university.zfcms.data.electric.ElectricRequestModel;
import cn.edu.university.zfcms.data.login.LoginDataRepo;
import cn.edu.university.zfcms.data.login.LoginDataSource;
import cn.edu.university.zfcms.data.login.local.LocalLoginDataSource;
import cn.edu.university.zfcms.data.login.remote.RemoteLoginDataSource;
import cn.edu.university.zfcms.http.HttpManager;
import cn.edu.university.zfcms.model.User;

/**
 * Created by hjw on 16/4/15.
 */
public class ElectricPresenter implements ElectricChargeContract.Presenter {

    private ElectricChargeContract.View electricView;

    public ElectricPresenter(ElectricChargeContract.View electricView){
        this.electricView = electricView;
        this.electricView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadElectricInquiryResult(String checkCode) {
        HttpPost post = new HttpPost("http://218.75.197.120:8021/XSCK/Login_Students.aspx");
        Map<String,String> headers = buildElectricInquiryHeaders();
        for (Map.Entry<String,String> entry: headers.entrySet()) {
            post.setHeader(entry.getKey(),entry.getValue());
        }
        List<BasicNameValuePair> pairs = new ArrayList<>();
        Map<String,String> params = buildElectricInquiryParams(buildRequestMode(checkCode));
        for (Map.Entry<String , String> entry : params.entrySet()) {
            pairs.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
        }
        try {
            post.setEntity(new UrlEncodedFormEntity(pairs, "utf-8"));
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                Log.d("electric" , EntityUtils.toString(response.getEntity()));
            } else  {
                Log.d("electric" , "electric inquiry failure : "+ response.getStatusLine().getStatusCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseEletricRequestModel(String html , ElectricRequestModel model) {
        Document document = Jsoup.parse(html);
        model.aspxTreeEv = document.getElementById("ASPxPopupControl1$ASPxTreeList1$STATE").val();
    }
    private ElectricRequestModel buildRequestMode(String checkCode){
        ElectricRequestModel model = new ElectricRequestModel();
        model.roomCheckCode = checkCode;
        HttpGet get = new HttpGet("http://218.75.197.120:8021/XSCK/Login_Students.aspx");
        try {
            HttpResponse resp = client.execute(get);
            if (resp.getStatusLine().getStatusCode() == 200) {
                parseEletricRequestModel(EntityUtils.toString(resp.getEntity(),"utf-8"),model);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return model;
    }

    @Override
    public void loadCheckcode() {

    }

    private static HttpClient client = new DefaultHttpClient();

    private Map<String,String> buildElectricInquiryParams(ElectricRequestModel model){
        Map<String,String> params = new HashMap<>();
        params.put("ScriptManager1","UpdatePanel1|btnLogin");
        params.put("ASPxRadioButtonList1","0");
        params.put("ASPxRadioButtonList1_RB","");
        params.put("ASPxButtonEdit1",model.roomLocation);
        params.put("ASPxTextBox2",model.roomPswd);
        params.put("ASPxTextBox2$CVS","");
        params.put("txtCheck",model.roomCheckCode);
        params.put("Hidden1","");
        params.put("ASPxPopupControl1WS",model.aspxWs);
        params.put("ASPxPopupControl1$ASPxTreeList1$STATE",model.aspxTreeState);
        params.put("ASPxPopupControl1$ASPxTreeList1$FKey",model.aspxTreeKey);
        params.put("ASPxPopupControl1$ASPxTreeList1$EV",model.aspxTreeEv);
        params.put("txtRandom","");
        params.put("Digest","");
        params.put("SN_SERAL","");
        params.put("fbxy","2560|1440");
        params.put("DXScript","");
        params.put("__EVENTTARGET","");
        params.put("__EVENTARGUMENT","");
        params.put("__VIEWSTATE",model.aspxViewState);
        params.put("__EVENTVALIDATION",model.aspxValidation);
        params.put("__ASYNCPOST","true");
        params.put("btnLogin","");
        return params;
    }

    private Map<String,String> buildElectricInquiryHeaders(){
        Map<String,String> preHeaders = new HashMap<>();
        preHeaders.put("Host","218.75.197.120:8021");
        preHeaders.put("Connection","keep-alive");
        preHeaders.put("Cache-Control","no-cache");
        preHeaders.put("Origin","http://218.75.197.120:8021");
        preHeaders.put("X-MicrosoftAjax","Delta=true");
        preHeaders.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.75 Safari/537.36");
        preHeaders.put("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        preHeaders.put("Accept","*/*");
        preHeaders.put("DNT","1");
        preHeaders.put("Referer","http://218.75.197.120:8021/XSCK/Login_Students.aspx");
        preHeaders.put("Accept-Encoding","gzip, deflate");
        preHeaders.put("Accept-Language","zh-CN,zh;q=0.8,en;q=0.6");
        return preHeaders;
    }

}
