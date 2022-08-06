package org.soma.everyonepick.common_ui.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("adapter")
fun bindAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter.apply {
        stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }
}

@BindingAdapter("submitList")
fun bindSubmitList(view: RecyclerView, submitList: List<Any>?) {
    view.adapter?.let {
        (it as ListAdapter<Any, *>).submitList(submitList?.toMutableList())
    }
}