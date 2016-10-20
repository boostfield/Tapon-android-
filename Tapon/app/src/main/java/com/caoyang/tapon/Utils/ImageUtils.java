package com.caoyang.tapon.Utils;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

import com.caoyang.tapon.AppData;

import java.io.IOException;

/**
 * Created by qky1412 on 15/7/31.
 */
public class ImageUtils {

    private static final int MAX_TEXTURE_SIZE = getOpengl2MaxTextureSize();

    public static int getOpengl2MaxTextureSize() {
        int[] maxTextureSize = new int[1];
        maxTextureSize[0] = 2048;
        android.opengl.GLES20.glGetIntegerv(android.opengl.GLES20.GL_MAX_TEXTURE_SIZE,
                maxTextureSize, 0);
        return maxTextureSize[0];
    }

    /**
     * Get the size in bytes of a bitmap.
     *
     * @param bitmap
     * @return size in bytes
     */
    @SuppressLint("NewApi")
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * Decode and sample down a bitmap from resources to the requested width and
     * height.
     *
     * @param res       The resources object containing the image data
     * @param resId     The resource id of the image data
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return A bitmap sampled down from the original with the same aspect
     * ratio and dimensions that are equal to or greater than the
     * requested width and height(inMutable)
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth,
                                                         int reqHeight) {
        return decodeSampledBitmapFromResource(res, resId, reqWidth, reqHeight, false);
    }

    /**
     * Decode and sample down a bitmap from resources to the requested width and
     * height.
     *
     * @param res       The resources object containing the image data
     * @param resId     The resource id of the image data
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @param isMutable 可编辑
     * @return A bitmap sampled down from the original with the same aspect
     * ratio and dimensions that are equal to or greater than the
     * requested width and height
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight, boolean isMutable) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        if (isMutable && Build.VERSION.SDK_INT >= 11) {
            options.inMutable = true;
        }
        Bitmap result = BitmapFactory.decodeResource(res, resId, options);
        if (isMutable) {
            result = createMutableBitmap(result);
        }
        return result;
    }

    public static Bitmap decodeSampledBitmapFromFile(String filePath, int sampledSize) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = sampledSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * Decode and sample down a bitmap from a file to the requested width and
     * height.
     *
     * @param filePath  The full path of the file to decode
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return A bitmap sampled down from the original with the same aspect
     * ratio and dimensions that are equal to or greater than the
     * requested width and height(inmutable)
     */
    public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight) {
        return decodeSampledBitmapFromFile(filePath, reqWidth, reqHeight, false);
    }

    /**
     * Decode and sample down a bitmap from a file to the requested width and
     * height.
     *
     * @param filePath  The full path of the file to decode
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @param isMutable 可编辑
     * @return A bitmap sampled down from the original with the same aspect
     * ratio and dimensions that are equal to or greater than the
     * requested width and height
     */
    public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight,
                                                     boolean isMutable) {
        if (filePath == null) {
            return null;
        }
        if (reqHeight == 0) {
            reqHeight = MAX_TEXTURE_SIZE;
        }
        if (reqWidth == 0) {
            reqWidth = MAX_TEXTURE_SIZE;
        }

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);

        if (options.outWidth == -1 || options.outHeight == -1) {
            return null;
        }

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        if (isMutable && Build.VERSION.SDK_INT >= 11) {
            options.inMutable = true;
        }

        Bitmap result = null;

        if (options.outWidth > MAX_TEXTURE_SIZE || options.outHeight > MAX_TEXTURE_SIZE
                || (options.outHeight >= options.outWidth * 3)) {
            // 长图
            try {
                result = regionDecode(filePath, reqWidth, reqHeight, options.outWidth,
                        options.outHeight);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            result = BitmapFactory.decodeFile(filePath, options);
        }

        if (isMutable) {
            result = createMutableBitmap(result);
        }

        return result;
    }

    private static Bitmap regionDecode(String path, int reqWidth, int reqHeight, int outWidth,
                                       int outHeight) throws IOException {
        BitmapRegionDecoder regionDecoder = BitmapRegionDecoder.newInstance(path, true);
        if (reqWidth > outWidth) {
            reqWidth = outWidth;
        }
        if (reqHeight > outHeight) {
            reqHeight = outHeight;
        }

        return regionDecoder.decodeRegion(new Rect(0, 0, reqWidth, reqHeight), null);
    }

    /**
     * Calculate an inSampleSize for use in a
     * {@link BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from
     * {@link BitmapFactory}. This implementation calculates
     * the closest inSampleSize that will result in the final decoded bitmap
     * having a width and height equal to or larger than the requested width and
     * height. This implementation does not ensure a power of 2 is returned for
     * inSampleSize which can be faster when decoding but results in a larger
     * bitmap which isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run
     *                  through a decode* method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                            int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            int widthSampleSize = 0;
            int heightSampleSize = 0;
            if (reqWidth < width) {
                widthSampleSize = Math.round((float) width / (float) reqWidth);
            }
            if (reqHeight < height) {
                heightSampleSize = Math.round((float) height / (float) reqHeight);
            }
            inSampleSize = Math.max(widthSampleSize, heightSampleSize);
            // if (width > height) {
            // inSampleSize = Math.round((float) height / (float) reqHeight);
            // } else {
            // inSampleSize = Math.round((float) width / (float) reqWidth);
            // }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger
            // inSampleSize).

            // final float totalPixels = width * height;
            //
            // // Anything more than 2x the requested pixels we'll sample down
            // // further.
            // float totalReqPixelsCap = reqWidth * reqHeight * 2;
            // while (totalPixels / (inSampleSize * inSampleSize) >
            // totalReqPixelsCap) {
            // inSampleSize++;
            // }
        }
        return inSampleSize;
    }

    /**
     * 通过srcbitmap 创建一个可编辑的bitmap
     *
     * @param src
     * @return
     */
    public static Bitmap createMutableBitmap(Bitmap src) {
        Bitmap result = null;
        if (src == null) {
            return null;
        }
        result = src.copy(Bitmap.Config.ARGB_8888, true);

        return result;
    }

    /**
     * 将subBmp图像合并到oriBmp中
     *
     * @param oriBmp
     * @param subBmp
     * @param oriRect subBmp中取出的bitmap需要填充到oriRect中的区域
     * @param subRect 从subBmp中取出的区域
     * @param paint
     * @return
     */
    public static Bitmap mergeBitmap(Bitmap oriBmp, Bitmap subBmp, final Rect oriRect,
                                     final Rect subRect) {
        if (subBmp == null) {
            return oriBmp;
        }

        if (oriBmp == null) {
            return null;
        }

        if (!oriBmp.isMutable()) {
            oriBmp = createMutableBitmap(oriBmp);
        }

        Canvas canvas = new Canvas(oriBmp);
        canvas.drawBitmap(subBmp, subRect, oriRect, null);
        return oriBmp;
    }

    /**
     * 将subBmp图像合并到oriBmp中
     *
     * @param oriBmp
     * @param subBmp
     * @param paint
     * @return oriBmp
     */
    public static Bitmap mergeBitmap(Bitmap oriBmp, Bitmap subBmp) {
        if (subBmp == null) {
            return oriBmp;
        }

        if (oriBmp == null) {
            return null;
        }

        return mergeBitmap(oriBmp, subBmp, new Rect(0, 0, oriBmp.getWidth(), oriBmp.getHeight()),
                new Rect(0, 0, subBmp.getWidth(), subBmp.getHeight()));
    }

    private static final PorterDuffXfermode SRC_IN_MODE = new PorterDuffXfermode(
            PorterDuff.Mode.SRC_IN);

    private final static Paint SRC_IN_PAINT = new Paint();

    static {
        SRC_IN_PAINT.setXfermode(SRC_IN_MODE);
    }

    /**
     * 遮罩图片
     *
     * @param dstBmp
     * @param mask
     * @param paint
     * @return 遮罩后的图片
     */
    public static Bitmap maskBitmap(final Bitmap dstBmp, final Bitmap mask) {
        if (dstBmp == null || mask == null) {
            return dstBmp;
        }
        Bitmap result = Bitmap
                .createBitmap(dstBmp.getWidth(), dstBmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        int sc = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null,
                Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                        | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        canvas.drawBitmap(mask, new Rect(0, 0, mask.getWidth(), mask.getHeight()), new Rect(0, 0,
                dstBmp.getWidth(), dstBmp.getHeight()), null);
        canvas.drawBitmap(dstBmp, 0, 0, SRC_IN_PAINT);

        canvas.restoreToCount(sc);
        return result;
    }

    public static Bitmap convertToAlphaMask(Bitmap b) {
        Bitmap a = Bitmap.createBitmap(b.getWidth(), b.getHeight(), Bitmap.Config.ALPHA_8);
        Canvas c = new Canvas(a);
        c.drawBitmap(b, 0.0f, 0.0f, null);
        return a;
    }

    public static Bitmap decodeBitmapFromDrawableRes(int resId, final int width, final int height) {
        Drawable drawable = AppData.getContext().getResources().getDrawable(resId);
        drawable.setBounds(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return bitmap;
    }

    //把view视图转换成bitmap
    public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
    }

    /**
     * Android api 17实现的虚化
     * 某些机型上可能会Crash
     *
     * @param sentBitmap
     * @param radius     大于1小于等于25
     * @return
     */
    @SuppressLint("NewApi")
    public static Bitmap fastblur(Bitmap sentBitmap, int radius) {
        if (sentBitmap == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT > 16) {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
            final RenderScript rs = RenderScript.create(AppData.getContext());
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius /* e.g. 3.f */);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            bitmap.recycle();
            rs.destroy();
            return bitmap;
        }
        return stackblur(sentBitmap, radius);
    }

    /**
     * 纯Java实现的虚化，适用老版本api，外部只需调fastblur，会自动判断
     *
     * @param sentBitmap
     * @param radius     大于1小于等于25
     * @return
     */
    private static Bitmap stackblur(Bitmap sentBitmap, int radius) {
        Bitmap bitmap = null;
        try {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return sentBitmap;
        }
        if (radius < 1) {
            return (null);
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;
        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];
        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }
        yw = yi = 0;
        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;
        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;
            for (x = 0; x < w; x++) {
                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;
                sir = stack[i + radius];
                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];
                rbs = r1 - Math.abs(i);
                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];
                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi += w;
            }
        }
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }
}
