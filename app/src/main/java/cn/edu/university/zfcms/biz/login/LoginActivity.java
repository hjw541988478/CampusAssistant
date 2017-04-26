package cn.edu.university.zfcms.biz.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.Bind;
import cn.edu.university.zfcms.R;
import cn.edu.university.zfcms.base.ui.BaseToolbarActivity;
import cn.edu.university.zfcms.biz.courses.CoursesActivity;
import cn.edu.university.zfcms.model.User;

public class LoginActivity extends BaseToolbarActivity implements LoginContract.View {

    private static final String tag = LoginActivity.class.getSimpleName();

    private LoginPresenter loginPresenter;

    @Bind(R.id.username)
    EditText mUsernameView;
    @Bind(R.id.password)
    EditText mPasswordView;
    @Bind(R.id.checkcode)
    EditText mCheckCodeTextView;
    @Bind(R.id.checkcode_img)
    ImageView mCheckCodeView;
    @Bind(R.id.btn_sign_in)
    Button mSignIn;
    @Bind(R.id.login_progress)
    View mProgressView;
    @Bind(R.id.code_loading_progress)
    View mCodeLoadingView;
    @Bind(R.id.login_form)
    View mLoginFormView;
    @Bind(R.id.checkcode_container)
    View mCheckcodeViewGroup;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        hideBack();
        showBaasLoginView();
    }

    @Override
    protected void initListeners() {
        mCheckCodeView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                reloadCheckCode();
                return true;
            }
        });
        mSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    @Override
    protected void initData() {
        loginPresenter = new LoginPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 自动加载验证码
        loginPresenter.start();
    }

    private void reloadCheckCode() {
        showLoadingCheckCodeView(true);
        loginPresenter.reloadCheckCode();
    }

    private void attemptLogin() {
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mCheckCodeTextView.setError(null);

        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String checkcode = mCheckCodeTextView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError("学号不能为空!");
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError("学号应该是10位!");
            focusView = mUsernameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("密码不能为空!");
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError("密码不能过短!");
            focusView = mPasswordView;
            cancel = true;
        }

        if (!isBassLoginType()) {
            if (TextUtils.isEmpty(checkcode)) {
                mCheckCodeTextView.setError("验证码不能为空!");
                focusView = mCheckCodeTextView;
                cancel = true;
            } else if (!isCheckCodeValid(checkcode)) {
                mCheckCodeTextView.setError("验证码为四位!");
                focusView = mCheckCodeTextView;
                cancel = true;
            }
        }
        if (cancel) {
            showFocusView(focusView);
        } else {
            showLogingIndicatorView(true);
            User loginUser = new User();
            loginUser.userId = mUsernameView.getText().toString().trim();
            loginUser.userPswd = mPasswordView.getText().toString().trim();
            loginUser.setUsername(loginUser.userId);
            loginUser.setPassword(loginUser.userPswd);
            String checkCode = mCheckCodeTextView.getText().toString().trim();
            if (isBassLoginType()) {
                loginPresenter.login(loginUser);
            } else {
                loginPresenter.login(loginUser, checkCode);
            }
        }
    }


    private boolean isUsernameValid(String username) {
        return username.trim().length() == 10;
    }

    private boolean isPasswordValid(String password) {
        return password.trim().length() >= 5;
    }

    private boolean isCheckCodeValid(String checkcode) {
        return checkcode.trim().length() == 4;
    }

    @Override
    public void showLoginSuccessfulView(User user) {
        showToast("登录成功,欢迎:" + user.toString());
        showLogingIndicatorView(false);
        CoursesActivity.launch(this, CoursesActivity.TYPE_DAY_COURSES);
    }

    @Override
    public void showLoginErrorView(String msg) {
        showToast(msg);
        showLogingIndicatorView(false);
        reloadCheckCode();
    }

    @Override
    public void resetLoginForm() {
        mUsernameView.setText("");
        mPasswordView.setText("");
        mCheckCodeTextView.setText("");
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mCheckCodeTextView.setError(null);
        loginPresenter.reloadCheckCode();
    }

    @Override
    public void showBaasLoginView() {
        showLogingIndicatorView(false);
        mCheckcodeViewGroup.setVisibility(View.GONE);
    }

    @Override
    public void showZfsoftLoginView() {
        showLogingIndicatorView(false);
        mCheckcodeViewGroup.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isBassLoginType() {
        return mCheckcodeViewGroup.getVisibility() == View.GONE;
    }

    @Override
    public void showCheckCodeView(Bitmap bitmap) {
        mCheckCodeView.setImageBitmap(bitmap);
        showLoadingCheckCodeView(false);
    }

    @Override
    public void showErrLoadCheckCode(String msg) {
        showToast(msg);
        showLoadingCheckCodeView(false);
    }

    @Override
    public void showFocusView(View focusView) {
        focusView.requestFocus();
    }

    @Override
    public void showLoadingCheckCodeView(boolean isShow) {
        if (isShow) {
            mCodeLoadingView.setVisibility(View.VISIBLE);
            mCheckCodeView.setEnabled(false);
        } else {
            mCodeLoadingView.setVisibility(View.GONE);
            mCheckCodeView.setEnabled(true);
        }
    }

    @Override
    public void showLogingIndicatorView(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {

    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public Context getLongLifeCycleContext() {
        return getApplicationContext();
    }
}

