package cn.edu.university.zfcms.ui.electric;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import cn.edu.university.zfcms.R;
import cn.edu.university.zfcms.base.BaseToolbarActivity;
import cn.edu.university.zfcms.base.contract.ElectricContract;
import cn.edu.university.zfcms.presenter.electric.ElectricPresenter;
import cn.edu.university.zfcms.model.entity.ElectricCharge;

/**
 * Created by hjw on 2016/04/18 0018.
 */
public class ElectricActivity extends BaseToolbarActivity implements ElectricContract.View{
    @BindView(R.id.inquiry_electric_charge)
    Button inquiryElectricCharge;
    @BindView(R.id.inquiry_electric_checkcode)
    ImageView inquiryElectricCheckCode;
    @BindView(R.id.inquiry_electric_text)
    EditText inquiryElectricText;

    ElectricPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_electric;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {
        RxView.clicks(inquiryElectricCharge)
                .throttleFirst(1333 , TimeUnit.MILLISECONDS)
                .subscribe(o -> presenter
                        .loadElectricInquiryResult(inquiryElectricText.getText().toString()));
    }

    @Override
    protected void initData() {
        presenter = new ElectricPresenter(this);
    }

    @Override
    public void showInquirySuccess(ElectricCharge electricCharge) {
        showToast(electricCharge.balance);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void showInquiryError(String msg) {
        showToast(msg);
    }

    @Override
    public void showLoading(boolean visible) {

    }

    @Override
    public void showCheckCode(Bitmap bitmap) {
        inquiryElectricCheckCode.setImageBitmap(bitmap);
    }

    @Override
    public void setPresenter(ElectricContract.Presenter presenter) {

    }
}
