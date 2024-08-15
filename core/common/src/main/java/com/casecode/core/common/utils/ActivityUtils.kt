package com.casecode.core.common.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.activity.ComponentActivity
import com.casecode.core.common.ClassNames

fun moveToAppLauncherActivity(context: Context) {
    val intent = Intent(context, Class.forName(ClassNames.APP_LAUNCHER_ACTIVITY))
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
    context.findActivity().finish()
}

fun moveToMainActivity(context: Context) {
    val intent = Intent(context, Class.forName(ClassNames.MAIN_ACTIVITY))
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
    context.findActivity().finish()
}

private tailrec fun Context.findActivity(): ComponentActivity =
    when (this) {
        is ComponentActivity -> this
        is ContextWrapper -> this.baseContext.findActivity()
        else -> throw IllegalArgumentException("Could not find activity!")
    }