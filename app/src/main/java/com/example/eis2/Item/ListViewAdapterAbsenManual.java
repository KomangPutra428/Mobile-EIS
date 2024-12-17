package com.example.eis2.Item;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.eis2.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ListViewAdapterAbsenManual extends ArrayAdapter<absenmanualmodel> {

    private List<absenmanualmodel> AbsensiList;

    private Context context;

    public ListViewAdapterAbsenManual(List<absenmanualmodel> AbsensiList, Context context) {
        super(context, R.layout.list_item_absen_manual, AbsensiList);
        this.AbsensiList = AbsensiList;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View listViewItem = inflater.inflate(R.layout.list_item_absen_manual, null, true);

        TextView tanggal = listViewItem.findViewById(R.id.tanggalid);
        TextView id = listViewItem.findViewById(R.id.id);
        TextView type = listViewItem.findViewById(R.id.type);
        TextView absen = listViewItem.findViewById(R.id.absen);
        TextView waktuabsen = listViewItem.findViewById(R.id.waktuabsen);
        TextView ket = listViewItem.findViewById(R.id.ket);
        ImageView approval1 = listViewItem.findViewById(R.id.approval1);
        ImageView approval2 = listViewItem.findViewById(R.id.approval2);

        absenmanualmodel movieItem = AbsensiList.get(position);
        tanggal.setText(tanggal(movieItem.getTanggal_absen()));
        id.setText(movieItem.getId_absen());
        type.setText(movieItem.getJenis_absen());
        absen.setText(tanggal(movieItem.getTanggal_absen()));
        waktuabsen.setText(movieItem.getWaktu_absen());
        ket.setText(movieItem.getKet_absen());


        if ("0".equalsIgnoreCase(movieItem.getStatus())){
            approval1.setImageResource(R.drawable.btn_open);
        }
        if ("1".equalsIgnoreCase(movieItem.getStatus())){
            approval1.setImageResource(R.drawable.btn_aprv);
        }
        if ("2".equalsIgnoreCase(movieItem.getStatus())){
            approval1.setImageResource(R.drawable.btn_notaprv);
        }
        if ("3".equalsIgnoreCase(movieItem.getStatus())){
            approval1.setImageResource(R.drawable.btn_hangus);
        }

        if ("0".equalsIgnoreCase(movieItem.getStatus_2())){
            approval2.setImageResource(R.drawable.btn_open);
        }
        if ("1".equalsIgnoreCase(movieItem.getStatus_2())){
            approval2.setImageResource(R.drawable.btn_open);
        }
        if ("2".equalsIgnoreCase(movieItem.getStatus_2())){
            approval2.setImageResource(R.drawable.btn_open);
        }
        if ("3".equalsIgnoreCase(movieItem.getStatus_2())){
            approval2.setImageResource(R.drawable.btn_open);
        }


        return listViewItem;
    }

    public static String tanggal(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        return convetDateFormat.format(date);
    }
}

