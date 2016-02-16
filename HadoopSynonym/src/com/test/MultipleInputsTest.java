package com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/*
* 这个文件是为了测试多路输入的,使用了MultipleInputs方法
*
* */

public class MultipleInputsTest {

	
    static class SimpleMapper extends Mapper<LongWritable,Text,Text,LongWritable>  
    {  
        BufferedReader reader = null;  
        List<String> lines = new ArrayList<String>(); //简单测试，没有任何业务逻辑  
          

        @Override  
        public void map(LongWritable key, Text value, Context context)throws IOException, InterruptedException  
        {
        	String line = value.toString();

        	String[] words = line.split("CITY");
        	if (words.length >=2) {
        		System.out.println(value.toString());
        		context.write(new Text(words[1]), new LongWritable(1));
        		return;
        		
        	}  else {
        		return;
        	}
        }  
    }  
      
    static class SimpleReducer extends Reducer<Text, LongWritable, Text, LongWritable>  
    {  
        public void reduce(Text key, Iterable<LongWritable> values, Context context)throws IOException, InterruptedException  
        {  
//        	values.
        	long num = 0;
    		String outValue = "";

        	for(LongWritable value: values)  
        	{  
            	num = num + 1;
//        		outValue +="," + value.toString();
            }  
        	if (num != 1) {
       
    			context.write(key, new LongWritable(num));
        	}
//        	context.write(key, new LongWritable(num));
        }  
    }  
	
	
	
	
	public static void main(String[] args) throws Exception {
	      Configuration conf = new Configuration();  
	        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();  
	        for (String s:otherArgs)  
	            System.out.println(s);  
	        if (otherArgs.length != 3) {  
	          System.err.println("Usage: Wordcount  input1 input2 output");  
	          System.exit(2);  
	        }  
	          
	        Job job = new Job(conf);  
	        job.setJarByClass(MultipleInputsTest.class);  
//	          
//	        FileInputFormat.addInputPath(job, new Path(otherArgs[0]));  
	        FileOutputFormat.setOutputPath(job, new Path(otherArgs[2]));  
	  
	        job.setNumReduceTasks(5);  
	        job.setMapperClass(SimpleMapper.class);  
	        job.setReducerClass(SimpleReducer.class);  
//	        job.setOutputFormatClass(GBKOutputFormat.class);
//	        job.setInputFormatClass(cls);
	        
	        //use MultipleOutputs and specify different Record class and Input formats
	    	MultipleInputs.addInputPath(job, new Path(otherArgs[0]), TextInputFormat.class, SimpleMapper.class);
	    	MultipleInputs.addInputPath(job, new Path(otherArgs[1]), TextInputFormat.class, SimpleMapper.class);
	        
	        
	        job.setOutputKeyClass(Text.class);  
	        job.setOutputValueClass(LongWritable.class);  
	          
	        System.exit(job.waitForCompletion(true)? 0: 1);  
	}

}
