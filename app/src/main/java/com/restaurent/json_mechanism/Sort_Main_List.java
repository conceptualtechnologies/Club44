package com.restaurent.json_mechanism;


import com.restaurent.utils.CategoryDataSet;

import java.util.ArrayList;
import java.util.Arrays;

class Sort_Main_List {

    static ArrayList<ArrayList<CategoryDataSet>> getHomeListSorted(ArrayList<CategoryDataSet> parentList, ArrayList<ArrayList<CategoryDataSet>> childList) {
        //Try to fix this (Order appears wrongly)
        ArrayList<ArrayList<CategoryDataSet>> result = new ArrayList<>();
        for (int i = 0; i < parentList.size(); i++) {
            for (int j = 0; j < childList.size(); j++) {
                if (parentList.get(i).getCategory_id() == childList.get(j).get(childList.get(j).size() - 1).getParent_id() &&
                        parentList.get(j).getMarked() && childList.get(j).get(childList.get(j).size() - 1).getMarked()) {
                    result.add(childList.get(j));
                    parentList.get(j).setMarked(false);
                    childList.get(j).get(childList.get(j).size() - 1).setMarked(false);
                }
            }
        }
        return result;
    }

    static ArrayList<CategoryDataSet> getParentSortedLists(ArrayList<CategoryDataSet> mPParentList) {
        ArrayList<CategoryDataSet> mParentList = new ArrayList<>();

        int values[] = new int[mPParentList.size()];
        for (int i = 0; i < mPParentList.size(); i++) {
            values[i] = mPParentList.get(i).getSort_order();
        }

        Arrays.sort(values);

        for (int value : values) {
            for (int j = 0; j < values.length; j++) {
                if (value == mPParentList.get(j).getSort_order() && mPParentList.get(j).getMarked()) {
                    CategoryDataSet parentCategory = new CategoryDataSet();
                    parentCategory.setName(mPParentList.get(j).getName());
                    parentCategory.setCategory_id(mPParentList.get(j).getCategory_id());
                    parentCategory.setTop(mPParentList.get(j).getTop());
                    parentCategory.setSort_order(mPParentList.get(j).getSort_order());
                    parentCategory.setImage(mPParentList.get(j).getImage());
                    mPParentList.get(j).setMarked(false);
                    parentCategory.setMarked(true);
                    mParentList.add(parentCategory);
                }
            }
        }
        return mParentList;
    }


    static ArrayList<CategoryDataSet> getChildSortedLists(ArrayList<CategoryDataSet> mPChildList) {
        ArrayList<CategoryDataSet> mChildList = new ArrayList<>();
        ArrayList<CategoryDataSet> mSecond = new ArrayList<>();
        int k = 0;

        int values[] = new int[mPChildList.size()];
        for (int i = 0; i < mPChildList.size(); i++) {
            values[i] = mPChildList.get(i).getSort_order();
        }

        Arrays.sort(values);

        for (int value : values) {
            for (int j = 0; j < values.length; j++) {
                if (value == mPChildList.get(j).getSort_order() && mPChildList.get(j).getMarked()) {
                    if (mPChildList.get(j).getTop() == 0) {
                        if (mChildList.size() != 0) {
                            if (!mChildList.get(k).getName().equals(mPChildList.get(j).getName()) && mPChildList.get(j).getMarked()) {
                                CategoryDataSet categoryDataSet = new CategoryDataSet();
                                categoryDataSet.setCategory_id(mPChildList.get(j).getCategory_id());
                                categoryDataSet.setSort_order(mPChildList.get(j).getSort_order());
                                categoryDataSet.setTop(mPChildList.get(j).getTop());
                                categoryDataSet.setName(mPChildList.get(j).getName());
                                categoryDataSet.setImage(mPChildList.get(j).getImage());
                                categoryDataSet.setParent_id(mPChildList.get(j).getParent_id());
                                mPChildList.get(j).setMarked(false);
                                categoryDataSet.setMarked(true);
                                mChildList.add(categoryDataSet);
                                k++;
                            }
                        } else if (mChildList.size() == 0 && mPChildList.get(j).getMarked()) {
                            CategoryDataSet categoryDataSet = new CategoryDataSet();
                            categoryDataSet.setCategory_id(mPChildList.get(j).getCategory_id());
                            categoryDataSet.setSort_order(mPChildList.get(j).getSort_order());
                            categoryDataSet.setTop(mPChildList.get(j).getTop());
                            categoryDataSet.setName(mPChildList.get(j).getName());
                            categoryDataSet.setParent_id(mPChildList.get(j).getParent_id());
                            categoryDataSet.setImage(mPChildList.get(j).getImage());
                            mPChildList.get(j).setMarked(false);
                            categoryDataSet.setMarked(true);
                            mChildList.add(categoryDataSet);
                        }

                    } else if (mPChildList.get(j).getMarked()) {
                        CategoryDataSet categoryDataSet = new CategoryDataSet();
                        categoryDataSet.setCategory_id(mPChildList.get(j).getCategory_id());
                        categoryDataSet.setSort_order(mPChildList.get(j).getSort_order());
                        categoryDataSet.setTop(mPChildList.get(j).getTop());
                        categoryDataSet.setName(mPChildList.get(j).getName());
                        categoryDataSet.setImage(mPChildList.get(j).getImage());
                        categoryDataSet.setParent_id(mPChildList.get(j).getParent_id());
                        mPChildList.get(j).setMarked(false);
                        categoryDataSet.setMarked(true);
                        mSecond.add(categoryDataSet);
                    }
                }
            }
        }
        if (mSecond.size() > 0) {
            for (int i = 0; i < mSecond.size(); i++) {
                mChildList.add(mSecond.get(i));
            }
        }
        return mChildList;
    }
}

