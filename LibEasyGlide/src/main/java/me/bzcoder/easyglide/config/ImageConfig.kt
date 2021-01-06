
package me.bzcoder.easyglide.config

import android.widget.ImageView
import java.util.*

open class ImageConfig {
    var url: Any? = null
        protected set
    var drawableId = 0
        protected set
    var imageView: ImageView? = null
        protected set
    var placeholder = 0
        protected set
    var errorPic = 0
        protected set
}