package com.yoyiyi.soleil.base

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import com.yoyiyi.soleil.R
import com.yoyiyi.soleil.utils.AppUtils
import com.yoyiyi.soleil.widget.ProgressWheel
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import java.util.*

/**
 * @author zzq  作者 E-mail:   soleilyoyiyi@gmail.com
 *
 * @date 创建时间：2017/6/5 22:43
 * 描述:基础刷新Activity
 */
abstract class BaseRefreshActivity<T : BaseContract.BasePresenter<*>, K> : BaseActivity<T>(), SwipeRefreshLayout.OnRefreshListener {
    protected var mRecycler: RecyclerView? = null
    protected var mRefresh: SwipeRefreshLayout? = null
    protected var mIsRefreshing = false
    protected var mList: MutableList<K>? = ArrayList()
    private var mLoading: ProgressWheel? = null

    protected fun initRefreshLayout() {
        mRefresh?.let {
            mRefresh?.setColorSchemeResources(R.color.colorPrimary)
            mRecycler?.post {
                mRefresh?.isRefreshing = true
                loadData()
            }
            mRefresh?.setOnRefreshListener(this)
        }
    }


    override fun initWidget() {
        mRefresh = find<SwipeRefreshLayout>(R.id.refresh)
        mRecycler = find<RecyclerView>(R.id.recycler)
        mLoading = find<ProgressWheel>(R.id.pw_loading)
        initRefreshLayout()
        initRecyclerView()
    }

    override fun onRefresh() {
        clearData()
        loadData()
    }

    protected fun clearData() {
        mIsRefreshing = true
    }

    override fun complete() {
        super.complete()
        AppUtils.runOnUIDelayed({ mRefresh?.isRefreshing = false }, 650)
        if (mIsRefreshing) {
            mList?.clear()
            clear()
            toast("刷新成功")
        }
        mIsRefreshing = false
        mLoading?.visibility = View.GONE
    }

    protected fun clear() {

    }

    override fun initDatas() {
        mRefresh?.let {
            mLoading?.let {
                mLoading?.visibility = View.VISIBLE
                AppUtils.runOnUIDelayed({ loadData() }, 650)
            } ?: super.initDatas()

        }
    }
}
