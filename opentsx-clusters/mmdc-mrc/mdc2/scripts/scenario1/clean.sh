sudo docker-compose exec cli-west-2 kafka-topics --delete test-szenario-01 --bootstrap-server 192.168.0.9:9091

sudo docker-compose exec cli-west-2 kafka-topics --list --bootstrap-server 192.168.0.9:9091
