package com.example.eis2.Item;

import com.example.eis2.soalexitmodel;
import com.google.android.gms.common.data.DataHolder;

import java.util.ArrayList;

public class Paginator {

    public static final int TOTAL_NUM_ITEMS = 52;
    public static final int ITEMS_PER_PAGE = 10;
    public static final int ITEMS_REMAINING = TOTAL_NUM_ITEMS % ITEMS_PER_PAGE;
    public static final int LAST_PAGE = TOTAL_NUM_ITEMS / ITEMS_PER_PAGE;

    public ArrayList<String> generatePage(int currentPage) {
        int startItem = currentPage * ITEMS_PER_PAGE + 1;
        int numOfData = ITEMS_PER_PAGE;

        ArrayList<String> pageData = new ArrayList<>();

        if (currentPage == LAST_PAGE && ITEMS_REMAINING > 0) {
            for (int i = startItem; i < startItem + ITEMS_REMAINING; i++) {
                pageData.add("Number " + i);
            }
        } else {
            for (int i = startItem; i < startItem + numOfData; i++) {
                pageData.add("Number " + i);
            }
        }

        return pageData;
    }

    public int getTotalPages() {
        int remainingItems=TOTAL_NUM_ITEMS % ITEMS_PER_PAGE;
        if(remainingItems>0)
        {
            return TOTAL_NUM_ITEMS / ITEMS_PER_PAGE;
        }
        return (TOTAL_NUM_ITEMS / ITEMS_PER_PAGE)-1;

    }


}
