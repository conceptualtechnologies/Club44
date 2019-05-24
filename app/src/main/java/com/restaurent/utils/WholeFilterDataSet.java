package com.restaurent.utils;

import java.util.ArrayList;

public class WholeFilterDataSet {

    public ArrayList<FilterDataSet> mTitleList;
    public ArrayList<ArrayList<FilterDataSet>> mChildList;
    public WholeFilterDataSet(ArrayList<FilterDataSet> mTitleList, ArrayList<ArrayList<FilterDataSet>> mChildList){
        this.mTitleList=mTitleList;
        this.mChildList=mChildList;
    }
}
