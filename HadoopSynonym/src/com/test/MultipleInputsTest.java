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
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;


public class MultipleInputsTest {

	
    static class SimpleMapper extends Mapper<LongWritable,Text,Text,LongWritable>  
    {  
        BufferedReader reader = null;  
        List<String> lines = new ArrayList<String>(); //简单测试，没有任何业务逻辑  
          
//        public void setup(Context context) throws IOException  
//        {  
//        	String[] filenames = {"test_upload_file", "test_upload_file2"};
//        	for(String filename: filenames) {
//        		FileReader fr = new FileReader(filename);  //必须和上传文件名一致  
//        		reader = new BufferedReader(fr);  
//
//        		String line = null;  
//        		while((line = reader.readLine()) != null)  
//        			lines.add(line);  
//        		System.out.println(lines);  
//        	}
//        }  
        @Override  
        public void map(LongWritable key, Text value, Context context)throws IOException, InterruptedException  
        {  
//            for(String line:lines)  
//                context.write(new Text("key"),new Text(line));  
//        	value = transformTextToUTF8(value, "gbk");
//        	System.out.println(value.encode("gbk").toString());
//        	System.out.println(value.toString());
//        	System.out.println(value.);

        	String line = value.toString();
        	if (line.contains("repcaptm")) {
        		return;
        	}
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
