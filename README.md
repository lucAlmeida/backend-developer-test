# Notas de Implementação

- Aplicação implementada utilizando Spring Boot, Spring Data JPA e MySQL
- Documentação gerada usando Springfox/Swagger UI
- Testes da aplicação foram realizados utilizando Mockito e JUnit 5
## Configuração inicial

### MySQL
- Importar o arquivo SQL_SCRIPT.sql através de uma sessão de usuário com privilégios para criação de tabelas, banco de dados e triggers
- Após execução do script será criado o usuário ```student_manager```, identificado pela senha padrão ```student_manager```.
- Tal usuário tem privilégios de acesso para o banco de dados ```student_management```, que manterá as tabelas responsáveis pelo armazenamento dos dados provenientes da API de Registro de Estudantes. Este também é o usuário utilizado por padrão pela aplicação.

### Spring Boot

- Aplicação pode ser inicializada através da execução do seguinte comando na pasta raíz do projeto:  
``` mvn spring-boot:run```
- Após a execução do comando a aplicação estará disponível no endereço <localhost:8181>

## Requisitos

- Java JDK 11
- Maven
- MySQL

# Backend Developer - Test #

![Forleven Logo](https://site.forleven.com/img/logotipo_green.png)

Desenvolva uma API Rest para um cadastro de estudantes com intuito de consultar, criar novos registros, realizar atualizações e exclusões nestes (CRUD) utilizando um banco de dados.
Em um cenário de trabalho esta API seria consumida por um desenvolvedor front-end trabalhando em par contigo.

Crie um repositório **público** no github em uma conta pessoal sua e nos envie o link como resposta ao seu teste.


## Informações técnicas ##

Utilizar linguagens suportadas pela Forleven que são Java ou PHP, preferencialmente Java que é a linguagem predominante no back-end da empresa.
Banco de dados preferencialmente MySQL/MariaDB ou algum banco similar ao mesmo (SQLite ou H2).

## Regras de negócio ##

Cadastrar campos nome, sobrenome e matrícula;
Todos os campos devem ser preenchidos;
Todos os campos devem conter mais de 3 caracteres;
O campo de matrícula não pode se repetir dentro da base;

## Dicas ##

Estamos avaliando seu perfil Back-end, caso queira você pode construir um front para sua aplicação, mas não é o foco, concentre-se na melhor solução possível para o seu back-end.

API de exemplo para você se inspirar: https://swapi.dev/

## Bônus ##

Cadastrar múltiplos telefones, dar a opção cadastrar N telefones ao registro de um estudante.
Usar frameworks consolidados como Spring (spring jpa, spring boot, spring web), Laravel, Slim.

