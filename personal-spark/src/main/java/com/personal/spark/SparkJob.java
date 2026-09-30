package com.personal.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Map;

/**
 * Spark 批处理示例作业：WordCount。
 * 本地运行：直接执行 main 方法即可。
 * 集群运行：mvn clean package 后用 spark-submit 提交 target/personal-spark-*.jar。
 */
public class SparkJob {

    public static void main(String[] args) throws Exception {
        SparkConf conf = new SparkConf()
                .setAppName("personal-spark-word-count")
                .setMaster("local[*]");

        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            JavaRDD<String> lines = sc.parallelize(Arrays.asList(
                    "hello spark", "hello flink", "hello personal project"));

            Map<String, Long> counts = lines
                    .flatMap(line -> Arrays.asList(line.split("\\s+")).iterator())
                    .mapToPair(word -> new Tuple2<>(word, 1L))
                    .reduceByKey(Long::sum)
                    .collectAsMap();

            counts.forEach((word, count) -> System.out.println(word + ": " + count));
        }
    }
}
