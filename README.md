# AutoPix-Reforged

> ℹ️ **Nota:** Este é um porte não oficial do plugin [AutoPix para Spigot](https://github.com/warleysr/autopix), originalmente criado por [@warleysr](https://github.com/warleysr). Todo o crédito pela ideia e funcionalidade original pertence a ele.

O AutoPix-Reforged é um mod para **Minecraft (Forge/Fabric)** que integra **código QR PIX** dentro do jogo e permite que os jogadores comprem itens e recebam automaticamente em questão de segundos.

## Como funciona?

O jogador digita um comando (ex: `/comprar`) e um menu customizado será mostrado com os produtos disponíveis.

*(Imagem do menu de produtos do plugin original)*

Ao confirmar a compra, o jogador receberá um mapa customizado com o QR code para pagar via PIX:

*(Imagem do QR code no mapa do plugin original)*

## Modos de validação

* **Modo automático:** No modo automático, o jogador só precisa aguardar a confirmação do pagamento. A cada `x` segundos, o mod faz uma verificação nos status dos pedidos pendentes e os ativa ao serem aprovados. Quanto menor o tempo de verificação, mais instantânea será a confirmação, porém isso pode gerar mais requisições para a API do Mercado Pago.
* **Modo manual:** Após pagar, o jogador obtém o código da transação PIX e usa um comando como `/pix validar <Codigo>`. A vantagem deste modo é a ausência de taxas, pois o PIX é realizado diretamente para a chave configurada. A validação busca a transação a partir do código E2E do PIX. O modo automático é o mais recomendado.

Você poderá configurar comandos para serem executados após a compra, como dar um cargo de VIP, dinheiro, itens, etc.

## Comandos Planejados

* `/pix info`: Abre uma interface com instruções de como o sistema funciona.
* `/pix lista`: Mostra a lista de ordens de pagamento criadas pelo jogador.
* `/pix lista <Jogador>`: (Para Admins) Permite ver as ordens de outro jogador.
* `/pix reload`: (Para Admins) Recarrega as configurações do mod.
* `/comprar <menu>`: Abre um menu de compra específico, permitindo a criação de múltiplas lojas.

## Permissões

As permissões serão gerenciadas através dos níveis de OP do servidor ou via integração com mods de permissões populares (como FTB Ranks ou LuckPerms para Fabric/Forge).

* **Nível de Usuário:** `autopix.use` (provisório) - Permite realizar compras por PIX.
* **Nível de Admin:** `autopix.admin` (provisório) - Permite usar comandos de administrador, como `reload` e ver a lista de outros jogadores.

## Outras Features

* Todas as mensagens editáveis.
* Limite de tempo entre as ações para evitar sobrecarga.
* Suporte a múltiplos menus de venda.

## Download e Instalação (Forge/Fabric)

1.  Baixe a última versão na aba de **Releases** do projeto.
2.  Coloque o arquivo `.jar` na pasta `mods` do seu servidor e também na pasta `mods` do Minecraft de todos os jogadores que forem entrar nele.
3.  Inicie o servidor uma vez para gerar os arquivos de configuração na pasta `config`.
4.  Edite os arquivos de configuração, inserindo seu Token do Mercado Pago, sua chave PIX e as credenciais do banco de dados.
5.  Reinicie o servidor.

---
## Agradecimentos e Créditos

* A **@warleysr** pela criação do incrível plugin [AutoPix](https://github.com/warleysr/autopix) original, que serviu como base e inspiração para este projeto.
* A **@rapust** pela criação da biblioteca [QRCodeMap](https://github.com/rapust/QRCodeMap), que tornou a exibição de QR codes no jogo possível.
