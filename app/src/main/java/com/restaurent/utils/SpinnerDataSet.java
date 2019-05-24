package com.restaurent.utils;

public class SpinnerDataSet {

    String _name;
    Integer _id;
    String initialText;

    public SpinnerDataSet(String name, Integer id){
        this._name=name;
        this._id=id;
    }
    public SpinnerDataSet(){
        this.initialText = "--- Please Select ---";
    }
    public String get_name() {
        return _name;
    }

    public Integer get_id() {
        return _id;
    }
    public String getInitialText() {
        return initialText;
    }

}
