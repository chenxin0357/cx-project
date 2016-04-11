package com.cx.fragmentdemo;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by chenxin on 16/3/30.
 */
public class MsgFragment extends Fragment {
    private ImageView imgView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.msg_fragment, container, false);
        imgView = (ImageView) view.findViewById(R.id.img_slide);
        view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initBitmap();
            }
        });
        return view;
    }

    private void initBitmap() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inJustDecodeBounds = false;// 禁止为Bitmap分配内存，不返回Bitmap对象，返回null 用于获取图片信息，如宽高！
        //options.inSampleSize = 4;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.slide_1, options);

        imgView.setImageBitmap(bitmap);

//        getResources().openRawResource(R.drawable.slide_1, null);
    }

}
