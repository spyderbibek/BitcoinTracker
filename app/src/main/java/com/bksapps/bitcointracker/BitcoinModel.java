package com.bksapps.bitcointracker;

import org.json.JSONObject;

/**
 * Created by spyde on 7/16/2017.
 */

public class BitcoinModel {
    private double mPrice;

    public static BitcoinModel fromJSON(JSONObject jsonObject){

        BitcoinModel bitcoinModel=new BitcoinModel();

        try{
            bitcoinModel.mPrice= jsonObject.getDouble("price");
            return bitcoinModel;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public double getPrice() {
        return mPrice;
    }
}
