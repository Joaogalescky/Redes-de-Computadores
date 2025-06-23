# Rede de Computadores - 3°TADS | 2025

Repositório para a matéria de Rede de Computadores - 3° TADS - Tecnólogo em Análise e Desenvolvimento de Sistemas | 2025.

## Docker

### Comandos
- Cria uma imagem a partir de um Dockerfile
```
docker build -t counter .
```

- Baixa uma imagem do Docker Hub ou outro registro
```
docker pull IMAGE
```

- Enviar imagem para um repositório Docker

```
docker push minha-imagem
```

- Remove uma imagem
```
docker rmi IMAGE
```

#### WatchTower
- Serve para observar um caminho e verificar se há alterações

```
watchtower:
    image: containrrr/watchtower  
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
    environment:
      - WATCHTOWER_POLL_INTERVAL=15 
      - WATCHTOWE_CLEANUP=true
    command: counter
```

## IFPR

[![IFPR Logo](https://user-images.githubusercontent.com/126702799/234438114-4db30796-20ad-4bec-b118-246ebbe9de63.png)](https://user-images.githubusercontent.com/126702799/234438114-4db30796-20ad-4bec-b118-246ebbe9de63.png)

**By João Vitor Campõe Galescky**

Written with  [StackEdit](https://stackedit.io/).
