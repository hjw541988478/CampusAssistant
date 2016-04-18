package cn.edu.university.zfcms.biz.electric;

import android.graphics.Bitmap;

import cn.edu.university.zfcms.base.mvp.BasePresenter;
import cn.edu.university.zfcms.base.mvp.BaseView;
import cn.edu.university.zfcms.data.electric.ElectricRequestModel;
import cn.edu.university.zfcms.model.ElectricCharge;

/**
 * Created by hjw on 2016/04/18 0018.
 */
public interface ElectricChargeContract {
    interface View extends BaseView<Presenter>{
        void showInquirySuccess(ElectricCharge electricCharge);
        void showInquiryError(String msg);
        void showLoading(boolean visible);
        void showCheckcode(Bitmap bitmap);
    }

    interface Presenter extends BasePresenter {
        void loadElectricInquiryResult(String chcekCode);
        void loadCheckcode();
    }
}
