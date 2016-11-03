package com.wei.applications.algorithms;

/**
 * Created by WEI on 2016/7/15.
 */
public interface IAdvancedSort
{
    /**
     * 希尔排序
     */
    void shellSort();

    /**
     * 快速排序
     * @param left
     * @param right
     */
    void recQuickSort(int left, int right);
}
