package com.example.myapplicationaaa1.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.View.OnLongClickListener


class ItemClickSupport private constructor(
    private val mRecyclerView: RecyclerView,
    private val mItemID: Int
) {
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null
    private val mOnClickListener = object : View.OnClickListener {
        override fun onClick(v: View) {
            if (mOnItemClickListener != null) {
                val holder = mRecyclerView.getChildViewHolder(v)
                mOnItemClickListener!!.onItemClicked(mRecyclerView, holder.adapterPosition, v)
            }
        }
    }
    private val mOnLongClickListener = object : View.OnLongClickListener {
        override fun onLongClick(v: View): Boolean {
            if (mOnItemLongClickListener != null) {
                val holder = mRecyclerView.getChildViewHolder(v)
                return mOnItemLongClickListener!!.onItemLongClicked(
                    mRecyclerView,
                    holder.adapterPosition,
                    v
                )
            }
            return false
        }
    }
    private val mAttachListener = object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            if (mOnItemClickListener != null) {
                view.setOnClickListener(mOnClickListener)
            }
            if (mOnItemLongClickListener != null) {
                view.setOnLongClickListener(mOnLongClickListener)
            }
        }

        override fun onChildViewDetachedFromWindow(view: View) {

        }
    }

    init {
        mRecyclerView.setTag(mItemID, this)
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener)
    }

    fun setOnItemClickListener(listener: OnItemClickListener): ItemClickSupport {
        mOnItemClickListener = listener
        return this
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener): ItemClickSupport {
        mOnItemLongClickListener = listener
        return this
    }

    private fun detach(view: RecyclerView) {
        view.removeOnChildAttachStateChangeListener(mAttachListener)
        view.setTag(mItemID, null)
    }

    interface OnItemClickListener {

        fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View)
    }

    interface OnItemLongClickListener {

        fun onItemLongClicked(recyclerView: RecyclerView, position: Int, v: View): Boolean
    }

    companion object {

        fun addTo(view: RecyclerView, itemID: Int): ItemClickSupport {
            var support: ItemClickSupport? = view.getTag(itemID) as ItemClickSupport?
            if (support == null) {
                support = ItemClickSupport(view, itemID)
            }
            return support
        }

        fun removeFrom(view: RecyclerView, itemID: Int): ItemClickSupport? {
            val support = view.getTag(itemID) as ItemClickSupport
            support?.detach(view)
            return support
        }
    }
}