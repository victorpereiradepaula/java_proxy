# Proxy HTTP

### Grupo: Renan Benatti dias, Victor Pereira de Paula

## Introdução
O proxy HTTP foi implementado para a disciplina de Redes de Computadores. A linguagem escolhida para implementação foi Java. Apenas os comandos GET são tratados pelo proxy.

## Configuração do navegador
Ensinaremos de forma breve a configuração do Firefox para utilizar o proxy:
1. Selecione a opção **Preferências**
1. Na sessão **Configurações de rede** clique em **Configurar conexões...**
1. Na sessão **Configuração do proxy de acesso à internet** selecione a opção **Configuração manual de proxy**
1. No campo **Proxy HTTP** insira *localhost* e no campo **Porta** insira a porta de sua preferência
1. Clique em **OK** e pronto

## Executar o proxy
1. Abra o navegador já configurado
1. Na pasta ```src/```, abra o terminal e compile o código com o comando ```javac MainServer.java```
1. Após compilado, para utilizar execute o seguinte comanando no terminal ```Java MainServer <número_da_porta> <tamanho_da_cache_em_MB>```
1. Volte ao navegador e navegue

## Problemas e soluções
1. Cabeçlhos HTTP:

1. LRU (Least Recently Used):

1. Requisição de imagens:

