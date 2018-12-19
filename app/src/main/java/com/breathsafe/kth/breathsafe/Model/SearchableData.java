package com.breathsafe.kth.breathsafe.Model;

import java.util.ArrayList;
import java.util.List;

public class SearchableData {
    private static SearchableData searchableData;
    private List<Searchable> list;

    private SearchableData() {
        list = new ArrayList<>();
    }

    public static SearchableData getInstance() {
        if (searchableData == null)
            searchableData = new SearchableData();
        return searchableData;
    }

    public List<Searchable> getList() {
        return list;
    }

    public void setList(List<Searchable> list) {
        this.list = list;
    }
}
