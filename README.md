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

## Implementação
### Classes

### Problemas e soluções
1. Cabeçlhos HTTP: Nas primeiras fases da implementação não havíamos implementado a adição de cabeçalhos às respostas, o que acarretava a não renderização das páginas que testávamos, mesmo quando recebiamos conteúdo nas respostas.

1. Cache e LRU (Least Recently Used): Como temos uma limitação do tamanho da cache, temos a necessidade de remover itens conforme há demanda por espaço. A solução encontrada para implementação de forma mais fácil, foi criar uma **LinkedList** para armazenar os *URLs* das páginas acessadas, desta forma as páginas mais antigas sempre estarão no fim da lista. Também criamos um **HashMap** para salvar os elementos cacheados, com a *URL* como chave, desta forma quando precisamos remover itens, removemos a partir da última posição da **LinkedList**. O uso do **HashMap** além da **LinkedList** foi para facilitar a optenção dos dados da cache, por meio do *URL* (chave).

1. Requisição de imagens:

1. HTTPS:

1. Requisição de vídeos: Não foram realizados os tratamentos necessários para requisições de vídeos, nem o armazenamento destes na cache.

## Conclusão