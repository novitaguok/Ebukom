package com.ebukom.utils

import android.app.ProgressDialog
import android.content.Context

object LoadingUtils {
    fun with(context: Context, message: String?): Init {
        return Init(context, message)
    }

    fun simpleLoading(context: Context): Init {
        return Init(context)
    }

    class Init {
        private var dialog: ProgressDialog? = null

        constructor(context: Context, message: String?) {
            var msg = message
            if (dialog == null) {
                dialog = ProgressDialog(context)
            }
            if (msg == null)
                msg = "Please wait.."
            dialog!!.setMessage(msg)
        }

        constructor(context: Context) {
            if (dialog == null) {
                dialog = ProgressDialog(context)
            }
            dialog!!.setMessage("Please wait..")
            dialog!!.setCancelable(false)
        }

        fun title(title: String): Init {
            if (dialog != null)
                dialog!!.setTitle(title)
            return this
        }

        fun cancelable(cancelable: Boolean): Init {
            if (dialog != null)
                dialog!!.setCancelable(cancelable)
            return this
        }

        fun show(): ProgressDialog? {
            if (dialog != null && !dialog!!.isShowing) {
                dialog!!.show()
            }
            return dialog
        }
    }
}
