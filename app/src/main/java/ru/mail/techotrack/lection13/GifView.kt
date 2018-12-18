package ru.mail.techotrack.lection13


import android.content.Context
import android.content.res.AssetManager
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v4.util.LruCache
import android.util.AttributeSet
import android.view.View

import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream


class GifView : View {

    private var _maxWidth = 0x7fffffff
    private var _maxHeght = 0x7fffffff

    private var _decoder: GifDecoder? = null
    private var _bitmap: Bitmap? = null

    var _imageType = IMAGE_TYPE_UNKNOWN
    var _decodeStatus = DECODE_STATUS_UNDECODE

    private var bWidth: Int = 0
    private var bHeight: Int = 0

    private var time: Long = 0
    private var index: Int = 0

    private var resId: Int = 0
    private var _filePath: String? = null
    private var _assetPath: String? = null

    private var playFlag = false

    private val inputStream: InputStream?
        get() {
            if (_filePath != null) {
                try {
                    return FileInputStream(_filePath!!)
                } catch (e: FileNotFoundException) {
                }

            }
            if (_assetPath != null) {
                try {
                    return context.assets.open(_assetPath!!)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return if (resId > 0) context.resources.openRawResource(resId) else null
        }

    constructor(context: Context) : super(context) {}

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet, defStyle: Int = 0) : super(
        context,
        attrs,
        defStyle
    ) {

        val a = context.obtainStyledAttributes(attrs, R.styleable.GifView, defStyle, 0)

        setMaxWidth(a.getDimensionPixelSize(R.styleable.GifView_maxWidth, Integer.MAX_VALUE))
        setMaxHeight(a.getDimensionPixelSize(R.styleable.GifView_maxHeight, Integer.MAX_VALUE))

        val resId = a.getResourceId(R.styleable.GifView_src, -1)
        if (resId != -1)
            setGif(resId)

        a.recycle()
        if (resId != -1)
            play()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (_bitmap == null)
            return

        if (_decodeStatus == DECODE_STATUS_UNDECODE) {
            canvas.drawBitmap(_bitmap!!, 0f, 0f, null)
            if (playFlag) {
                decode()
                invalidate()
            }
        } else if (_decodeStatus == DECODE_STATUS_DECODING) {
            canvas.drawBitmap(_bitmap!!, 0f, 0f, null)
            invalidate()
        } else if (_decodeStatus == DECODE_STATUS_DECODED) {
            if (_imageType == IMAGE_TYPE_STATIC) {
                canvas.drawBitmap(_bitmap!!, 0f, 0f, null)
            } else if (_imageType == IMAGE_TYPE_DYNAMIC) {
                if (playFlag) {
                    val now = System.currentTimeMillis()

                    if (time + _decoder!!.getDelay(index) < now) {
                        time += _decoder!!.getDelay(index).toLong()
                        incrementFrameIndex()
                    }
                    val bitmap = _decoder!!.getFrame(index)
                    if (bitmap != null) {
                        canvas.drawBitmap(bitmap, 0f, 0f, null)
                    }
                    invalidate()
                } else {
                    val bitmap = _decoder!!.getFrame(index)
                    canvas.drawBitmap(bitmap!!, 0f, 0f, null)
                }
            } else {
                canvas.drawBitmap(_bitmap!!, 0f, 0f, null)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        var w: Int
        var h: Int

        if (null != _bitmap) {
            w = _bitmap!!.width
            h = _bitmap!!.height
            if (w < 0) w = 0
            if (h < 0) h = 0
            w = Math.min(w, _maxWidth)
            h = Math.min(h, _maxHeght)
        } else {
            w = 0
            h = 0
        }

        when (widthMode) {
            View.MeasureSpec.EXACTLY -> w = widthSize
            View.MeasureSpec.AT_MOST -> w = Math.min(w, widthSize)
        }

        when (heightMode) {
            View.MeasureSpec.EXACTLY -> h = heightSize
            View.MeasureSpec.AT_MOST -> h = Math.min(h, heightSize)
        }
        setMeasuredDimension(w, h)
    }

    fun setGif(filePath: String) {
        if (null == filePath)
            throw IllegalArgumentException("Invalid filePath null")
        var bitmap = getImageFromCache(filePath + "file")
        if (null == bitmap) {
            bitmap = BitmapFactory.decodeFile(filePath)
            addImageToCache(bitmap, filePath + "file")
        }
        setGif(filePath, bitmap)
    }

    fun setGifAsset(assetPath: String?) {
        val am = context.assets
        if (null == assetPath)
            throw IllegalArgumentException("Invalid assetPath null")
        var bitmap = getImageFromCache(assetPath + "file")
        if (null == bitmap) {
            try {
                bitmap = BitmapFactory.decodeStream(am.open(assetPath))
                addImageToCache(bitmap, assetPath + "file")
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        setGifAsset(assetPath, bitmap)
    }

    fun setGifAsset(assetPath: String, cacheImage: Bitmap?) {
        this.resId = 0
        _assetPath = assetPath
        _imageType = IMAGE_TYPE_UNKNOWN
        _decodeStatus = DECODE_STATUS_UNDECODE
        playFlag = false
        _bitmap = cacheImage
        if (null != _bitmap) {
            bWidth = _bitmap!!.width
            bHeight = _bitmap!!.height
            // TODO: Need invalidate rect
            //setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        }
    }

    private fun setGif(filePath: String?, cacheImage: Bitmap?) {
        this.resId = 0
        _filePath = filePath
        _imageType = IMAGE_TYPE_UNKNOWN
        _decodeStatus = DECODE_STATUS_UNDECODE
        playFlag = false
        _bitmap = cacheImage
        if (null != _bitmap) {
            bWidth = _bitmap!!.width
            bHeight = _bitmap!!.height
            // TODO: Need invalidate rect
            //setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        }
    }

    private fun setGif(resId: Int) {
        var bitmap = getImageFromCache("!!__res!!" + resId + "file")
        if (null == bitmap) {
            val `is` = context.resources.openRawResource(resId)
            if (null != `is`) {
                bitmap = BitmapFactory.decodeStream(`is`)
                addImageToCache(bitmap, "!!__res!!" + resId + "file")
            }
        }
        setGif(resId, bitmap)
    }

    fun setGif(resId: Int, cacheImage: Bitmap?) {
        _filePath = null
        this.resId = resId
        _imageType = IMAGE_TYPE_UNKNOWN
        _decodeStatus = DECODE_STATUS_UNDECODE
        playFlag = false
        _bitmap = cacheImage
        if (null != _bitmap) {
            bWidth = _bitmap!!.width
            bHeight = _bitmap!!.height
            // TODO: Need invalidate rect
            //setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        }
    }

    private fun decode() {
        release()
        index = 0

        _decodeStatus = DECODE_STATUS_DECODING

        object : Thread() {
            override fun run() {
                _decoder = GifDecoder()
                _decoder!!.read(inputStream)
                if (_decoder!!.width == 0 || _decoder!!.height == 0) {
                    _imageType = IMAGE_TYPE_STATIC
                } else {
                    _imageType = IMAGE_TYPE_DYNAMIC
                }
                postInvalidate()
                time = System.currentTimeMillis()
                _decodeStatus = DECODE_STATUS_DECODED
            }
        }.start()
    }

    fun release() {
        _decoder = null
    }

    private fun incrementFrameIndex() {
        index++
        if (index >= _decoder!!.frameCount) {
            index = 0
        }
    }

    private fun decrementFrameIndex() {
        index--
        if (index < 0) {
            index = _decoder!!.frameCount - 1
        }
    }

    fun play() {
        time = System.currentTimeMillis()
        playFlag = true
        invalidate()
    }

    fun pause() {
        playFlag = false
        invalidate()
    }

    fun stop() {
        playFlag = false
        index = 0
        invalidate()
    }

    fun nextFrame() {
        if (_decodeStatus == DECODE_STATUS_DECODED) {
            incrementFrameIndex()
            invalidate()
        }
    }

    fun prevFrame() {
        if (_decodeStatus == DECODE_STATUS_DECODED) {
            decrementFrameIndex()
            invalidate()
        }
    }

    fun setMaxWidth(width: Int) {
        _maxWidth = width
    }

    fun setMaxHeight(height: Int) {
        _maxHeght = height
    }

    companion object {

        val IMAGE_TYPE_UNKNOWN = 0
        val IMAGE_TYPE_STATIC = 1
        val IMAGE_TYPE_DYNAMIC = 2

        val DECODE_STATUS_UNDECODE = 0
        val DECODE_STATUS_DECODING = 1
        val DECODE_STATUS_DECODED = 2

        private val _imageSize: Int = 0
        private var _memoryCache: LruCache<String, Bitmap>? = null

        private fun initMemoryCache() {
            if (_memoryCache == null) {
                val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
                val cacheSize = maxMemory / 8
                _memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
                    override fun sizeOf(key: String, bitmap: Bitmap): Int {
                        return bitmap.byteCount / 1024
                    }
                }
            }
        }

        private fun addImageToCache(bitmap: Bitmap?, name: String) {
            initMemoryCache()
            _memoryCache!!.put(name, bitmap!!)
        }

        private fun getImageFromCache(name: String): Bitmap? {
            return if (null == _memoryCache) null else _memoryCache!!.get(name)
        }
    }
}

