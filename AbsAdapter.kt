package com.czb.wisdomcashier.view.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.czb.wisdomcashier.view.viewholder.BaseHolder

/**
 * RecyclerView.Adapter的扩展,包含headerView/footerView等
 * Created by pcf on 2017/11/22.
 */
abstract class AbsAdapter<M, VH : BaseHolder>(var context: Context?) : RecyclerView.Adapter<BaseHolder>() {
    protected var headerView: View? = null
    protected var footerView: View? = null
    var onItemClickListener: OnItemClickListener? = null

    /**
     * 获取附加View的数量,包括HeaderView和FooterView
     *
     * @return 数量
     */
    val extraViewCount: Int
        get() {
            var extraViewCount = 0
            if (headerView != null) {
                extraViewCount++
            }
            if (footerView != null) {
                extraViewCount++
            }
            return extraViewCount
        }

    /**
     * 获取顶部附加View数量,即HeaderView数量
     *
     * @return 数量
     */
    val headerExtraViewCount: Int
        get() = if (headerView == null) 0 else 1

    /**
     * 获取底部附加View数量,即FooterView数量
     *
     * @return 数量, 0或1
     */
    val footerExtraViewCount: Int
        get() = if (footerView == null) 0 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            BaseHolder(headerView!!)
        } else if (viewType == VIEW_TYPE_FOOTER) {
            BaseHolder(footerView!!)
        } else {
            createCustomViewHolder(parent, viewType)
        }
    }

    /**
     * 创建自定义的ViewHolder
     *
     * @param parent   父类容器
     * @param viewType view类型
     * @return ViewHolder
     */
    abstract fun createCustomViewHolder(parent: ViewGroup, viewType: Int): VH

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_HEADER || holder.itemViewType == VIEW_TYPE_FOOTER) {

        } else {
            holder.itemView.setOnClickListener {
                if (onItemClickListener != null) {
                    onItemClickListener!!.onItemClick(position)
                }
            }
            bindCustomViewHolder(holder as VH, position)

        }
    }

    /**
     * 绑定自定义的ViewHolder
     *
     * @param holder   ViewHolder
     * @param position 位置
     */
    abstract fun bindCustomViewHolder(holder: VH, position: Int)

    /**
     * 添加HeaderView
     *
     * @param headerView 顶部View对象
     */
    fun addHeaderView(headerView: View?) {
        if (headerView == null) {
            return
        }
        this.headerView = headerView
        notifyDataSetChanged()
    }

    /**
     * 移除HeaderView
     */
    fun removeHeaderView() {
        if (headerView != null) {
            headerView = null
            notifyDataSetChanged()
        }
    }

    /**
     * 添加FooterView
     *
     * @param footerView View对象
     */
    fun addFooterView(footerView: View?) {
        if (footerView == null) {
            return
        }
        this.footerView = footerView
        notifyDataSetChanged()
    }

    /**
     * 移除FooterView
     */
    fun removeFooterView() {
        if (footerView != null) {
            footerView = null
            notifyDataSetChanged()
        }
    }

    abstract override fun getItemId(position: Int): Long

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    companion object {

        val VIEW_TYPE_HEADER = 1024
        val VIEW_TYPE_FOOTER = 1025
        private val TAG = "AbsAdapter"
    }
}
