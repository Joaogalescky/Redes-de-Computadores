# TADS - Redes de Computadores
## Trabalho 2° Bimestre
https://materiais.darlon.com.br/SO/rc2025-2b

1. 

### Ambiente Web Multi-Serviço utilizando Docker Compose
Desenvolva um ambiente funcional com frontend, backend e banco de dados, orquestrado via docker-compose.

Todos os serviços devem ser definidos em um único arquivo docker-compose.yml (ou compose.yml), com configuração de volumes, redes e variáveis de ambiente. O projeto deve estar pronto para ser executado com o comando:

```bash
cp .env.template .env
docker-compose up --build
```

O projeto deve ser reprodutível em qualquer máquina com Docker e Docker Compose instalados, sem necessidade de configurações adicionais além das definições do arquivo .env.

**Requisitos**
* Utilizar Docker Compose para orquestrar os serviços
* Cada serviço deve possuir seu próprio Dockerfile (sem usar imagens * prontas exceto para o banco ou admin)
* Utilizar volumes para persistência do banco
* Usar redes Docker para comunicação entre os serviços
* Configurar variáveis de ambiente via arquivo .env
* Documentar o projeto em um README.md com:
* Descrição do ambiente e das ferramentas utilizadas
* Instruções de uso
* Explicação da arquitetura
* Imagens ou diagramas (opcional)


## IFPR

[![IFPR Logo](https://user-images.githubusercontent.com/126702799/234438114-4db30796-20ad-4bec-b118-246ebbe9de63.png)](https://user-images.githubusercontent.com/126702799/234438114-4db30796-20ad-4bec-b118-246ebbe9de63.png)

**By João Vitor Campõe Galescky**

Written with  [StackEdit](https://stackedit.io/)