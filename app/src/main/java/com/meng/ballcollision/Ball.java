package com.meng.ballcollision;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * User: mengqingdong
 * Date: 2018/5/21
 * Time: 14:30
 * Description:
 */

public class Ball {
    /**
     * x轴速度 向右为正，向左为负
     */
    public float vx = 2.0f;
    /**
     * y轴速度  向右为正，向左为负
     */
    public float vy = 2.0f;
    /**
     * 圆心x坐标
     */
    public float centerX;
    /**
     * 圆心y坐标
     */
    public float centerY;
    /**
     * 圆半径
     */
    public float radius = 150.0f;
    /**
     * 2个球碰撞的弹性
     */
    public float spring = 1.0f;
    /**
     * 球与边界碰撞的弹性
     */
    public float bounce = -1.0f;
    /**
     * 球的重力
     */
    public float gravity = 0.00f;

    /**
     * paint
     */
    public Paint paint;
    /**
     * 图片bitmap
     */
    public Bitmap bitmap;
    public RectF bitmapRectF;

    public Ball(float radius, float centerX, float centerY) {
        this.radius = radius;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public Ball(float radius, float centerX, float centerY, Paint paint) {
        this.radius = radius;
        this.centerX = centerX;
        this.centerY = centerY;
        this.paint = paint;
    }

    public Ball(float radius, float centerX, float centerY, Bitmap bitmap) {
        this.radius = radius;
        this.centerX = centerX;
        this.centerY = centerY;
        this.bitmap = bitmap;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    /**
     * 优先使用bitmap绘制
     */
    public void draw(Canvas canvas) {
        if (null == bitmap) {
            canvas.drawCircle(centerX, centerY, radius, paint);
        } else {
            bitmapRectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
            canvas.drawBitmap(bitmap, null, bitmapRectF, null);
        }
    }



    /**
     * 计算小球是否越界，超出边界调整其位置并改变运动方向
     *
     * @param width  边界的宽
     * @param height 边界的高
     */
    public void calculateBallIsOutOfBounds(float width, float height) {
        float edgeDistance = centerX + radius - width;
        if (edgeDistance > 0) {
            //超出右边界
            centerX = width - radius;
            //向左移动
            vx *= bounce;
        }
        edgeDistance = this.centerX - radius;
        if (edgeDistance < 0) {
            //超出左边界
            centerX = radius;
            //向右移动
            vx *= bounce;
        }
        edgeDistance = centerY + radius - height;
        if (edgeDistance > 0) {
            //超出下边界
            centerY = height - radius;
            vy *= bounce;
        }
        edgeDistance = centerY - radius;
        if (edgeDistance < 0) {
            //超出上边界
            centerY = radius;
            vy *= bounce;
        }
    }

    /**
     * 计算小球下一步的位置
     */
    public void calculateNextPositionOfBall() {
        this.vy += gravity;
        this.centerX += vx;
        this.centerY += vy;
    }

}
