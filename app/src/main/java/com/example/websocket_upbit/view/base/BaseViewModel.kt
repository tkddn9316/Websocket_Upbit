package com.example.websocket_upbit.view.base

import android.app.Application
import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.websocket_upbit.util.FLog
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private val compositeDisposable = CompositeDisposable()
    val loading = ObservableBoolean(false)
    val title = ObservableField("")

    /** 완료 여부 */
    val done = MutableLiveData<Boolean>()

    /** 에러 감지 */
    val error = SingleLiveEvent<Throwable>()

    /** AppBar BackButton */
    val back = ObservableBoolean(true)

    /** AppBar RefreshButton */
    val refresh = ObservableBoolean(true)

    /** RxJava 통신을 위한 함수 */
    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    public override fun onCleared() {
        super.onCleared()
        FLog.e(javaClass.simpleName, "onCleared")
        compositeDisposable.clear()
    }

    fun getContext(): Context {
        return getApplication<Application>().applicationContext
    }

    fun <T : Any> Single<T>.networkThread(loading: Consumer<Boolean>? = null): Single<T> {
        return subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                AndroidSchedulers.mainThread().scheduleDirect { loading?.accept(true) }
            }
            .doOnError { }
            .doFinally { loading?.accept(false) }
    }

    fun <T : Any> Flowable<T>.networkThread(loading: Consumer<Boolean>? = null): Flowable<T> {
        return subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                AndroidSchedulers.mainThread().scheduleDirect { loading?.accept(true) }
            }
            .doOnError { }
            .doFinally { loading?.accept(false) }
    }

    fun Completable.networkThread(): Completable {
        return subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}