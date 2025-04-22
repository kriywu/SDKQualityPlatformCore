package com.wurengao.android.targetsdk;

/**
 * Created by wurengao on 2025/4/22
 *
 * @author wurengao@bytedance.com
 */
public class MathSDK {

    private MathSDK() {
    }

    public static MathSDK createEngine() {
        return new MathSDK();
    }

    public int plus(int leftValue, int rightValue) {
        return leftValue + rightValue;
    }

    public int destroyEngine() {
        return 0;
    }
}