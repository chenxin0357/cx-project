package com.cx.fragmentdemo;

import android.content.Intent;
import android.test.InstrumentationTestCase;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by chenxin on 16/3/31.
 */
public class TitleFragmentTest extends InstrumentationTestCase {
    private MainActivity mainAct;
    private Button btn;
    private TextView text;

    /**
     * 用来初始设置。 初始化资源等
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Intent intent = new Intent();
        intent.setClassName("com.cx.fragmentdemo", MainActivity.class.getName());

        mainAct = (MainActivity) getInstrumentation().startActivitySync(intent);

    }

    /**
     * 用来清理垃圾和资源回收
     * @throws Exception
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * 模拟点击事件
     *  需要重启一个线程，直接在UI线程运行可能会阻塞UI线程
     */
    private class BtnClick implements Runnable{

        public BtnClick(Button button) {
            btn = button;
        }

        @Override
        public void run() {
            btn.performClick();
        }
    }
}
