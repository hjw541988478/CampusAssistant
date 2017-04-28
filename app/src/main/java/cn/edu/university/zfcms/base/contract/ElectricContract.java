package cn.edu.university.zfcms.base.contract;

import android.graphics.Bitmap;

import cn.edu.university.zfcms.base.BasePresenter;
import cn.edu.university.zfcms.base.BaseView;
import cn.edu.university.zfcms.model.entity.ElectricCharge;

/**
 * Created by hjw on 2016/04/18 0018.
 */
public interface ElectricContract {
    interface View extends BaseView<Presenter>{
        void showInquirySuccess(ElectricCharge electricCharge);
        void showInquiryError(String msg);
        void showLoading(boolean visible);
        void showCheckCode(Bitmap bitmap);
    }

    interface Presenter extends BasePresenter {
        void loadElectricInquiryResult(String checkCode);
        void loadCheckCode();
    }
}
