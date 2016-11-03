package com.wei.applications.algorithms;

/**
 * 高级排序：希尔排序、快速排序、基数排序
 * Created by WEI on 2016/7/15.
 */
public class AdvancedSort extends BaseSort implements IAdvancedSort
{
    public static void main(String[] args)
    {
        IAdvancedSort advancedSort = new AdvancedSort();
        advancedSort.shellSort();
    }

    @Override
    public void shellSort()
    {
        outPut("\n--- 希尔排序 ---\n");
        outPutArray(testArray);

        int inner, outer, size = testArray.length;
        int temp;

        int h = 1;
        while (h <= size/3)
        {
            h = h * 3 + 1;
            outPut("\n h = " + h);
        }

        while (h > 0)
        {
            for (outer = h ; outer < size ; outer ++)
            {
                temp = testArray[outer];
                inner = outer;
                while (inner > h-1 && testArray[inner-h] >= temp)
                {
                    testArray[inner] = testArray[inner-h];
                    inner -= h;
                }
                testArray[inner] = temp;
            }
            h = (h-1)/3;
        }

        outPut("\n");
        outPutArray(testArray);
        outPut("\n--- 希尔排序END ---\n");
    }

    @Override
    public void recQuickSort(int left, int right)
    {

    }
}
