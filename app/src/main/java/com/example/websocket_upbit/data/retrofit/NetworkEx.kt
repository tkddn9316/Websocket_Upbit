package com.example.websocket_upbit.data.retrofit

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

fun <T : Any> Single<T>.networkThread(): Single<T> {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T : Any> Flowable<T>.networkThread(): Flowable<T> {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Maybe<T>.networkThread(): Maybe<T> {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

private fun <T : Any> Single<T>.newThread(): Single<T> {
    return subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
}

fun Completable.networkThread(): Completable {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

private fun Completable.mainThread(): Completable {
    return subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T : Any> Observable<T>.networkThread(): Observable<T> {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T : Any> Observable<T>.mainThread(): Observable<T> {
    return subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
}