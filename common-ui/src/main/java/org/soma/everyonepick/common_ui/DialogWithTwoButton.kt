package org.soma.everyonepick.common_ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import org.soma.everyonepick.common_ui.databinding.DialogWithTwoButtonBinding

class DialogWithTwoButton(
    context: Context,
    private val message: String,
    private val negativeButtonText: String,
    private val positiveButtonText: String,
    private val onClickPositiveButton: () -> Unit
): Dialog(context) {

    private lateinit var binding: DialogWithTwoButtonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogWithTwoButtonBinding.inflate(layoutInflater).apply {
            textMessage.text = message

            textNegativebutton.text = negativeButtonText
            textNegativebutton.setOnClickListener { dismiss() }

            textPositivebutton.text = positiveButtonText
            textPositivebutton.setOnClickListener {
                onClickPositiveButton.invoke()
                dismiss()
            }
        }
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    class Builder(private val context: Context) {
        private var message = ""
        private var negativeButtonText = "취소"
        private var positiveButtonText = "확인"
        private var onClickPositiveButton = {}

        fun setMessage(message: String) = apply {
            this.message = message
        }

        fun setPositiveButtonText(text: String) = apply {
            this.positiveButtonText = text
        }

        fun setNegativeButtonText(text: String) = apply {
            this.negativeButtonText = text
        }

        fun setOnClickPositiveButton(onClickPositiveButton: () -> Unit) = apply {
            this.onClickPositiveButton = onClickPositiveButton
        }

        fun build() = DialogWithTwoButton(
            context,
            message,
            negativeButtonText,
            positiveButtonText,
            onClickPositiveButton
        )
    }
}