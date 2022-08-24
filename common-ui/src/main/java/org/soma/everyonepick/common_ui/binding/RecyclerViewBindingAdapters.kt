package org.soma.everyonepick.common_ui.binding

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("adapter", "submitList", requireAll = true)
fun bindRecyclerView(view: RecyclerView, adapter: RecyclerView.Adapter<*>, submitList: List<Any>?) {
    view.adapter = adapter.apply {
        stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        (this as ListAdapter<Any, *>).submitList(submitList?.toMutableList())
    }
}