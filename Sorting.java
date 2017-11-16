/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorting;
import java.util.Arrays;
import java.lang.management.ThreadMXBean;
import java.lang.management.ManagementFactory;

/**
 *
 * @author brianhaufler
 */
public class Sorting {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Initialization of variables (so that they don't have to be
        // initialized over and over within the for loops
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long mergeSortTime, heapSortTime, quickSortTime;
        long startTime, endTime, elapsedTime;
        
        // Initializes the number of elements in the array
        int n = 2;
        
        // Runs forever (stop when it takes to long to complete)
        while (true) {
            
            
            long[] newArray1 = new long[n];
            long[] newArray2 = new long[n];
            long[] newArray3 = new long[n];
            mergeSortTime = 0;
            heapSortTime = 0;
            quickSortTime = 0;
            
            // Runs 1000000 times to get the averages
            for (int k=0; k<=100000; k++) {
                
                // Creates 3 new arrays with n numbers
                for (int i=0; i<n; i++) {
                    newArray1[i] = (long) (Math.random() * Long.MAX_VALUE);
                }
                System.arraycopy(newArray1, 0, newArray2, 0, newArray1.length);
                System.arraycopy(newArray1, 0, newArray3, 0, newArray1.length);
                
                // MERGESORT PERFORMANCE
                startTime = bean.getCurrentThreadCpuTime();
                mergeSort(newArray1);
                endTime = bean.getCurrentThreadCpuTime();
                // elapsed time in nanoseconds
                elapsedTime = endTime-startTime;
                mergeSortTime += elapsedTime;
                
                // HEAPSORT PERFORMANCE
                startTime = bean.getCurrentThreadCpuTime();
                heapSort(newArray2);
                endTime = bean.getCurrentThreadCpuTime();
                // elapsed time in nanoseconds
                elapsedTime = endTime-startTime;
                heapSortTime += elapsedTime;
                
                // QUICKSORT PERFORMANCE
                startTime = bean.getCurrentThreadCpuTime();
                quickSort(newArray3);
                endTime = bean.getCurrentThreadCpuTime();
                // elapsed time in nanoseconds
                elapsedTime = endTime-startTime;
                quickSortTime += elapsedTime;
                
            }
            
            System.out.println("Array size: " + n);
            // 100,000 trials were run so to find the average run time, 
            // we need to divide the total time by 100,000
            System.out.println("MergeSort average run time in nanoseconds: " 
                    + mergeSortTime/100000);
            System.out.println("HeapSort average run time in nanoseconds: " 
                    + heapSortTime/100000);
            System.out.println("QuickSort average run time in nanoseconds: " 
                    + quickSortTime/100000);
            
            // Doubles the array and we'll repeat
            n = n * 2;
        }
    }
    
    static long[] mergeSort(long[] a) {
        
        long[] firstHalf;
        long[] secondHalf;
        int length = a.length;
        
        // Divide Stage
        // -------------------------------------------------------
        // If array contains more than 1 element than recursively
        // call mergeSort in order to split the array into more
        // easily sortable chunks
        if (length > 1) {
            int halfway = (int) Math.ceil(length/2);
            // Call merge sort on first half of array
            // Returns sorted first half
            firstHalf = mergeSort(Arrays.copyOfRange(a, 0, halfway));
            // Call merge sort on second half of array
            // Returns sorted second half
            secondHalf = mergeSort(Arrays.copyOfRange(a, halfway, length));
        } else {
        // If the array has 1 element then it is already trivially
        // sorted and we can return
            return a;
        }
        
        // Conquer (Merge) Stage
        // --------------------------------------
        long[] mergedArray = new long[length];
        int firstHalfLength = firstHalf.length;
        int secondHalfLength = secondHalf.length;
        
        
        // Creates counter that we will increment over course of while loop
        int i=0;
        int j=0;
        // We compare one-to-one the beginnings of each half of the array,
        // adding the smaller element to the beginning of the merged array,
        // and moving down the arrays until one array is exhausted
        while (i<firstHalfLength && j<secondHalfLength) {
            if (firstHalf[i]<secondHalf[j]) {
                mergedArray[i+j] = firstHalf[i];
                i++;
            } else {
                mergedArray[i+j] = secondHalf[j];
                j++;
            }
        }
        
        // For the array that still has elements remaining, we add them to
        // the merged array
        if (i<j) {
            while (i<firstHalfLength) {
                mergedArray[i+j] = firstHalf[i];
                i++;
            }
        } else {
            while (j<secondHalfLength) {
                mergedArray[i+j] = secondHalf[j];
                j++;
            }
        }
        
        // We then return the merged array
        return mergedArray;
    }
    
    static long[] heapSort(long[] a) {
        // Builds max heap at start
        buildMaxHeap(a);
        
        // Runs until every element in the array is sorted
        for (int i=a.length-1; i>=1; i--) {
            // Max value in max heap is at start, so put that at the end
            swap(a, 0, i);
            
            // Since the max value was moved, the max heap property may have
            // been violated. We need to fix that
            maxHeapify(a, 0, i-1);
        }
        
        // Returns a sorted array
        return a;
    }
    
    static void buildMaxHeap(long[] a) {
        int heapSize = a.length-1;
        // Builds max heap by making sure every single tree within the heap
        // is max-heapified
        for (int i=(a.length/2)-1; i>=0; i--) {
            maxHeapify(a, i, heapSize);
        }
    }
    
    static void maxHeapify(long[] a, int i, int heapSize) {
        
        // Assumes that the root is the largest value
        int largest = i;
        
        // Left and right nodes of root are in these locations (if they exist)
        int left = 2*i + 1;
        int right = 2*i + 2;
        
        // If left exists and is greater than the root, it is now the largest
        if (left <= heapSize && a[left] > a[i]) {
            largest = left;
        }
        // If right exists and is greater than the root (or left, if left was
        // greater than the root), it is now the largest
        if (right <= heapSize && a[right] > a[largest]) {
            largest = right;
        }
        // If the root wasn't the largest, we'll need to swap it with whatever
        // element was the largest
        if (largest != i) {
            swap(a, i, largest);
            // Now that we've swapped it, the heap-property might be violated.
            // Better fix that
            maxHeapify(a, largest, heapSize);
        }
        
    }

    static long[] quickSort(long[] a) {
        // Initializes the quicksort call
        newQuickSort(a, 0, a.length-1);
        
        // Returns the sorted array
        return a;
    }
    
    static void newQuickSort(long[] a, int p, int r) {
        // Runs while there are still numbers between the boundaries p and r
        if (p<r) {
            // This returns where the pivot is located
            int q = partition(a, p, r);
            
            // Partitions all the values less than the pivot
            newQuickSort(a, p, q-1);
            
            // Partitions all the values greater than the pivot
            newQuickSort(a, q+1, r);
        }
    }
    
    
    static int partition(long[] a, int p, int r) {
        // Sets the pivot to the last value in the section of the array that
        // we're partitioning
        long pivot = a[r];
        
        // Sets the line that divides the longs in the array that are
        // greater and less than the pivot. Not partitioned yet, so at very 
        // beginning
        int i = p-1;
        
        // Goes through array and if a number is less or equal to the pivot, it
        // moves it before the dividing line and advances the dividing line
        for (int j=p; j<r; j++) {
            if (a[j] <= pivot) {
                i = i+1;
                swap(a, i, j);
            }
        }
        // Puts the pivot right after the dividing line since it is just greater
        // (or equal) to all the numbers before it
        swap(a, i+1, r);
        
        // Returns where the pivot is located
        return i+1;   
    }
    
    
    // Method that swaps the value in the array at a[i] with the value
    // at a[j] and vice-versa
    static void swap (long[] a, int i, int j) {
        // temporarily stores a[i] value since that's about to change
        long tempVal = a[i];
        a[i] = a[j];
        a[j] = tempVal;
    }
}
