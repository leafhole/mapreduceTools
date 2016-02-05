/**
 * 
 */
package mr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;










import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import format.GBKFileOutputFormat;
import format.MRUtils;

/**
 * @author leaf
 *
 */
public class AliasSynonymSelect {

	/**
	 * 
	 */
	public AliasSynonymSelect() {
		// TODO Auto-generated constructor stub
	}

	public static class Map extends Mapper<Object, Text, Text, LongWritable> {
		@Override
		protected void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			String line = MRUtils.getGBKString(value).trim();// 读取GBK编码的源文件
			ArrayList<String> ret = AliasSplit.parse(line);
			if (ret == null) {
				return;
			}
			LongWritable one = new LongWritable(1);
			for(String synonym: ret) {
				
				context.write(new Text(synonym), one);
			}
			
		}
	}
	
	
	public static class IntSumReducer
	extends Reducer<Text,LongWritable,Text,LongWritable> {
		private LongWritable result = new LongWritable();

		public void reduce(Text key, Iterable<LongWritable> values,
				Context context
				) throws IOException, InterruptedException {
			int sum = 0;
			for (LongWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);
		}
	}
	
	
//	public static class Reduce extends Reducer<Text, LongWritable, Text, LongWritable> {
//		
//		@Override
//		protected void reduce(Text key, Iterable<LongWritable> values, Context context)
//				throws IOException, InterruptedException {
//			
//		}
//		
//		
//	}
//	
	
	
	public void hadoopRun(String allArgs[]) {
		try {
			Configuration conf = new Configuration();
			String[] args = new GenericOptionsParser(conf, allArgs)
					.getRemainingArgs();
			System.out.println(Arrays.toString(args));

			// Path srcPath = new Path(args[0]);
			Path destPath = new Path(args[1]);

			Job job = new Job(conf);
			job.setJarByClass(AliasSynonymSelect.class);
			job.setMapperClass(Map.class);
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(LongWritable.class);
			job.setReducerClass(IntSumReducer.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(LongWritable.class);

			job.setInputFormatClass(TextInputFormat.class);
			// job.setOutputFormatClass(TextOutputFormat.class);
			job.setOutputFormatClass(GBKFileOutputFormat.class);
			job.setNumReduceTasks(50);

			// FileInputFormat.addInputPath(job, srcPath);
			FileInputFormat.addInputPaths(job, args[0]);
			FileOutputFormat.setOutputPath(job, destPath);

			if (job.waitForCompletion(true)) {
				System.out.println("Successful Done.....");
			} else {
				System.err.println("Process Error!!!!");
				System.exit(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AliasSynonymSelect step = new AliasSynonymSelect();
		step.hadoopRun(args);
	}
}
