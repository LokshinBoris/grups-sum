package telran.numbers.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import telran.numbers.*;
class GroupsSumTest {
private static final int N_GROUPS = 100000;
private static final int GROUP_LENGTH = 1000;
int[][] groups = {
		{1,2},
		{3,4},
		{5,6}
};

	@Test
	void goupsSumTaskThreadTest() {
		GroupsSum gs = new GroupsSumTaskThread(groups);
		assertEquals(21, gs.getSum());
	}
	private int[][] getLargeGroups(int nGroups, int groupLength) {
		// TODO creating random two dimensional array 
		// using method generate of Stream
		int[][] largeGroup=new int[nGroups][groupLength];
		Random rand = new Random();
		Supplier<int[]> sup = () -> rand.ints(groupLength, Integer.MIN_VALUE, Integer.MAX_VALUE).toArray();
		largeGroup = Stream.generate(sup).limit(nGroups).toArray(int[][]::new);
		return largeGroup;
	}
	
	@Test
	void getLargeGroupTest()
	{
		int nY=10;
		int nX=5;
		int[][] largeGroup=getLargeGroups(nY,nX);
		for(int i=0;i<nY;i++)
		{
			for(int j=0;j<nX;j++)
			{
				System.out.printf("%12d, ",largeGroup[i][j]);
			}
			System.out.printf("\n");
		}
	}
	
	@Test
	void performanceTest()
	{
		printTitle();
		int[][] largeGroup;
		for(int i=10;i<=N_GROUPS;i=i*10)
		{
			largeGroup=getLargeGroups(i,GROUP_LENGTH);
			long tTaskThread = getTimeWorkTaskThread(largeGroup);
			long tThreadPool = getTimeWorkThreadPoll(largeGroup);
			String strAbout=String.format("[%d %d] ", i,GROUP_LENGTH);
			System.out.printf("%20s %20d %20d\n",strAbout,tTaskThread,tThreadPool);
		}
	}
	
	private void printTitle() 
	{
		System.out.printf("%20s %20s %20s\n", "Dimension_of_Array","Time_of_TaskThread","Time_of_ThreadPool");
	}
	
	private long getTimeWorkThreadPoll(int[][] largeGroup)
	{
		Instant start = Instant.now();
		groupsSumTaskThreadPerformance(largeGroup);
		Instant finish = Instant.now();
		long t= Duration.between(start, finish).toMillis();
		return t;
	}
	
	private long getTimeWorkTaskThread(int[][] largeGroup) 
	{
		Instant start = Instant.now();
		groupsSumTaskThreadPerformance(largeGroup);
		Instant finish = Instant.now();
		long t= Duration.between(start, finish).toMillis();
		return t;
	}
	@Test
	void goupsSumThreadPoolTest() {
		GroupsSum gs = new GroupsSumThreadPool(groups);
		assertEquals(21, gs.getSum());
	}
	@Test
	void groupsSumTaskThreadPerformance(int[][] largeGroups) 
	{
		new GroupsSumTaskThread(largeGroups).getSum();
	}
	@Test
	void groupsSumTaskThreadPoolsPerformance(int[][] largeGroups)
	{
		new GroupsSumThreadPool(largeGroups).getSum();
	}

}
