package com.eme22.kumaanime.AppUtils.Mods;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

public class OnItemClickSpinner extends androidx.appcompat.widget.AppCompatSpinner implements AdapterView.OnItemSelectedListener {

    boolean spinnerTouched = false;
    OnItemClickListener onItemClickListener = null;
    OnItemSelectedListener onItemSelectedListener = null;

    public OnItemClickSpinner(Context context) {
        super(context);
        super.setOnItemSelectedListener(this);
    }

    public OnItemClickSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOnItemSelectedListener(this);
    }

    public OnItemClickSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        spinnerTouched = true;
        return super.onTouchEvent(event);
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    @Override
    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
        super.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(spinnerTouched && this.onItemClickListener!=null) this.onItemClickListener.onItemClick(parent,view,position,id);
        if(this.onItemSelectedListener!=null) this.onItemSelectedListener.onItemSelected(parent,view,position,id);
        spinnerTouched=false;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if(this.onItemSelectedListener!=null) this.onItemSelectedListener.onNothingSelected(parent);
        spinnerTouched=false;
    }
}
