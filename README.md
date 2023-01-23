# expense-control
API criada para controle de gastos

## Como rodar projeto local

Basta entrar na pasta **containers** e rodar o servico **database**
Ex:

```
docker-compose up -d database
```

## SonarQube
Para fazer análises de qualidade de códigos basta subir o SonarQube
```
docker-compose up -d sonarqube
```
Acessar ele na <localhost:9000> com usuário admin senha admin, gerar o token de autenticação
e rodar passando as informações necessárias para as analises. Ex:
```
./gradlew sonar -D sonar.projectKey=expense-control -D sonar.host.url=http://localhost:9000 -D sonar.login=069b5982cc6843f8d914db5439bb1bfba8ee56fd
```