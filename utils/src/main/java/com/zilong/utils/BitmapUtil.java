package com.zilong.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class BitmapUtil {
    private static Context mContext = LibUtils.getContext();


    /**
     * 质量压缩 (仅仅在文件存储的时候使用此压缩即可,否则没有任何作用)
     * <p>
     * 此压缩,只针对文件存储或者文件传输,在内存中,大小是不会改变的
     * 如果想要不崩溃,还是得进行尺寸的压缩,真正的缩小内存占用
     *
     * @param image
     * @param maxSize 压缩的大小 单位kb
     * @return
     */
    public static ByteArrayOutputStream compressQuality(Bitmap image, int maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;

        while (baos.toByteArray().length / 1024 > maxSize) { // 循环判断如果压缩后图片是否大于maxSize kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
            if (options <= 0) {
                break;
            }
        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        return baos;
    }


    /**
     * 尺寸压缩
     * 默认压缩宽高 480 * 800
     *
     * @param srcPath
     * @return
     */
    public static Bitmap compressSize(String srcPath) {
        return compressSize(srcPath, 480f, 800f);
    }

    /**
     * 尺寸压缩
     * 图片按比例
     * 如果是从file中读取图片,加载到内存,然后显示,小图无所谓,大图,最好调用此方法,压缩一下,避免oom
     *
     * @param srcPath （根据路径获取图片并压缩）
     * @return
     */
    public static Bitmap compressSize(String srcPath, float pixelW, float pixelH) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = pixelH;
        float ww = pixelW;
        int be = 1;// be=1表示不缩放
        if (w >= h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w <= h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }


    /**
     * 截取webView快照(webView加载的整个内容的大小)
     *
     * @param webView
     * @return
     */
    public static Bitmap captureWebView(WebView webView) {
        //获取Picture对象
        Picture snapShot = webView.capturePicture();
        //得到图片的宽和高（没有reflect图片内容）
        int width = snapShot.getWidth();
        int height = snapShot.getHeight();
        if (width > 0 && height > 0) {
            //创建位图
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            //绘制(会调用native方法，完成图形绘制)
            snapShot.draw(canvas);
            return bitmap;
        }

        return null;
    }


    /**
     * 保存Bitmap至本地固定位置
     * 先质量压缩一下,这样file文件会变小
     * 默认 128kb
     *
     * @param bitmap
     * @param fileName
     * @param isCover  如果文件存在,是否覆盖
     * @return 文件路径
     */
    public static String saveBitmapToAppDir(Bitmap bitmap, String fileName, boolean isCover) {
        File f = null;
        try {
            f = new File(FileUtil.getPictureAppDir(), fileName);
            if (f.exists() && !isCover) {
                Log.e("BitmapUtil", f.getName() + "已经存在");
                return f.getAbsolutePath();
            } else {
                if (!f.exists() && !f.createNewFile()) {
                    Log.e("BitmapUtil", fileName + "创建失败!");
                } else {
                    //将bitmap质量压缩,
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
                    int options = 90;
                    while (baos.toByteArray().length / 1024 > 128) { // 循环判断如果压缩后图片是否大于maxSize kb,大于继续压缩
                        baos.reset(); // 重置baos即清空baos
                        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
                        options -= 10;// 每次都减少10
                        if (options <= 0) {
                            break;
                        }
                    }

                    FileOutputStream fOut = null;

                    fOut = new FileOutputStream(f);
                    fOut.write(baos.toByteArray());
                    fOut.flush();
                    fOut.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return f == null ? null : f.getAbsolutePath();

    }

    /**
     * 保存Bitmap至本地固定位置 png格式,比较慢
     * 先质量压缩一下,这样file文件会变小
     * 默认 128kb
     *
     * @param bitmap
     * @param fileName
     * @param isCover  如果文件存在,是否覆盖
     * @return 文件路径
     */
    public static String saveBitmapToAppDirByPng(Bitmap bitmap, String fileName, boolean isCover) {
        File f = null;
        try {
            f = new File(FileUtil.getPictureAppDir(), fileName);
            if (f.exists() && !isCover) {
                Log.e("BitmapUtil", f.getName() + "已经存在");
                return f.getAbsolutePath();
            } else {
                if (!f.exists() && !f.createNewFile()) {
                    Log.e("BitmapUtil", fileName + "创建失败!");
                } else {
                    //将bitmap质量压缩,
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
                    int options = 90;
                    while (baos.toByteArray().length / 1024 > 128) { // 循环判断如果压缩后图片是否大于maxSize kb,大于继续压缩
                        baos.reset(); // 重置baos即清空baos
                        bitmap.compress(Bitmap.CompressFormat.PNG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
                        options -= 10;// 每次都减少10
                        if (options <= 0) {
                            break;
                        }
                    }

                    FileOutputStream fOut = null;

                    fOut = new FileOutputStream(f);
                    fOut.write(baos.toByteArray());
                    fOut.flush();
                    fOut.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return f == null ? null : f.getAbsolutePath();

    }


    public static String saveBitmapToPhoneDir(String src, String fileName) {
        return saveBitmapToPhoneDir(BitmapFactory.decodeFile(src), fileName);
    }

    /**
     * 保存Bitmap至本地固定位置
     *
     * @param bitmap
     */
    public static String saveBitmapToPhoneDir(Bitmap bitmap, String fileName) {
        File f = null;
        try {
            f = new File(FileUtil.getPicturePhoneDir(), System.currentTimeMillis() + fileName);

            //将bitmap质量压缩,
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 90;
            while (baos.toByteArray().length / 1024 > 128) { // 循环判断如果压缩后图片是否大于maxSize kb,大于继续压缩
                baos.reset(); // 重置baos即清空baos
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
                options -= 10;// 每次都减少10
                if (options <= 0) {
                    break;
                }
            }

            FileOutputStream fOut = null;

            fOut = new FileOutputStream(f);
            fOut.write(baos.toByteArray());
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (f != null) {
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + f.getAbsolutePath())));
            return f.getAbsolutePath();
        } else {
            return null;
        }
    }

    /**
     * 把两个位图覆盖合成为一个位图，上下拼接
     *
     * @param topBitmap
     * @param bottomBitmap
     * @param isBaseMax    是否以宽度大的位图为准，true则小图等比拉伸，false则大图等比压缩
     * @return
     */
    public static Bitmap mergeBitmap_TB(Bitmap topBitmap, Bitmap bottomBitmap, boolean isBaseMax) {

        if (topBitmap == null || topBitmap.isRecycled()
                || bottomBitmap == null || bottomBitmap.isRecycled()) {
            Log.e("BitmapUtil", "topBitmap=" + topBitmap + ";bottomBitmap=" + bottomBitmap);
            return null;
        }
        int width = 0;
        if (isBaseMax) {
            width = topBitmap.getWidth() > bottomBitmap.getWidth() ? topBitmap.getWidth() : bottomBitmap.getWidth();
        } else {
            width = topBitmap.getWidth() < bottomBitmap.getWidth() ? topBitmap.getWidth() : bottomBitmap.getWidth();
        }
        Bitmap tempBitmapT = topBitmap;
        Bitmap tempBitmapB = bottomBitmap;

        if (topBitmap.getWidth() != width) {
            tempBitmapT = Bitmap.createScaledBitmap(topBitmap, width, (int) (topBitmap.getHeight() * 1f / topBitmap.getWidth() * width), false);
        } else if (bottomBitmap.getWidth() != width) {
            tempBitmapB = Bitmap.createScaledBitmap(bottomBitmap, width, (int) (bottomBitmap.getHeight() * 1f / bottomBitmap.getWidth() * width), false);
        }

        int height = tempBitmapT.getHeight() + tempBitmapB.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Rect topRect = new Rect(0, 0, tempBitmapT.getWidth(), tempBitmapT.getHeight());
        Rect bottomRect = new Rect(0, 0, tempBitmapB.getWidth(), tempBitmapB.getHeight());

        Rect bottomRectT = new Rect(0, tempBitmapT.getHeight(), width, height);

        canvas.drawBitmap(tempBitmapT, topRect, topRect, null);
        canvas.drawBitmap(tempBitmapB, bottomRect, bottomRectT, null);
        return bitmap;
    }


    /**
     * 把两个位图覆盖合成为一个位图，左右拼接
     *
     * @param leftBitmap
     * @param rightBitmap
     * @param isBaseMax   是否以宽度大的位图为准，true则小图等比拉伸，false则大图等比压缩
     * @return
     */
    public static Bitmap mergeBitmap_LR(Bitmap leftBitmap, Bitmap rightBitmap, boolean isBaseMax) {

        if (leftBitmap == null || leftBitmap.isRecycled()
                || rightBitmap == null || rightBitmap.isRecycled()) {
            Log.e("BitmapUtil", "leftBitmap=" + leftBitmap + ";rightBitmap=" + rightBitmap);
            return null;
        }
        int height = 0; // 拼接后的高度，按照参数取大或取小
        if (isBaseMax) {
            height = leftBitmap.getHeight() > rightBitmap.getHeight() ? leftBitmap.getHeight() : rightBitmap.getHeight();
        } else {
            height = leftBitmap.getHeight() < rightBitmap.getHeight() ? leftBitmap.getHeight() : rightBitmap.getHeight();
        }

        // 缩放之后的bitmap
        Bitmap tempBitmapL = leftBitmap;
        Bitmap tempBitmapR = rightBitmap;

        if (leftBitmap.getHeight() != height) {
            tempBitmapL = Bitmap.createScaledBitmap(leftBitmap, (int) (leftBitmap.getWidth() * 1f / leftBitmap.getHeight() * height), height, false);
        } else if (rightBitmap.getHeight() != height) {
            tempBitmapR = Bitmap.createScaledBitmap(rightBitmap, (int) (rightBitmap.getWidth() * 1f / rightBitmap.getHeight() * height), height, false);
        }

        // 拼接后的宽度
        int width = tempBitmapL.getWidth() + tempBitmapR.getWidth();

        // 定义输出的bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // 缩放后两个bitmap需要绘制的参数
        Rect leftRect = new Rect(0, 0, tempBitmapL.getWidth(), tempBitmapL.getHeight());
        Rect rightRect = new Rect(0, 0, tempBitmapR.getWidth(), tempBitmapR.getHeight());

        // 右边图需要绘制的位置，往右边偏移左边图的宽度，高度是相同的
        Rect rightRectT = new Rect(tempBitmapL.getWidth(), 0, width, height);

        canvas.drawBitmap(tempBitmapL, leftRect, leftRect, null);
        canvas.drawBitmap(tempBitmapR, rightRect, rightRectT, null);
        return bitmap;
    }

    /**
     * 把两个位图覆盖合成为一个位图，以底层位图的长宽为基准
     *
     * @param backBitmap  在底部的位图
     * @param frontBitmap 盖在上面的位图
     * @return
     */
    public static Bitmap mergeBitmap(Bitmap backBitmap, Bitmap frontBitmap, Bitmap bitmap2) {

        if (backBitmap == null || backBitmap.isRecycled()
                || frontBitmap == null || frontBitmap.isRecycled()) {
            Log.e("BitmapUtil", "backBitmap=" + backBitmap + ";frontBitmap=" + frontBitmap);
            return null;
        }
        Bitmap bitmap = backBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        //根据需要,调整位置
        Rect baseRect = new Rect(0, backBitmap.getHeight() - frontBitmap.getHeight() * 5 + DensityUtil.dp2px(100), backBitmap.getWidth(), backBitmap.getHeight() - frontBitmap.getHeight() * 4 + DensityUtil.dp2px(100));

        Rect frontRect = new Rect(0, 0, frontBitmap.getWidth(), frontBitmap.getHeight());
        canvas.drawBitmap(frontBitmap, frontRect, baseRect, null);


        //根据需要,调整位置
        Rect baseRect2 = new Rect(backBitmap.getWidth() - DensityUtil.dp2px(172), DensityUtil.dp2px(258), backBitmap.getWidth() - DensityUtil.dp2px(52), DensityUtil.dp2px(258) + bitmap2.getHeight());

        Rect frontRect2 = new Rect(0, 0, bitmap2.getWidth(), bitmap2.getHeight());
        canvas.drawBitmap(bitmap2, frontRect2, baseRect2, null);

        return bitmap;
    }
}