/**
 * 
 * Time Series Creator from Wikipedia RAW-PageClick Count Files.
 * 
 */ 
package tstool4mr.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;



/**
 *
 * @author kamir
 */
public class TSCFromWikiRawPageCount extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {

        boolean LOCAL = false;
        
        Configuration conf = getConf();
        for (java.util.Map.Entry<String, String> entry: conf) {
            System.out.printf("%s=%s\n", entry.getKey(), entry.getValue());
        }
        
//        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
//        if (otherArgs.length != 2) {
//            System.err.println("Usage: ... <in> <out>");
//            System.exit(2);
//        }
        
        /**
         * DEBUGGING notes:  http://wiki.apache.org/hadoop/HowToDebugMapReducePrograms
         */
        if ( LOCAL ) { 
                conf.set("mapred.job.tracker", "local");
                conf.set("fs.default.name", "local");
                
                args = new String[2];
                args[0] = "/media/sde/TEST/wikiraw/2009/2009-01c/";
                args[1] = "/media/sde/TEST/OUT/c";
        }
        else { 
                args = new String[2];
                args[0] = "/media/sde/TEST/wikiraw/2009/2009-01c/";
                args[1] = "/media/sde/TEST/OUT/c2";
        
        }
        
        System.out.println("arg[0]= "+args[0] + " args[1]= "+ args[1]);

        Job job = new Job(conf, TSCFromWikiRawPageCount.class.getSimpleName());
        job.setJobName("TSCFromWikiRawPageCount");
        
        job.setJarByClass(TSCFromWikiRawPageCount.class);
        
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(tstool4mr.mr.TSCMapper.class);
        job.setCombinerClass(tstool4mr.mr.TSCReduce.class);
        job.setReducerClass(tstool4mr.mr.TSCReduce.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);

        return 0;
    }



    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new TSCFromWikiRawPageCount(), args);
        System.exit(exitCode);
    }
}
