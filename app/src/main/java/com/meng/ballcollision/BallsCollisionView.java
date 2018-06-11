package com.meng.ballcollision;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: mengqingdong
 * Date: 2018/5/21
 * Time: 14:42
 * Description: 小球碰撞
 */

public class BallsCollisionView extends View {
    /**
     * 小球半径 px
     */
    private float radius = 150.0f;
    /**
     * 小球数量
     */
    private int ballCount = 10;
    /**
     * 小球集合
     */
    List<Ball> ballList = new ArrayList<>();

    public BallsCollisionView(Context context) {
        this(context, null);
    }

    public BallsCollisionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BallsCollisionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initBall(float width, float height) throws Exception {
        ballList = createBalls(width, height);
    }

    /**
     * 初始化碰撞小球，默认随机位置，初始化时小球不可以重叠
     * 如果想生成不同样式的小球需要重写此方法
     *
     * @param width  view width
     * @param height view height
     */
    public List<Ball> createBalls(float width, float height) throws Exception {
        float minX = radius;
        float maxX = width - radius;
        float minY = radius;
        float maxY = height - radius;
        List<Ball> balls = new ArrayList<>();
        Paint paint = createBallPaint();
        do {
            float centerX = getRandomIntByRange((int) minX, (int) maxX);
            float centerY = getRandomIntByRange((int) minY, (int) maxY);
            if (!ballIntersectList(radius, centerX, centerY, balls)) {
                Ball ball = new Ball(radius, centerX, centerY, paint);
                balls.add(ball);
            }
        } while (balls.size() < ballCount);
        return balls;
    }

    private Paint createBallPaint() {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#55943a21"));
        return paint;
    }

    /**
     * 判断当前要生成的小球是否与其他球相交
     *
     * @param radius   要生成小球的半径
     * @param centerX  要生成小球圆心 x 坐标
     * @param centerY  要生成小球圆心 y 坐标
     * @param ballList 被比较小球集合
     * @return 有一个相交返回true
     */
    private boolean ballIntersectList(float radius, float centerX, float centerY, List<Ball> ballList) {
        for (Ball temp : ballList) {
            if (ballIntersect(radius, centerX, centerY, temp)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取指定范围内整数 rangeStart要小于rangeEnd
     *
     * @param rangeStart 整数开始值
     * @param rangeEnd   整数结束值
     */
    private int getRandomIntByRange(int rangeStart, int rangeEnd) throws Exception {
        if (rangeStart >= rangeEnd) {
            throw new Exception("rangeStart should less then rangeEnd");
        }
        Random random = new Random();
        return random.nextInt(rangeEnd) % (rangeEnd - rangeStart + 1) + rangeStart;
    }

    /**
     * 判断2个小球是否相交
     *
     * @param ball    第一个小球
     * @param radius  第二个小球的半径
     * @param centerX 第二个小球圆心 x 坐标
     * @param centerY 第二个小球圆心 y 坐标
     * @return 相交返回true
     */
    private boolean ballIntersect(float radius, float centerX, float centerY, Ball ball) {
        double dx = centerX - ball.centerX;
        double dy = centerY - ball.centerY;
        double dist = Math.sqrt(dx * dx + dy * dy);
        return ball.radius + radius > dist;
    }

    /**
     * 判断2个小球是否相交
     *
     * @param ball1 第一个小球
     * @param ball2 第二个小球
     * @return 相交返回true
     */
    private boolean ballIntersect(Ball ball1, Ball ball2) {
        double dx = ball2.centerX - ball1.centerX;
        double dy = ball2.centerY - ball1.centerY;
        double dist = Math.sqrt(dx * dx + dy * dy);
        return ball1.radius + ball2.radius > dist;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (ballList.size() == 0) {
            try {
                initBall(getWidth(), getHeight());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        hitBall();
        for (Ball ball : ballList) {
            ball.calculateNextPositionOfBall();
            ball.calculateBallIsOutOfBounds(getWidth(), getHeight());
        }
        for (Ball ball : ballList) {
            ball.draw(canvas);
        }
        postInvalidate();
    }

    /**
     * 一个球与当前球碰撞
     */
    public void hitBall() {
        int num = ballList.size();
        for (int i = 0; i < num; i++) {
            Ball ball1 = ballList.get(i);
            for (int j = i + 1; j < num; j++) {
                Ball ball2 = ballList.get(j);
                double dx = ball2.centerX - ball1.centerX;
                double dy = ball2.centerY - ball1.centerY;
                double dist = Math.sqrt(dx * dx + dy * dy);
                double misDist = ball1.radius + ball2.radius;
                if (dist < misDist) {
                    double angle = Math.atan2(dy, dx);
                    double tx = ball1.centerX + Math.cos(angle) * misDist;
                    double ty = ball1.centerY + Math.sin(angle) * misDist;
                    double ax = (tx - ball2.centerX) * ball1.spring;
                    double ay = (ty - ball2.centerY) * ball1.spring;
                    ball1.vx -= ax;
                    ball1.vy -= ay;
                    ball2.vx += ax;
                    ball2.vy += ay;
                }
            }
        }
    }
}
