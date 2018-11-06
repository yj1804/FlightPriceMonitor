1.create network

```
docker network create my-net
```

run ES

```
docker run --publish 9200:9200 --publish 9300:9300 --name myEs --network my-net -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:6.4.2

```

reference:
https://www.elastic.co/guide/en/elasticsearch/reference/current/docker.html

2.run kibana

```
docker run -d -p 5601:5601 --name myKibana --network my-net -e "ELASTICSEARCH_URL=http://172.18.0.2:9200" docker.elastic.co/kibana/kibana:6.4.2
```

 Kibana print log to stdout by default. so we can check log by "docker logs $contiaer_id" to view log

Reference:

https://www.elastic.co/guide/en/kibana/current/docker.html#environment-variable-config

1. visit kibana http://dockerdemo-2037667.lvs01.dev.ebayc3.com:5601

 

4. Load sample data

   ```
   wget https://download.elastic.co/demos/kibana/gettingstarted/shakespeare_6.0.json
   ```


\#define mapping in ES

```
curl -XPUT 'localhost:9200/shakespeare?pretty' -H 'Content-Type: application/json' -d'
{
"mappings": {
  "doc": {
   "properties": {
    "speaker": {"type": "keyword"},
    "play_name": {"type": "keyword"},
    "line_id": {"type": "integer"},
    "speech_number": {"type": "integer"}
   }
  }
 }
}
'
```



\#upload data to ES

```
curl -H 'Content-Type: application/x-ndjson' -XPOST 'localhost:9200/shakespeare/doc/_bulk?pretty' --data-binary @shakespeare_6.0.json
```

 

5.define index pattern. Kibana 用index pattern从ES获取数据

 

6.run logstash. 把配置文件放到~/logstash_config目录下

---------------logstash.conf

```
input {

  log4j {
     mode => "server"
     host => "0.0.0.0"
     port => 3456
     type => "log4j"
  }
}

output {
  elasticsearch { hosts => ["10.64.251.117:9200"] }
}
```

 

 

------------logstash.yml,这个用来配置ES的healthcheck，否则logstash启动会失败

xpack.monitoring.elasticsearch.url: "http://10.64.251.117:9200"

 

```
sudo docker run -v ~/logstash_setting/logstash.yml:/usr/share/logstash/config/logstash.yml -v ~/logstash_config:/config-dir  -p 3456:3456 --name logstash docker.elastic.co/logstash/logstash:6.2.2 logstash -f /config-dir/logstash.conf --debug
```

 

---------------------cmd

\#查看mapping

curl -XGET "http://127.0.0.1:9200/logstash-2018.02.27/_mapping?pretty"

curl -H 'Content-Type: application/x-ndjson' -XDELETE 'http://dockerdemo-2037667.lvs01.dev.ebayc3.com:9200/logstash-2018.02.27/'

curl -H 'Content-Type: application/x-ndjson' -XDELETE 'http://dockerdemo-2037667.lvs01.dev.ebayc3.com:9200/logstash-2018.02.27/_query' -d '{

​    "query" : {

​        "match_all" : {}

​    }

}'

 

 

 

 

http://dockerdemo-2037667.lvs01.dev.ebayc3.com:5601/