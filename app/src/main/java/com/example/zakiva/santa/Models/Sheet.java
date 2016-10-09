package com.example.zakiva.santa.Models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zakiva on 10/9/16.
 */

public class Sheet {

    public String name;
    public ArrayList<HashMap<String, Object>> data;

    public Sheet() {}

    public Sheet (String name, ArrayList<HashMap<String, Object>> data) {
        this.name = name;
        this.data = data;
    }

}
