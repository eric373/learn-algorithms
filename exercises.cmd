:: --------------------------------------------------------------------------------
::
:: This script is used to run solutions to selected exercises in Sedgewick -
:: Algorithms 4ed.
::
:: Ensure that CLASSPATH environment variable on windows includes the current
:: directory .\
::
:: --------------------------------------------------------------------------------
cls

::cd exercises\1.3.32
::javac Steque.java
::java Steque
::del *.class
::cd ..\..

::cd exercises\1.3.32
::javac ResizingArraySteque.java
::java ResizingArraySteque
::del *.class
::cd ..\..

::cd exercises\1.3.33
::javac Deque.java
::java Deque
::del *.class
::cd ..\..

::cd exercises\1.3.33
::javac ResizingArrayDeque.java
::java ResizingArrayDeque
::del *.class
::cd ..\..

::cd exercises\1.3.37
::javac Josephus.java
::java Josephus input.txt
::del *.class
::cd ..\..

::cd exercises\1.4.02
::javac BigThreeSum.java
::java BigThreeSum BigInts.txt
::del *.class
::cd ..\..

::cd exercises\1.4.04
::javac TwoSum.java
::Java TwoSum ..\..\algs4-data\2Kints.txt
::del *.class
::cd ..\..

::cd exercises\1.4.08
::javac PairCounter.java
::Java PairCounter input.txt
::del *.class
::cd ..\..

::cd exercises\1.4.10
::javac BinarySearchSmallestIndex.java
::java BinarySearchSmallestIndex 8KIntsWithEvenReps.txt 2 4 6 8 1998
::del *.class
::cd ..\..

::cd exercises\1.5.01
::javac QuickFindUF.java
::java QuickFindUF input.txt
::del *.class
::cd ..\..

::cd exercises\1.5.02
::javac QuickUnionUF.java
::java QuickUnionUF input.txt
::del *.class
::cd ..\..

::cd exercises\1.5.03
::javac WeightedQuickUnionUF.java
::java WeightedQuickUnionUF input.txt
::del *.class
::cd ..\..

::cd exercises\1.5.12
::javac QuickUnionPCUF.java
::java QuickUnionPCUF input.txt
::del *.class
::cd ..\..

cd exercises\2.1.01
javac SelectionSort.java
java SelectionSort input.txt input2.txt ..\..\src\main\resources\PQSampleInput.txt
del *.class
cd ..\..

::cd exercises\2.1.04
::javac InsertionSort.java
::java InsertionSort < input.txt
::java InsertionSort < ..\2.1.05\PQSampleInput.txt
::cd ..\..

::cd exercises\2.1.09
::javac ShellSort.java
::java ShellSort < input.txt
::java ShellSort < input2.txt
::cd ..\..

::cd exercises\2.2.01
::javac Merge.java
::java Merge < input.txt
::cd ..\..

::cd exercises\2.2.02
::javac TopDownMergeSort.java
::java TopDownMergeSort < input.txt
::java TopDownMergeSort < input2.txt
::cd ..\..

::cd exercises\2.2.03
::javac BottomUpMergeSort.java
::java BottomUpMergeSort < input.txt
::java BottomUpMergeSort < input2.txt
::cd ..\..

::cd exercises\2.2.06
::javac MergeSortAAC.java
::java MergeSortAAC
::cd ..\..

::cd exercises\2.2.08
::javac TopDownMergeSort.java
::java TopDownMergeSort < input2.txt
::cd ..\..

::cd exercises\2.3.01
::javac Partition.java
::java Partition < input.txt
::java Partition < input2.txt
::cd ..\..

::cd exercises\2.3.02
::javac QuickSort.java
::java QuickSort < input.txt
::cd ..\..

::cd exercises\2.3.03
::javac QuickSortMEE.java
::java QuickSortMEE
::del QuickSortMEE.class
::cd ..\..

::cd exercises\2.3.05
::javac SortDistinct2.java
::java SortDistinct2 < input.txt
::del SortDistinct2.class
::cd ..\..

::cd exercises\2.3.06
::javac Cn.java
::java Cn
::del Cn.class
::cd ..\..

::cd exercises\2.4.01
::javac PQClient.java
::java PQClient < input.txt
::del PQClient.class
::cd ..\..

::cd exercises\2.4.01
::javac PQClient.java
::java PQClient < input.txt
::del PQClient.class
::cd ..\..

::cd exercises\2.4.03
::javac MaxPQUA.java
::java MaxPQUA < input.txt
::del MaxPQUA.class
::javac MaxPQOA.java
::java MaxPQOA < input.txt
::del MaxPQOA.class
::javac MaxLLUO.java
::java MaxLLUO < input.txt
::del MaxLLUO.class
::del MaxLLUO$1.class
::del MaxLLUO$Node.class
::javac MaxLL.java
::java MaxLL < input.txt
::del MaxLL.class
::del MaxLL$1.class
::del MaxLL$Node.class
::cd ..\..

::cd exercises\2.4.05
::javac MaxPQClient.java
::java MaxPQClient < input.txt
::del MaxPQClient.class
::cd ..\..

::cd exercises\2.4.06
::javac MaxPQClient.java
::java MaxPQClient < input.txt
::del MaxPQClient.class
::cd ..\..

::cd exercises\2.4.07
::javac MaxPQClient.java
::java MaxPQClient input.txt
::del MaxPQClient.class
::cd ..\..

::cd exercises\2.5.01
::javac Main.java
::java Main
::del Main.class
::cd ..\..

::cd exercises\2.5.02
::javac CompoundWords.java
::java CompoundWords < input.txt
::del CompoundWords.class
::cd ..\..

::cd exercises\2.5.04
::javac Main.java
::java Main < ..\2.5.02\PQSampleInput.txt
::del Main.class
::cd ..\..

::cd exercises\3.1.01
::javac LetterGrades.java
::java LetterGrades < input.txt
::del LetterGrades.class
::cd ..\..

::cd exercises\3.1.04
::javac Time.java
::javac Event.java
::javac Client.java
::java Time
::java Event
::java Client
::del *.class
::cd ..\..