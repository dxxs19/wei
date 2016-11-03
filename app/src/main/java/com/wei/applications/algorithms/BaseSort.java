package com.wei.applications.algorithms;

/**
 * Created by WEI on 2016/7/8.
 */
public class BaseSort
{
    protected int[] testArray = {45, 34, 13, 90, 12, 47, 8, 3, 65, 23};

    protected void outPut(Object target)
    {

        System.out.print(target + "");
    }

    protected void outPutArray(int[] array)
    {
        for (int i = 0; i < array.length; i ++)
        {
            outPut(array[i] + ",");
        }
    }

    protected void swap(int i, int j)
    {
        int temp = testArray[i];
        testArray[i] = testArray[j];
        testArray[j] = temp;
    }
}
