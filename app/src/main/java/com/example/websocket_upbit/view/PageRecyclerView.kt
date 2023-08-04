package com.example.websocket_upbit.view

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.websocket_upbit.util.FOnListChangedCallBack
import com.example.websocket_upbit.view.base.FObservable

class PageRecyclerView : RecyclerView {
    private var observable: FObservable<*>? = null
    lateinit var smoothScroller: LinearSmoothScroller
    private var listCallbackF: FOnListChangedCallBack<*>? = null
    private var page: Int = 1

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        itemAnimator = DefaultItemAnimator()
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == SCROLL_STATE_IDLE) {
                    if (!recyclerView.canScrollVertically(SCROLL_STATE_DRAGGING)) {
                        recyclerView.tag?.let {
                            observable?.getData(recyclerView.tag)
                        }
                    }
                }
            }
        })

        smoothScroller = object : LinearSmoothScroller(context) {
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return 120f / displayMetrics.densityDpi
            }
        }

        listCallbackF = object : FOnListChangedCallBack<ObservableList<*>>() {
            override fun onItemRangeInserted(
                sender: ObservableList<*>,
                positionStart: Int,
                itemCount: Int,
            ) {
                if (itemCount > 0) {
//                    if (page > 1) startSmoothScroll(itemCount)
                    observable?.let { fObservable ->
                        tag = 1.let {
                            page += it
                            page
                        }
//                        fObservable.isNoData(false)
                    }
                } else {
                    tag = null
                    isEmpty()
                }
            }


            override fun onItemRangeRemoved(
                sender: ObservableList<*>,
                positionStart: Int,
                itemCount: Int,
            ) {
                isEmpty()
                adapter?.notifyDataSetChanged()
            }
        }
    }

    fun onClear() {
        page = 1
        observable?.onClear()
    }

    private fun isEmpty() {
//        observable?.isNoData(adapter?.itemCount == 0)
    }

    fun setObservable(observable: FObservable<*>) {
        this.observable = observable
        addOnListChangedCallback()
    }

    private fun addOnListChangedCallback() {
        observable!!.getObservableArrayList().addOnListChangedCallback(listCallbackF)
    }

    private fun removeOnListChangedCallback() {
        observable!!.getObservableArrayList().removeOnListChangedCallback(listCallbackF)
    }

    private fun startSmoothScroll(itemCount: Int) {
        adapter?.let {
            if (it.itemCount > 0 && it.itemCount - itemCount >= 0) {
                smoothScroller.targetPosition = it.itemCount + 1
                try {
                    handler.postDelayed({ layoutManager?.startSmoothScroll(smoothScroller) }, 250)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    override fun onDetachedFromWindow() {
        observable?.let { removeOnListChangedCallback() }
        super.onDetachedFromWindow()
    }
}