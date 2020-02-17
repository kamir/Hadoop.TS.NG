package org.opentsx.kstreams.tsa;


import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Printed;
import org.opentsx.data.model.EpisodesRecord;
import org.opentsx.kstreams.StateStoreExample2;
import org.opentsx.kstreams.cks.CassandraStoreBuilder;
import org.opentsx.kstreams.processors.CountingProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class TSAExample1 {

    private static final Logger LOG = LoggerFactory.getLogger(TSAExample1.class);

    public static void main(String[] args) throws Exception {


      StreamsConfig streamsConfig = new StreamsConfig(getProperties());

      StreamsBuilder builder = new StreamsBuilder();


      KTable<String, EpisodesRecord> episodesTable = builder.table("OpenTSx_Episodes");

        episodesTable.toStream().print(Printed.<String, EpisodesRecord>toSysOut().withLabel("Episodes-KTable"));

      KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), streamsConfig);

      LOG.info("KTable processing started");

      kafkaStreams.cleanUp();
      kafkaStreams.start();

      Thread.sleep(15000);

      LOG.info("Shutting down KTable processing Application now");

      // kafkaStreams.close();


      Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));

  }

    public static Properties getProperties() {

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "TSAExample1");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        props.put("schema.registry.url", "http://my-schema-registry:8081");
        return props;

    }




}
