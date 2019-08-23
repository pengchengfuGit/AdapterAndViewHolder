package com.czb.wisdomcashier.view.adapter

/**
 * Created by San on 2017/11/22.
 */

import android.content.Context
import com.czb.wisdomcashier.view.viewholder.BaseHolder

import java.util.ArrayList

/**
 * 基础的Adapter
 *
 *
 * Created by pcf on 2017/11/22.
 */
abstract class BaseAdapter<M, VH : BaseHolder> : AbsAdapter<M, VH> {


    private var dataList: MutableList<M>? = null

    private var onRefreshListener: OnRefreshListener? = null

    constructor(context: Context?) : super(context) {
        this.dataList = ArrayList()
    }

    constructor(context: Context, list: List<M>) : super(context) {
        this.dataList = ArrayList()
        this.dataList!!.addAll(list)
    }

    fun getDataList(): List<M>? {
        return dataList
    }

    /**
     * 填充数据,此操作会清除原来的数据
     *
     * @return true:填充成功并调用刷新数据
     */
    fun clearList() {
        dataList!!.clear()
        notifyDataSetChanged()
    }

    /**
     * 填充数据,此操作会清除原来的数据
     *
     * @param list 要填充的数据
     * @return true:填充成功并调用刷新数据
     */
    fun fillList(list: List<M>?): Boolean {
        dataList!!.clear()
        if (list != null && list.size > 0) {
            val result = dataList!!.addAll(list)
        }
        notifyDataSetChanged()
        //  if (result) {
        // }
        return true
    }

    /**
     * 追加一条数据
     *
     * @param data 要追加的数据
     * @return true:追加成功并刷新界面
     */
    fun appendItem(data: M): Boolean {
        val result = dataList!!.add(data)
        if (result) {
            if (headerExtraViewCount == 0) {
                notifyItemInserted(dataList!!.size - 1)
            } else {
                notifyItemInserted(dataList!!.size)
            }
        }
        return result
    }

    /**
     * 追加集合数据
     *
     * @param list 要追加的集合数据
     * @return 追加成功并刷新
     */
    fun appendList(list: List<M>): Boolean {
        val result = dataList!!.addAll(list)
        if (result) {
            notifyDataSetChanged()
        }
        return result
    }

    /**
     * 在最顶部前置数据
     *
     * @param data 要前置的数据
     */
    fun proposeItem(data: M) {
        dataList!!.add(0, data)
        if (headerExtraViewCount == 0) {
            notifyItemInserted(0)
        } else {
            notifyItemInserted(headerExtraViewCount)
        }
    }

    /**
     * 在顶部前置数据集合
     *
     * @param list 要前置的数据集合
     */
    fun proposeList(list: List<M>) {
        dataList!!.addAll(0, list)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return if (headerView != null && position == 0) {
            AbsAdapter.Companion.VIEW_TYPE_HEADER
        } else if (footerView != null && position == dataList!!.size + headerExtraViewCount) {
            AbsAdapter.Companion.VIEW_TYPE_FOOTER
        } else {
            getCustomViewType(position)
        }
    }

    abstract override fun bindCustomViewHolder(holder: VH, position: Int)

    /**
     * 获取自定义View的类型
     *
     * @param position 位置
     * @return View的类型
     */
    abstract fun getCustomViewType(position: Int): Int

    override fun getItemCount(): Int {
        return dataList!!.size + extraViewCount
    }

    /**
     * 根据位置获取一条数据
     *
     * @param position View的位置
     * @return 数据
     */
    fun getItem(position: Int): M? {
        if (headerView != null && position == 0 || position >= dataList!!.size + headerExtraViewCount) {
            return null
        }
        return if (headerView == null) dataList!![position] else dataList!![position - 1]
    }

    /**
     * 根据ViewHolder获取数据
     *
     * @param holder ViewHolder
     * @return 数据
     */
    fun getItem(holder: VH): M? {
        return getItem(holder.adapterPosition)
    }

    fun updateItem(data: M) {
        val index = dataList!!.indexOf(data)
        if (index < 0) {
            return
        }
        dataList!![index] = data
        if (headerView == null) {
            notifyItemChanged(index)
        } else {
            notifyItemChanged(index + 1)
        }
        notifyDataSetChanged()
    }

    /**
     * 移除一条数据
     *
     * @param position 位置
     */
    fun removeItem(position: Int) {
        if (headerView == null) {
            dataList!!.removeAt(position)
        } else {
            dataList!!.removeAt(position - 1)
        }
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    /**
     * 移除一条数据
     *
     * @param data 要移除的数据
     */
    fun removeItem(data: M) {
        val index = dataList!!.indexOf(data)
        if (index < 0) {
            return
        }
        dataList!!.removeAt(index)
        if (headerView == null) {
            notifyItemRemoved(index)
        } else {
            notifyItemRemoved(index + 1)
        }
        notifyDataSetChanged()
    }

    interface OnRefreshListener {
        fun refresh()
    }

    fun onRefresh() {
        if (onRefreshListener != null) {
            onRefreshListener!!.refresh()
        }
    }

    fun setOnRefreshListener(refreshListener: OnRefreshListener) {
        this.onRefreshListener = refreshListener
    }
}
