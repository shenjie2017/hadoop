package com.blue.hadoop.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @author shenjie
 * @version v1.0
 * @Description
 * @Date: Create in 17:42 2018/1/22
 * @Modifide By:
 **/

//      ┏┛ ┻━━━━━┛ ┻┓
//      ┃　　　　　　 ┃
//      ┃　　　━　　　┃
//      ┃　┳┛　  ┗┳　┃
//      ┃　　　　　　 ┃
//      ┃　　　┻　　　┃
//      ┃　　　　　　 ┃
//      ┗━┓　　　┏━━━┛
//        ┃　　　┃   神兽保佑
//        ┃　　　┃   代码无BUG！
//        ┃　　　┗━━━━━━━━━┓
//        ┃　　　　　　　    ┣┓
//        ┃　　　　         ┏┛
//        ┗━┓ ┓ ┏━━━┳ ┓ ┏━┛
//          ┃ ┫ ┫   ┃ ┫ ┫
//          ┗━┻━┛   ┗━┻━┛

public class LocalRunDriver {

    static class LocalRunMapper extends Mapper<LongWritable,Text,Text,IntWritable>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] words = value.toString().split("\\s+");
            for(String word:words){
                context.write(new Text(word),new IntWritable(1));
            }
        }
    }

    static class LocalRunReducer extends Reducer<Text,IntWritable,Text,IntWritable>{
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int count = 0;
            for(IntWritable value:values){
                count +=  value.get();
            }

            context.write(key,new IntWritable(count));
        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //hdfs上传文件需要配置HADOOP_USER_NAME
//        System.setProperty("HADOOP_USER_NAME", "hadoop");
        Configuration conf = new Configuration();

        //构造yarn的任务
        Job job = Job.getInstance(conf);

        //设置jar包的位置
        job.setJarByClass(LocalRunDriver.class);

        //设置map task处理类
        job.setMapperClass(LocalRunMapper.class);
        job.setReducerClass(LocalRunReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        //设置map task的输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        //设置程序的输入和输出参数，这里是程序需要统计文件所在的目录
        FileInputFormat.setInputPaths(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));

        //等待yarn的任务执行完成
        boolean res = job.waitForCompletion(true);
        //根据程序的执行状态返回一个当前程序的执行状态
        System.exit(res?0:1);
    }
}
