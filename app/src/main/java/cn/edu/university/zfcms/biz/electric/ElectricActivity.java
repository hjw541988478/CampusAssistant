package cn.edu.university.zfcms.biz.electric;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.Bind;
import cn.edu.university.zfcms.R;
import cn.edu.university.zfcms.base.BaseToolbarActivity;
import cn.edu.university.zfcms.data.electric.ElectricRequestModel;
import cn.edu.university.zfcms.model.ElectricCharge;

/**
 * Created by hjw on 2016/04/18 0018.
 */
public class ElectricActivity extends BaseToolbarActivity implements ElectricChargeContract.View{
    @Bind(R.id.inquiry_electric_charge)
    Button inquiryElectricCharge;
    @Bind(R.id.inquiry_electric_checkcode)
    ImageView inquiryElectricCheckCode;
    @Bind(R.id.inquiry_electric_text)
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
        inquiryElectricCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadElectricInquiryResult(inquiryElectricText.getText().toString());
            }
        });
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
    public void showInquiryError(String msg) {
        showToast(msg);
    }

    @Override
    public void showLoading(boolean visible) {

    }

    @Override
    public void showCheckcode(Bitmap bitmap) {
        inquiryElectricCheckCode.setImageBitmap(bitmap);
    }

    @Override
    public void setPresenter(ElectricChargeContract.Presenter presenter) {

    }
}
