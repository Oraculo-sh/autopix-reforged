# 🔄 Prompt para Portabilidade AutoPix: Spigot → Minecraft Forge

## 🎯 OBJETIVO PRINCIPAL
Analise o plugin AutoPix original para Spigot/Bukkit e recrie completamente para Minecraft Forge, mantendo 100% das funcionalidades originais. O sistema deve ser compatível com Minecraft 1.19.x, 1.20.x e 1.21.x.

## 📋 DADOS A SE UTILIZAR:
- O diretorio do java é C:\Program Files\Eclipse Adoptium\jdk-17.0.16.8-hotspot
- O repositório em que esta trabalhando é  o qual ja esta criado e clonado na pasta C:\Github\autopix-reforged
- O repositório ja possui o .gitignore, LICENSE e README.md.
- O diretorio do curseforge é C:\Users\Leonn\AppData\Programs\CurseForge Windows
- O diretorio da instalação do arquivos do curseforge para sua versão do minecraft contendo forge e suas instancias é C:\Users\Leonn\curseforge\minecraft
- O diretorio das instancias do minecraft no curseforge é C:\Users\Leonn\curseforge\minecraft\Instances
- O diretorio da instancia 1.19.2 é C:\Users\Leonn\curseforge\minecraft\Instances\1.19.2
- O diretorio da instancia 1.20.1 é C:\Users\Leonn\curseforge\minecraft\Instances\1.20.1
- O diretorio da instancia 1.21.1 é C:\Users\Leonn\curseforge\minecraft\Instances\1.21.1
- A iniciativa de reforjar o mod é minha, nome Leonne Martins, github user Oraculo-sh, email leonne.martins@outlook.com, discord leonne.martins
- O plugin para paper que estamos reforjando é o  do Warley com o user warleysr. 
- As documentações sobre forge são 
  
- Documentação completa do paper api para devs no repositorio 

## 📋 INSTRUÇÕES PARA ANÁLISE DO CÓDIGO FONTE

### 1. LOCALIZAÇÃO DO PLUGIN ORIGINAL
**Repositório**: 
**Linguagem**: Java (Spigot/Bukkit API)
**Funcionalidade**: Sistema de pagamentos PIX integrado ao Minecraft

### 2. SISTEMA DE VERSIONAMENTO
**Estratégia**: Criar branches separadas para cada versão principal
- `main` → 1.19.2 (base)
- `1.20.x` → Minecraft 1.20.x
- `1.21.x` → Minecraft 1.21.x

## 🔍 ANÁLISE FUNCIONAL DETALHADA

### 1. SISTEMA DE PAGAMENTOS
**Função Original**: Integração com API MercadoPago para gerar PIX (O codigo original funciona em java nao sendo necessario modificar)
**Migração**: Manter exata funcionalidade.


### 2. SISTEMA DE GUI
**Função Original**: Menus de inventário para seleção de produtos
**Migração**: Converter para AbstractContainerMenu + Screen


### 3. SISTEMA DE COMANDOS
**Função Original**: Comandos como `/comprarpix`, `/pix`

## 📚 RECURSOS DE REFERÊNCIA

### 1. DOCUMENTAÇÃO OFICIAL
- **Minecraft Forge**: 
- **Forge MDK**: 
- **Mappings**: 

### 2. FERRAMENTAS DE DESENVOLVIMENTO
- **MCP**: Mod Coder Pack para obfuscation
- **Architectury**: Para multi-platform
- **ModDevGradle**: Plugin Gradle para desenvolvimento
- **LuckPerms e FTB Ranks**. Para gerir permissões alem de sistema de OP nativo do minecraft, uma implementação que sera realizado em um proximo update, porem ter em mente a implementação do mesmo.


## 🔧 INSTRUÇÕES ESPECÍFICAS PARA O AGENTE

### 0. ATIVIDADES A REALIZAR
```
- Atualizar o .gitignore sempre que necessario.
- Realizar commits sempre que for realizado algo.
- Modificar o README.md somente se necessario, mantendo a Nota de porte e devidos Agradecimentos e Créditos.
```

### 1. PROCESSO DE ANÁLISE
```
STEP 1: Clonar repositório original do AutoPix
STEP 2: Mapear todas as classes e suas responsabilidades
STEP 3: Identificar dependências externas (APIs, bibliotecas)
STEP 4: Criar matriz de compatibilidade Spigot → Forge
STEP 5: Implementar equivalentes Forge para cada componente
```

### 2. PRIORIDADES DE IMPLEMENTAÇÃO
```
PRIORIDADE 1: Estrutura base do mod (@Mod, configurações)
PRIORIDADE 2: Sistema de rede (packets, sincronização)
PRIORIDADE 3: Database e persistência
PRIORIDADE 4: API MercadoPago e pagamentos
PRIORIDADE 5: GUI e menus
PRIORIDADE 6: Comandos e permissões 
PRIORIDADE 7: Eventos e listeners
```

### 3. VALIDAÇÃO DE FUNCIONALIDADE
```
TESTE 1: Carregamento do mod sem erros
TESTE 2: Criação de pedidos PIX
TESTE 3: Geração de QR codes
TESTE 4: Interface gráfica responsiva
TESTE 5: Persistência no banco de dados
TESTE 6: Execução de comandos pós-pagamento
TESTE 7: Sincronização cliente-servidor
```

## ⚠️ PONTOS CRÍTICOS DE ATENÇÃO

### 1. DIFERENÇAS ARQUITETURAIS
- **Spigot**: Single-threaded, server-only
- **Forge**: Multi-threaded, client-server separation

### 2. SISTEMAS DE EVENTOS
- **Spigot**: Event-driven com @EventHandler
- **Forge**: Event bus com @SubscribeEvent

### 3. PERSISTÊNCIA DE DADOS
- **Spigot**: Arquivos YAML/JSON simples
- **Forge**: Capability system + NBT + Database

### 4. NETWORKING
- **Spigot**: Não necessário (server-only)
- **Forge**: Packets obrigatórios para sincronização

## 🎯 CRITÉRIOS DE SUCESSO

### Funcionalidades Obrigatórias:
- ✅ Integração completa com MercadoPago API
- ✅ Geração de códigos PIX e QR codes
- ✅ Sistema de menus interativos
- ✅ Persistência de pedidos e transações
- ✅ Comandos administrativos e de usuário
- ✅ Sistema de configuração flexível
- ✅ Logs detalhados de transações
- ✅ Compatibilidade multi-versão (1.19-1.21)

### Métricas de Qualidade:
- **Performance**: Sem lag perceptível
- **Estabilidade**: Zero crashes em operações normais
- **Compatibilidade**: Funciona com outros mods populares
- **Usabilidade**: Interface intuitiva para jogadores
- **Manutenibilidade**: Código limpo e documentado
