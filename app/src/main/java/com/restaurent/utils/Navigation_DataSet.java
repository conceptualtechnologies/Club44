package com.restaurent.utils;

import java.util.ArrayList;

public class Navigation_DataSet {

    public ArrayList<CategoryDataSet> mParentList;
    public ArrayList<ArrayList<CategoryDataSet>> mChildAdder;
    public Navigation_DataSet(ArrayList<CategoryDataSet> mParentList, ArrayList<ArrayList<CategoryDataSet>> mChildAdder){
        this.mParentList=mParentList;
        this.mChildAdder=mChildAdder;
    }
}
