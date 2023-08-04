package com.example.websocket_upbit.view.base

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.websocket_upbit.R
import com.example.websocket_upbit.util.FLog
import com.example.websocket_upbit.util.OnSingleClickListener
import com.example.websocket_upbit.BR

abstract class BaseActivity<VDB : ViewDataBinding, VM : BaseViewModel> :
    AppCompatActivity(), ViewModelStoreOwner, OnSingleClickListener {
    lateinit var binding: VDB
    abstract val viewModel: VM
    protected lateinit var context: Context
    protected lateinit var activity: BaseActivity<VDB, VM>
    private var lastClickTime = 0L

    protected val TAG: String by lazy {
        javaClass.simpleName
    }

    private val viewModelStores = ViewModelStore()

    protected abstract fun setup()
    protected abstract fun onCreateView(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FLog.e(TAG, "onCreate")
        context = this
        activity = this
        setup()
        onCreateView(savedInstanceState)
    }

    override fun onResume() {
        FLog.e(TAG, "onResume")
        super.onResume().apply {
            viewModel.done.observe(this@BaseActivity, this@BaseActivity::onDone)
            viewModel.error.observe(this@BaseActivity, this@BaseActivity::onError)
        }
    }

    override fun onPause() {
        FLog.e(TAG, "onPause")
        super.onPause().apply {
            viewModel.done.removeObservers(this@BaseActivity)
            viewModel.error.removeObservers(this@BaseActivity)
        }
    }

    override fun onDestroy() {
        FLog.e(TAG, "onDestroy")
        viewModel.onCleared()
        super.onDestroy().apply { viewModelStores.clear() }
    }

    open fun setBinding(@LayoutRes layoutId: Int) {
        binding = DataBindingUtil.setContentView<VDB>(this, layoutId).apply {
            setVariable(BR.viewModel, viewModel)
            setVariable(BR.view, this@BaseActivity)
            lifecycleOwner = this@BaseActivity
        }
    }

//    protected fun setBinding(@LayoutRes layoutId: Int, modelClass: Class<VM>) {
//        viewModel = FViewModelFactory.getInstance(this).create(modelClass)
//        setBinding(layoutId)
//    }

    override fun onSingleClick(v: View) {
        if (v.id == R.id.left_) {
            finish()
        } else {
            onRefresh()
        }
    }

    open fun getAdapter(): RecyclerView.Adapter<*>? {
        return null
    }

    override fun onItemClick(v: View) {

    }

    override fun onClick(v: View?) {
        val currentClickTime = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - lastClickTime
        lastClickTime = currentClickTime
        if (elapsedTime <= OnSingleClickListener.CLICK_INTERVAL) {
            return
        }
        onSingleClick(v!!)
    }

    protected fun createView(@LayoutRes res: Int): View {
        return View.inflate(applicationContext, res, null)
    }

    protected open fun onRefresh() {

    }

    protected open fun onDone(b: Boolean) {

    }

    protected open fun onError(error: Throwable) {
        FLog.e(error.message!!)
        AlertDialog.Builder(this@BaseActivity).setMessage(error.message)
            .setPositiveButton(getString(R.string.close), null)
            .show()
    }
}