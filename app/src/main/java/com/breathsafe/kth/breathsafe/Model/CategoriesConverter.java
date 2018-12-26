package com.breathsafe.kth.breathsafe.Model;

import android.arch.persistence.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

public class CategoriesConverter {
    @TypeConverter
    public Categories storedStringToCategories(String value) {
        List<String> langs = Arrays.asList(value.split("\\s*,\\s*"));
        return new Categories(langs);
    }

    @TypeConverter
    public String categoriesToStoredString(Categories categories) {
        String res = "";
        for (String c :categories.getCategories())
            res += c + ",";
        return res;
    }
}
