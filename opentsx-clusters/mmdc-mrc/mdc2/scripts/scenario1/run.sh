export TN=T2.1

echo "*******************"
echo "Topicname: $TN"
echo "*******************"

# sudo docker-compose exec cli-west-2 kafka-topics --list --bootstrap-server broker-west-1:9091

sudo docker-compose exec cli-west-2 kafka-topics --create --topic $TN --replication-factor 3 --partitions 108 --bootstrap-server broker-west-1:9091

rm -rf prod.config
echo "bootstrap.servers=broker-west-1:9091" >> prod.config

sudo docker cp prod.config cli-west-2:config

sudo docker-compose exec cli-west-2 kafka-producer-perf-test --topic $TN --record-size 512 --throughput 60000 --num-records 54000000 --producer-props acks=all linger.ms=10 --producer.config /config

#| tee /tmp/producer &

#sudo docker-compose exec cli-west-2 kafka-topics --list --bootstrap-server broker-west-1:9091

sudo docker-compose exec cli-west-2 kafka-topics --describe --topic $TN