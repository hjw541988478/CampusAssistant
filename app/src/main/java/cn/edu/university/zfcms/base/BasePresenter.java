package cn.edu.university.zfcms.base;

/**
 * Presenter
 */
public interface BasePresenter<T extends BaseView>{

    void attachView(T view);

    void detachView();
}
