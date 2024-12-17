package com.example.eis2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class YearPickerDialog extends Dialog {

    private NumberPicker yearPicker;
    private Button selectButton;
    private YearPickerListener yearPickerListener;

    public TextView keterangan;

    public YearPickerDialog(Context context, YearPickerListener listener) {
        super(context);
        this.yearPickerListener = listener;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.year_picker_dialog);
        keterangan = findViewById(R.id.keterangan);
        yearPicker = findViewById(R.id.yearPicker);
        selectButton = findViewById(R.id.selectButton);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String currentDateandTime = sdf.format(new Date());


        yearPicker.setMinValue(1900);  // Set your desired range
        yearPicker.setMaxValue(3000);  // Set your desired range
        yearPicker.setValue(Integer.parseInt(currentDateandTime));     // Set an initial value

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedYear = yearPicker.getValue();
                if (yearPickerListener != null) {
                    yearPickerListener.onYearSelected(selectedYear);
                }
                dismiss();
            }
        });
    }

    public interface YearPickerListener {
        void onYearSelected(int year);
    }
}
