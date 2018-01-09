# Environment Manager on Linux

Linkedin: [Tiago Casemiro](https://www.linkedin.com/in/tiago-p-58b45228)

   > Esta aplicação facilita a gerencia de variaveis de ambiente.
   > Com apenas 1 comando voce pode...
    
## Features
  * Apagar variáveis de ambiente no linux
  * Listar variáveis de ambiente no linux
  * Criar variáveis de ambiente no linux
 
Você também pode...
 - Criar e adicionar ao PATH
 - Criar e adicionar ao PATH com um complemento na variável

## Instalação

[Baixe](https://github.com/tiagocasemiro/Environment/blob/master/Environment.jar) o Environment.jar e execute no terminal com os devidos parametros.

## Como usar

Para criar nova variável:
```sh
$ sudo java -jar Environment.jar create NOME_DA_VARIAVEL "valor da variável"
```

Para apagar variáves:
```sh
$ sudo java -jar Environment.jar delete NOME_DA_VARIAVEL
```

Para listar variáves:
```sh
...em construção :D
```
Para ver a versão corrente:
```sh
...em construção :D
```

Para criar variável e adicionar ao PATH:
```sh
$ sudo java -jar Environment.jar createOnPath NOME_DA_VARIAVEL "valor da variável"
```

Para criar variável e adicionar ao PATH com complemento na variável:
```sh
$ sudo java -jar Environment.jar createOnPath NOME_DA_VARIAVEL "valor da variável" "complemento" 
```
Após execução do Enviroment.jar será necessário reiniciar o terminal. Caso você não queira reiniciar o terminal, poderá executar o seguinte comando no terminal de trabaho.
```sh
$ source /etc/.environment 
```
Após execução do comando variáveis temporárias serão criadas apenas no terminal em que o comando foi executado.
<br/><br/>
<p>
> Se você não tem a oportunidade <br/>
> de fazer grandes coisas, <br/>
> pode fazer pequenas coisas <br/>
> de forma grandiosa. <br/>
</p>
<br/>
