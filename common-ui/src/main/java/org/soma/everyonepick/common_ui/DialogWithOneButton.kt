package org.soma.everyonepick.common_ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import org.soma.everyonepick.common_ui.databinding.DialogWithOneButtonBinding

class DialogWithOneButton(
    context: Context,
    private val message: String,
    private val buttonText: String,
    private val onClickButton: () -> Unit
): Dialog(context) {

    private lateinit var binding: DialogWithOneButtonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogWithOneButtonBinding.inflate(layoutInflater).apply {
            textMessage.text = message

            textButton.text = buttonText
            textButton.setOnClickListener {
                onClickButton.invoke()
                dismiss()
            }
        }
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    class Builder(private val context: Context) {
        private var message = ""
        private var buttonText = "확인"
        private var onClickButton = {}

        fun setMessage(message: String) = apply {
            this.message = message
        }

        fun setButtonText(text: String) = apply {
            this.buttonText = text
        }

        fun setOnClickButton(onClickButton: () -> Unit) = apply {
            this.onClickButton = onClickButton
        }

        fun build() = DialogWithOneButton(
            context,
            message,
            buttonText,
            onClickButton
        )
    }
}