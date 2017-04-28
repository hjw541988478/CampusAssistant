package cn.edu.university.zfcms.presenter.electric;

import android.graphics.BitmapFactory;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

import cn.edu.university.zfcms.base.contract.ElectricContract;
import cn.edu.university.zfcms.model.entity.ElectricRequestModel;
import cn.edu.university.zfcms.model.entity.ElectricCharge;
import cn.edu.university.zfcms.model.network.BizServiceManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * Created by hjw on 17/4/25.
 */
public class ElectricPresenter implements ElectricContract.Presenter {

    private ElectricContract.View electricView;

    public ElectricPresenter(ElectricContract.View electricView) {
        this.electricView = electricView;
        this.electricView.setPresenter(this);
    }

    @Override
    public void start() {
        loadCheckCode();
    }

    @Override
    public void loadElectricInquiryResult(final String checkCode) {
        BizServiceManager.getInstance().getElectricService()
                // 加载验证码
                .loadElectricCheckCode()
                .map(s -> parseElectricRequestModel(s, checkCode))
                .flatMap((requestModel) ->
                        // 请求电费信息HTML
                        BizServiceManager.getInstance().getElectricService()
                                .loadElectricInquiryResult(buildElectricInquiryParams(requestModel))

                )
                .compose(electricView.bindToLifecycle())

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((s, throwable) -> {
                    if (throwable != null) {
                        electricView.showInquiryError("electric inquiry failure");
                    } else {
                        Log.d(TAG, "loadElectricInquiryResult() called with: checkCode = [" + checkCode + "]");
                        Log.d(TAG, "loadElectricInquiryResult success :" + s);
                        electricView.showInquirySuccess(new ElectricCharge());
                    }
                });
    }

    /**
     * 解析header
     * @param doc
     * @param key
     * @return
     */
    private String parseParamsHeader(Document doc, String key) {
        Elements elements = doc.getElementsByTag("input");
        for (Element element : elements) {
            if (key.equals(element.attr("name"))) {
                return element.val();
            }
        }
        return "";
    }

    private ElectricRequestModel parseElectricRequestModel(String html, String checkCode) {
        Document document = Jsoup.parse(html);
        ElectricRequestModel model = new ElectricRequestModel();
        model.roomCheckCode = checkCode;
        model.aspxTreeState = parseParamsHeader(document, "ASPxPopupControl1$ASPxTreeList1$STATE");
        model.aspxTreeKey = parseParamsHeader(document, "ASPxPopupControl1$ASPxTreeList1$FKey");
        model.aspxWs = parseParamsHeader(document, "ASPxPopupControl1WS");
        model.aspxViewState = parseParamsHeader(document, "__VIEWSTATE");
        model.aspxValidation = parseParamsHeader(document, "__EVENTVALIDATION");
        return model;
    }


    @Override
    public void loadCheckCode() {
        BizServiceManager.getInstance().getElectricService()
                .loadElectricCheckCodeBytes()
                .map(responseBody -> BitmapFactory.decodeStream(responseBody.byteStream()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((bitmap, throwable) -> {
                    if (throwable != null) {
                        electricView.showInquiryError("加载验证码失败");
                    } else {
                        electricView.showCheckCode(bitmap);
                    }
                });
    }

    /**
     * 填写必要的Post参数
     * @param model
     * @return
     */
    private Map<String, String> buildElectricInquiryParams(ElectricRequestModel model) {
        Map<String, String> params = new HashMap<>();
        params.put("ScriptManager1", "UpdatePanel1|btnLogin");
        params.put("ASPxRadioButtonList1", "0");
        params.put("ASPxRadioButtonList1_RB", "");
        params.put("ASPxButtonEdit1", model.roomLocation);
        params.put("ASPxTextBox2", model.roomPswd);
        params.put("ASPxTextBox2$CVS", "");
        params.put("txtCheck", model.roomCheckCode);
        params.put("Hidden1", "");
        params.put("ASPxPopupControl1WS", model.aspxWs);
        params.put("ASPxPopupControl1$ASPxTreeList1$STATE", model.aspxTreeState);
        params.put("ASPxPopupControl1$ASPxTreeList1$FKey", model.aspxTreeKey);
        params.put("ASPxPopupControl1$ASPxTreeList1$EV", model.aspxTreeEv);
        params.put("txtRandom", "");
        params.put("Digest", "");
        params.put("SN_SERAL", "");
        params.put("fbxy", "2560|1440");
        params.put("DXScript", "");
        params.put("__EVENTTARGET", "");
        params.put("__EVENTARGUMENT", "");
        params.put("__VIEWSTATE", model.aspxViewState);
        params.put("__EVENTVALIDATION", model.aspxValidation);
        params.put("__ASYNCPOST", "true");
        params.put("btnLogin", "");
        return params;
    }

}
