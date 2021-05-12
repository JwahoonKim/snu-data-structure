// 대부분의 sorting 함수를 교수님의 강의ppt를 바탕으로 참고하여 짰습니다.

import java.io.*;
import java.util.*;


public class SortingTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try
		{
			boolean isRandom = false;	// 입력받은 배열이 난수인가 아닌가?
			int[] value;	// 입력 받을 숫자들의 배열
			String nums = br.readLine();	// 첫 줄을 입력 받음
			if (nums.charAt(0) == 'r')
			{
				// 난수일 경우
				isRandom = true;	// 난수임을 표시

				String[] nums_arg = nums.split(" ");

				int numsize = Integer.parseInt(nums_arg[1]);	// 총 갯수
				int rminimum = Integer.parseInt(nums_arg[2]);	// 최소값
				int rmaximum = Integer.parseInt(nums_arg[3]);	// 최대값

				Random rand = new Random();	// 난수 인스턴스를 생성한다.

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 각각의 배열에 난수를 생성하여 대입
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
			}
			else
			{
				// 난수가 아닐 경우
				int numsize = Integer.parseInt(nums);

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 한줄씩 입력받아 배열원소로 대입
					value[i] = Integer.parseInt(br.readLine());
			}

			// 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
			while (true)
			{
				int[] newvalue = (int[])value.clone();	// 원래 값의 보호를 위해 복사본을 생성한다.

				String command = br.readLine();

				long t = System.currentTimeMillis();
				switch (command.charAt(0))
				{
					case 'B':	// Bubble Sort
						newvalue = DoBubbleSort(newvalue);
						break;
					case 'I':	// Insertion Sort
						newvalue = DoInsertionSort(newvalue);
						break;
					case 'H':	// Heap Sort
						newvalue = DoHeapSort(newvalue);
						break;
					case 'M':	// Merge Sort
						newvalue = DoMergeSort(newvalue);
						break;
					case 'Q':	// Quick Sort
						newvalue = DoQuickSort(newvalue);
						break;
					case 'R':	// Radix Sort
						newvalue = DoRadixSort(newvalue);
						break;
					case 'X':
						return;	// 프로그램을 종료한다.
					default:
						throw new IOException("잘못된 정렬 방법을 입력했습니다.");
				}
				if (isRandom)
				{
					// 난수일 경우 수행시간을 출력한다.
					System.out.println((System.currentTimeMillis() - t) + " ms");
				}
				else
				{
					// 난수가 아닐 경우 정렬된 결과값을 출력한다.
					for (int i = 0; i < newvalue.length; i++)
					{
						System.out.println(newvalue[i]);
					}
				}

			}
		}
		catch (IOException e)
		{
			System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoBubbleSort(int[] value)
	{
		// TODO : Bubble Sort 를 구현하라.
		// value는 정렬안된 숫자들의 배열이며 value.length 는 배열의 크기가 된다.
		// 결과로 정렬된 배열은 리턴해 주어야 하며, 두가지 방법이 있으므로 잘 생각해서 사용할것.
		// 주어진 value 배열에서 안의 값만을 바꾸고 value를 다시 리턴하거나
		// 같은 크기의 새로운 배열을 만들어 그 배열을 리턴할 수도 있다.
		int size = value.length;
		for(int last = size - 1; 0 < last; last--) {
			for(int i = 0; i < last; i++) {
				if(value[i] > value[i + 1]) {
					int temp = value[i + 1];
					value[i + 1] = value[i];
					value[i] = temp;
				}
			}
		}
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoInsertionSort(int[] value)
	{
		// TODO : Insertion Sort 를 구현하라.
		int size = value.length;
		for(int i = 1; i < size; i++) {
			int j = i - 1;
			int insertionItem = value[i];
			while(0 <= j && insertionItem < value[j]) {
				value[j + 1] = value[j];
				j--;
			}
		value[j + 1] = insertionItem;
		}
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static int[] DoHeapSort(int[] value)
	{
		// TODO : Heap Sort 를 구현하라.
		Heap heapValue = new Heap(value);
		heapValue.buildHeap();
		for(int i = 0; i <= value.length - 1; i++) {
			heapValue.deleteMax();
		}
		int[] sortedValue = heapValue.A;
		return (sortedValue);
	}	

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoMergeSort(int[] value) {
		return DoMergeSort2(value, 0, value.length - 1);
	}
	
	private static int[] DoMergeSort2(int[] value, int left, int right)
	{ 
		// TODO : Merge Sort 를 구현하라.
		if(left < right) {
			int mid = (left + right) / 2;
			DoMergeSort2(value, left, mid);
			DoMergeSort2(value, mid + 1, right);
			merge(value, left, mid, right);
		}
		return (value);
	}
	
	//merge 함수 구현
	private static void merge(int[] A, int left, int mid, int right) {
		int[] tmp = new int[right - left + 1];
		int leftCur = left;
		int rightCur = mid + 1;
		int tmpCur = 0;
		while(leftCur <= mid && rightCur <= right) {
			if(A[leftCur] < A[rightCur])
				tmp[tmpCur++] = A[leftCur++];
			else {
				tmp[tmpCur++] = A[rightCur++];
			}
		}
		while(leftCur <= mid)
			tmp[tmpCur++] = A[leftCur++];
		while(rightCur <= right)
			tmp[tmpCur++] = A[rightCur++];
		leftCur = left;
		tmpCur = 0;
		while(leftCur <= right) 
			A[leftCur++] = tmp[tmpCur++];
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoQuickSort(int[] value) {
		return DoQuickSort2(value, 0, value.length - 1);
	}
	
	private static int[] DoQuickSort2(int[] value, int left, int right)
	{
		// TODO : Quick Sort 를 구현하라.
		if(left < right) {
			// partiton
			int mid = partition(value, left, right);
			// left sort left ~ mid - 1
			DoQuickSort2(value, left, mid - 1);
			// right sort mid ~ right
			DoQuickSort2(value, mid, right);
		}
		
		return (value);
	}
	
	//partition 함수 구현
	private static int partition(int[] A, int left, int right) {
		int pivot = A[right];
		int leftend = left - 1;
		for(int i = left; i < right; i ++ ) {
			if(A[i] <= pivot) {
				leftend ++;
				int tmp = A[i];
				A[i] = A[leftend];
				A[leftend] = tmp;
			}
		}
		int tmp = A[leftend + 1];
		A[leftend + 1] = pivot;
		A[right] = tmp;
		
		return leftend + 1;  
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoRadixSort(int[] value)
	{
		// TODO : Radix Sort 를 구현하라.
		int[] counter = new int[19];
		int[] bucket = new int[value.length];
		
		int digitNum = 0;
		
		for(int i=0; i < value.length; i++) {
			int now = value[i] > 0 ? value[i] : -value[i];
			digitNum = Math.max(digitNum, (int)(Math.log10(now)+1)); 
		}
		
		int maxDigit = (int) Math.pow(10, digitNum - 1);
		
		for(int digit = 1; digit <= maxDigit; digit *= 10) {
			Arrays.fill(counter, 0);
			
			for(int i = 0; i < value.length; i++) {
				// -9 --> 0 ... 9 --> 18로 인덱싱되게
				int num = (value[i] / digit) % 10 + 9;
				counter[num]++ ;
			}
			
			for(int j = 1; j < 19; j++) {
				counter[j] += counter[j - 1];
			}
			
			for(int k = value.length - 1; k >= 0; k--) {
				int num = (value[k] / digit) % 10 + 9;
				bucket[--counter[num]] = value[k];
			}

			int[] tmp = value;
			value = bucket;
			bucket = tmp;			
		}
		
		return (value);
	}
}

class Heap{
	public int[] A;
	public int numItems;
	
	public Heap(int[] arr) {
		A = arr;
		numItems = arr.length;
	}
	
	//buildHeap 함수 구현
	public void buildHeap() {
		if(numItems >= 2) {
			for(int i = (numItems - 2) / 2; i >= 0; i --) {
				percolateDown(i);
			}
		}
	}
	//percorateDown 함수 구현
	private void percolateDown(int i) {
		int child = 2 * i + 1;
		int rightChild = 2 * i + 2;
		if(child <= numItems - 1) {
			if((rightChild <= numItems - 1) && (A[child] < A[rightChild])) {
				child = rightChild;
				}
		
			if(A[i] < A[child]) {
				int tmp = A[i];
				A[i] = A[child];
				A[child] = tmp;
				percolateDown(child);
			}
		}
	}
	//deleteMax 함수 구현
	public int deleteMax() {
		int max = A[0];
		A[0] = A[numItems - 1];
		A[numItems - 1] = max;
		numItems --;
		percolateDown(0);
		return max;
	}	
}


