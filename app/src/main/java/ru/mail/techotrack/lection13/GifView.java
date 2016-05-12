package ru.mail.techotrack.lection13;


import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.util.LruCache;
import android.util.AttributeSet;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class GifView extends View {

    public static final int IMAGE_TYPE_UNKNOWN = 0;
    public static final int IMAGE_TYPE_STATIC = 1;
    public static final int IMAGE_TYPE_DYNAMIC = 2;

    public static final int DECODE_STATUS_UNDECODE = 0;
    public static final int DECODE_STATUS_DECODING = 1;
    public static final int DECODE_STATUS_DECODED = 2;

    private int _maxWidth = 0x7fffffff;
    private int _maxHeght = 0x7fffffff;

    private GifDecoder _decoder;
    private Bitmap _bitmap;

    public int _imageType = IMAGE_TYPE_UNKNOWN;
    public int _decodeStatus = DECODE_STATUS_UNDECODE;

    private int width;
    private int height;

    private long time;
    private int index;

    private int resId;
    private String _filePath;
    private String _assetPath;

    private boolean playFlag = false;

    private static int _imageSize;
    private static LruCache<String, Bitmap> _memoryCache;

    public GifView(Context context) {
        super(context);
    }

    public GifView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GifView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GifView, defStyle, 0);

        setMaxWidth(a.getDimensionPixelSize(R.styleable.GifView_maxWidth, Integer.MAX_VALUE));
        setMaxHeight(a.getDimensionPixelSize(R.styleable.GifView_maxHeight, Integer.MAX_VALUE));

        int resId = a.getResourceId(R.styleable.GifView_src, -1);
        if (resId != -1)
            setGif(resId);

        a.recycle();
        if (resId != -1)
            play();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (_bitmap == null)
            return;

        if (_decodeStatus == DECODE_STATUS_UNDECODE) {
            canvas.drawBitmap(_bitmap, 0, 0, null);
            if (playFlag) {
                decode();
                invalidate();
            }
        } else if (_decodeStatus == DECODE_STATUS_DECODING) {
            canvas.drawBitmap(_bitmap, 0, 0, null);
            invalidate();
        } else if (_decodeStatus == DECODE_STATUS_DECODED) {
            if (_imageType == IMAGE_TYPE_STATIC) {
                canvas.drawBitmap(_bitmap, 0, 0, null);
            } else if (_imageType == IMAGE_TYPE_DYNAMIC) {
                if (playFlag) {
                    long now = System.currentTimeMillis();

                    if (time + _decoder.getDelay(index) < now) {
                        time += _decoder.getDelay(index);
                        incrementFrameIndex();
                    }
                    Bitmap bitmap = _decoder.getFrame(index);
                    if (bitmap != null) {
                        canvas.drawBitmap(bitmap, 0, 0, null);
                    }
                    invalidate();
                } else {
                    Bitmap bitmap = _decoder.getFrame(index);
                    canvas.drawBitmap(bitmap, 0, 0, null);
                }
            } else {
                canvas.drawBitmap(_bitmap, 0, 0, null);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int w;
        int h;

        if (null != _bitmap) {
            w = _bitmap.getWidth();
            h = _bitmap.getHeight();
            if (w < 0) w = 0;
            if (h < 0) h = 0;
            w = Math.min(w, _maxWidth);
            h = Math.min(h, _maxHeght);
        } else {
            w = 0;
            h = 0;
        }

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                w = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                w = Math.min(w, widthSize);
                break;
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                h = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                h = Math.min(h, heightSize);
                break;
        }
        setMeasuredDimension(w, h);
    }

    private InputStream getInputStream() {
        if (_filePath != null) {
            try {
                return new FileInputStream(_filePath);
            } catch (FileNotFoundException e) {
            }
        }
        if (_assetPath != null) {
            try {
                return getContext().getAssets().open(_assetPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (resId > 0)
            return getContext().getResources().openRawResource(resId);
        return null;
    }

    public void setGif(String filePath) {
        if (null == filePath)
            throw new IllegalArgumentException("Invalid filePath null");
        Bitmap bitmap = getImageFromCache(filePath + "file");
        if (null == bitmap) {
            bitmap = BitmapFactory.decodeFile(filePath);
            addImageToCache(bitmap, filePath + "file");
        }
        setGif(filePath, bitmap);
    }

    public void setGifAsset(String assetPath) {
        AssetManager am = getContext().getAssets();
        if (null == assetPath)
            throw new IllegalArgumentException("Invalid assetPath null");
        Bitmap bitmap = getImageFromCache(assetPath + "file");
        if (null == bitmap) {
            try {
                bitmap = BitmapFactory.decodeStream(am.open(assetPath));
                addImageToCache(bitmap, assetPath + "file");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setGifAsset(assetPath, bitmap);
    }

    public void setGifAsset(String assetPath, Bitmap cacheImage) {
        this.resId = 0;
        _assetPath = assetPath;
        _imageType = IMAGE_TYPE_UNKNOWN;
        _decodeStatus = DECODE_STATUS_UNDECODE;
        playFlag = false;
        _bitmap = cacheImage;
        if (null != _bitmap) {
            width = _bitmap.getWidth();
            height = _bitmap.getHeight();
            // TODO: Need invalidate rect
            //setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        }
    }

    public void setGif(String filePath, Bitmap cacheImage) {
        this.resId = 0;
        _filePath = filePath;
        _imageType = IMAGE_TYPE_UNKNOWN;
        _decodeStatus = DECODE_STATUS_UNDECODE;
        playFlag = false;
        _bitmap = cacheImage;
        if (null != _bitmap) {
            width = _bitmap.getWidth();
            height = _bitmap.getHeight();
            // TODO: Need invalidate rect
            //setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        }
    }

    public void setGif(int resId) {
        Bitmap bitmap = getImageFromCache("!!__res!!" + resId + "file");
        if (null == bitmap) {
            InputStream is = getContext().getResources().openRawResource(resId);
            if (null != is) {
                bitmap = BitmapFactory.decodeStream(is);
                addImageToCache(bitmap, "!!__res!!" + resId + "file");
            }
        }
        setGif(resId, bitmap);
    }

    public void setGif(int resId, Bitmap cacheImage) {
        _filePath = null;
        this.resId = resId;
        _imageType = IMAGE_TYPE_UNKNOWN;
        _decodeStatus = DECODE_STATUS_UNDECODE;
        playFlag = false;
        _bitmap = cacheImage;
        if (null != _bitmap) {
            width = _bitmap.getWidth();
            height = _bitmap.getHeight();
            // TODO: Need invalidate rect
            //setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        }
    }

    private void decode() {
        release();
        index = 0;

        _decodeStatus = DECODE_STATUS_DECODING;

        new Thread() {
            @Override
            public void run() {
                _decoder = new GifDecoder();
                _decoder.read(getInputStream());
                if (_decoder.width == 0 || _decoder.height == 0) {
                    _imageType = IMAGE_TYPE_STATIC;
                } else {
                    _imageType = IMAGE_TYPE_DYNAMIC;
                }
                postInvalidate();
                time = System.currentTimeMillis();
                _decodeStatus = DECODE_STATUS_DECODED;
            }
        }.start();
    }

    public void release() {
        _decoder = null;
    }

    private void incrementFrameIndex() {
        index++;
        if (index >= _decoder.getFrameCount()) {
            index = 0;
        }
    }

    private void decrementFrameIndex() {
        index--;
        if (index < 0) {
            index = _decoder.getFrameCount() - 1;
        }
    }

    public void play() {
        time = System.currentTimeMillis();
        playFlag = true;
        invalidate();
    }

    public void pause() {
        playFlag = false;
        invalidate();
    }

    public void stop() {
        playFlag = false;
        index = 0;
        invalidate();
    }

    public void nextFrame() {
        if (_decodeStatus == DECODE_STATUS_DECODED) {
            incrementFrameIndex();
            invalidate();
        }
    }

    public void prevFrame() {
        if (_decodeStatus == DECODE_STATUS_DECODED) {
            decrementFrameIndex();
            invalidate();
        }
    }

    public void setMaxWidth(int width) {
        _maxWidth = width;
    }

    public void setMaxHeight(int height) {
        _maxHeght = height;
    }

    private static void initMemoryCache() {
        if (_memoryCache == null) {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;
            _memoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount() / 1024;
                }
            };
        }
    }

    private static void addImageToCache(Bitmap bitmap, String name) {
        initMemoryCache();
        _memoryCache.put(name, bitmap);
    }

    private static Bitmap getImageFromCache(String name) {
        if (null == _memoryCache) return null;
        return _memoryCache.get(name);
    }
}

