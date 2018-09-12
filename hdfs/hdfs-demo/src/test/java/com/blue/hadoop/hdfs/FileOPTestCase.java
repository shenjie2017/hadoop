package com.blue.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;

public class FileOPTestCase {
    private FileSystem fs = null;
    private Configuration conf = null;
    private PrintStream out = System.out;

    @Before
    public void init(){
        try {
            conf = new Configuration();
            //本地文件系统
            fs = LocalFileSystem.get(new URI("file:///"),conf);

            //hadoop分布式文件系统
//            fs = DistributedFileSystem.get(new URI("hdfs://node1.big.blue.com:8020"),conf);
//            fs = FileSystem.get(new URI("hdfs://node1.big.blue.com:8020"),conf);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定目录下的文件列表
     */
    @Test
    public void testFileStatus(){
        try {
            FileStatus[] fileStatuses = fs.listStatus(new Path("d:/notes") );
            printArray(out,fileStatuses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定目录下的所有文件列表
     */
    @Test
    public void testGlobalStatus(){
        try {
            FileStatus[] fileStatuses = fs.globStatus(new Path("d:/notes/{*,*/*}") );
            printArray(out,fileStatuses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testDelete(){
        try {
//            fs.delete(new Path("d:/tmp/hello/java"),true);
//            fs.delete(new Path("d:/tmp/hello/scala.txt"),true);
//            fs.delete(new Path("d:/tmp/hello/cpp.txt"),false);

            //true表示递归删除，false则为空或者是文件的形式才允许被删除
            boolean b = fs.delete(new Path("d:/tmp/hello"),true);
            out.printf(b?"delete success!":"delete fail!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印泛型数组
     * @param out 输出流
     * @param arr 数组
     * @param <T> 任意数组对象类型
     */
    private static <T extends Object> void printArray(PrintStream out, T[] arr){
        for(T t:arr){
            out.println(t.toString());
        }
    }
}
