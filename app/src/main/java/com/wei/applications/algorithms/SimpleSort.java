package com.wei.applications.algorithms;

/**
 * 简单排序，包括：冒泡排序、选择排序、插入排序
 * Created by WEI on 2016/7/15.
 */
public class SimpleSort extends BaseSort
{
    public static void main(String[] args)
    {
        SimpleSort simpleSort = new SimpleSort();
//        simpleSort.bubbleSort();
//        simpleSort.selectSort();
        simpleSort.insertionSort();
    }

    // 冒泡排序
    private void bubbleSort()
    {
        outPut("\n--- 冒泡排序 ---\n");
        int length = testArray.length;
        for (int i = 0; i < length; i ++)
        {
            outPut(testArray[i] + ",");
        }

        // 思路：最小项放最左边，下标为0的位置；最大项放最右边，下标为length-1的位置
        // 外层for循环的计算器从后向前，每次循环后外层-1.下标大于outIndex的数据项都已经是排好序的了，不需再比较
        for (int outIndex = length-1; outIndex > 1; outIndex --)
        {
            for (int inIndex = 0; inIndex < outIndex; inIndex ++) // 下标大于outIndex的数据项都已经是排好序的了，不需再比较
            {
                if (testArray[inIndex] > testArray[inIndex+1])
                {
                    swap(inIndex, inIndex + 1);
                }
            }
        }
        outPut("\n");
        for (int i = 0; i < length; i ++)
        {
            outPut(testArray[i] + ",");
        }
        outPut("\n --- 冒泡排序END ---\n");
    }

    // 选择排序
    private void selectSort()
    {
        outPut("\n--- 选择排序 ---\n");
        outPutArray(testArray);
        outPut("\n");
        int minIndex, size;
        size = testArray.length;
        // 1.找出所有中最小的，与第一位交换，第一位变成有序的
        // 2.从第二位开始，找出最小的与第二位交换，前两位是有序的
        // 3.从第三位开始，找出最小的与第三位交换，前三位是有序的；如此往复直到所有数据都有序
        for (int out = 0; out < size - 1; out ++)
        {
            minIndex = out;
            // out之前的都是有序的，无需再比较；找出out之后中最小的数与out位置交换
            for (int in = out + 1; in < size; in ++)
            {
                if (testArray[in] < testArray[minIndex])
                {
                    minIndex = in;
                }
            }
            swap(out, minIndex);
        }

        for (int i = 0; i < size; i ++)
        {
            outPut(testArray[i] + ",");
        }

        outPut("\n--- 选择排序END ---\n");
    }

    // 插入排序
    private void insertionSort()
    {
        outPut("\n--- 插入排序 ---\n");
        int in, out, size = testArray.length;

        outPutArray(testArray);
        outPut("\n");
        for (out = 1; out < size; out ++)
        {
            int temp = testArray[out];
            in = out;
            for (; in > 0; in --)
            {
                if (testArray[in - 1] >= temp)
                {
                    testArray[in] = testArray[in - 1];
                }
                else
                {
                    break;
                }
            }
            // 注释的代码比较简洁
//            while (in > 0 && testArray[in-1] >= temp) // 从out开始向左查找能插入temp的位置
//            {
//                testArray[in] = testArray[in - 1]; // 如果不小于temp，则右移
//                --in;  // 左移继续判断能否插入temp
//            }
            testArray[in] = temp;
//            outPutArray(testArray);
//            outPut("\n");
        }
        outPutArray(testArray);
        outPut("\n--- 插入排序END ---\n");
    }

}
