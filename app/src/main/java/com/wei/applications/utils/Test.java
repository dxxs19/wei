package com.wei.applications.utils;

/**
 * Created by WEI on 2016/6/17.
 */
public class Test
{
    private static final String URL = "/my_order.htm?orderStatus=0&token=asdfghjkl&organizationSeq=4400100001";
    private final static double target = 2.1;

    public static void main(String[] args)
    {
//        System.out.println(URL);
        new Test().parseUrl();
        double ceil = Math.ceil(target);
        double floor = Math.floor(target);
        double round = Math.round(target);
        System.out.print("ceil = " + ceil + ", floor = " + floor + ", round = " + round);
    }

    private void parseUrl()
    {
//        String[] strArray = URL.split("=");
//        System.out.println(strArray[1]);
        int index = URL.lastIndexOf("orderStatus");
        String target = URL.substring(index + "orderStatus=".length(), index + "orderStatus=".length()+1);
        System.out.println("target = " + target);
    }
}
