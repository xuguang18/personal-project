package com.personal.flink;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Flink 流处理示例作业：读取源数据，word count 后打印。
 * 本地运行：直接执行 main 方法即可。
 * 集群运行：mvn clean package 后用 flink run 提交 target/personal-flink-*.jar。
 */
public class StreamingJob {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<String> source = env.fromElements(
                "hello flink", "hello spark", "hello personal project");

        source
                .flatMap((String line, org.apache.flink.util.Collector<Tuple2<String, Integer>> out) -> {
                    for (String word : line.split("\\s+")) {
                        out.collect(Tuple2.of(word, 1));
                    }
                })
                .returns(Types.TUPLE(Types.STRING, Types.INT))
                .keyBy(tuple -> tuple.f0)
                .sum(1)
                .print();

        env.execute("personal-flink-word-count");
    }
}
