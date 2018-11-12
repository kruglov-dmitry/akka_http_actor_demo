### Requirements
JDK 8 or above
SBT 0.13.17

### Assumptions
1) username and password ascii strings without special symbols
2) settings of parallelism will be defined by cfg file via deployment section for particular actors
3) API endpoints will receive & return data in plain json format 
4) no security

### How to build
```bash
sbt assembly
```

will create fat jar with all dependencies at `target/scala-2.11/akka_http_actor_demo-assembly-1.0.0.jar`

### How to run

1) edit cfg file at conf/app.conf
2) execute start.sh by invoking sh start.sh
3) use following endpoints to validate requests:
```bash
curl -w "\n" -XGET 'http://localhost:8080/healthcheck'

curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"username":"A","password":"A"}' \
  http://localhost:8080/v1/auth

curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"username":"B","password":"A"}' \
  http://localhost:8080/v1/auth

curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"username":"B","password":"B"}' \
  http://localhost:8080/v1/auth
  
  curl -v --header "Content-Type: application/json" \
    --request POST \
    --data '{"username":"B"}' \
    http://localhost:8080/v1/auth
```

If you want to run multiple simultaneous requests you may rely on simple python scripts:
by default it send 1k request and print their results

```bash
cd load_test
pip install -r requirements.txt
python run_load.py
```

### Notes

NOTE: in case you have encrypted home folder or mounted volume(s)
you may need to run following commands before assembling and after every 
```bash
sbt clean:
```

```bash
rm -rf ./target
mkdir /tmp/`echo $$`
ln -s /tmp/`echo $$` ./target
```
