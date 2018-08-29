# Smart Push

## 1     - Build Environment and installs

    - Install Docker https://docs.docker.com/install/
    
### 1.0.1 - Image and containers linux - Localhost

    - Arquivo que prepara ambiente de desenvolvimento na maquina do desenvolvedor. Ex.: imagem e containers kafka, mongo, redis...
    
```shell
    sudo ./buildEnvironment.sh
```

### 1.0.2 - Create consumers Kafka and view logs

####        Consumers
```shell
    sudo docker exec kafka /opt/kafka_2.11-0.10.1.0/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic {name fo topic} --from-beginning
```
####        TODO logs

TODO


### 1.0.3 - Testing Web Notification with Firebase

###         Firebase Devmvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dmaven.test.failure.ignore=false
    1 - Edit file hosts. Add -> 10.1.2.75 firebase.egs.com.br
    2 - View in browser Chrome https://firebase.egs.com.br/
    3 - Allow notification
    4 - Gerate IID_TOKEN
###         Testing request - https://bit.ly/1K5ZGHG
    1 - POST Request - https://fcm.googleapis.com/fcm/send
    2 - HEADERs:

    - Authorization : key=AAAAjai7rcY:APA91bFuV6qGf4e3HifyBFYC_KsGmzm31bk8AA69CJYODMDCBFXVUHce5dfH1gBVGbTv2zamkUcqQluBwQePVZjd6LLUiTAcAGRaCqC5YkgpeL3AkyDbKb0oTro43H7CPuMyLKotKGFS
    
    - Content-Type :  application/json
    
        3 - BODY - Raw = 
    ```json
    {
      "notification": {
        "title": "Alerta de saldo",
        "body": "Seu saldo é menor que R$ 5,00",
        "icon": "https://www.seeklogo.net/wp-content/uploads/2013/01/claro-novo-logo-vector.png",
        "click_action": "http://minhaclaro.com.br",
        "image": "https://i.imgsafe.org/6d/6daa47e59b.jpeg",
        "actions": [
           { "action": "like", "title": "Like" }
        ]
      },
      "to": "'${IID_TOKEN}'"
    }
    ```


## 2     - Deploy on DEV server

## 2.0.1 - exec localhost

          - 	Na máquina local, build do projeto e geração de imagens docker . Executar no diretório do projeto

```shell
    ./buildAllDockerImages.sh
```
## 2.0.1 - exec on server dev

          -   no servidor de dev executar arquivo de deploy
```shell
    ./deploy.sh
```
          -   no servidor de dev executar comandos para conferir os logs do deploy
```shell
    sudo docker ps
    sudo docker image ls
    sudo docker logs -f {id image container}
```
