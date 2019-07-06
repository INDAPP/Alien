package it.earthgardeners.alien

import android.content.Context
import android.widget.Toast

/**
 * This class is part of Alien project.
 * Created by riccardopizzoni on 2019-07-06.
 * Copyright © 2019 INDAPP
 * info@indapp.it
 */

val Float.timer: String
    get() {
        val seconds = this.toInt()
        return String.format("%02d:%02d", seconds / 60, seconds % 60)
    }

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}