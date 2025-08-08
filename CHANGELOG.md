# Changelog

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e este projeto adere ao [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [Unreleased]

### Planejado
- Suporte para Minecraft 1.20.x
- Suporte para Minecraft 1.21.x
- Integração com Fabric (futuro)
- Sistema de notificações Discord
- API para outros mods

## [v1.0.0-beta.1] - 2024-12-XX

### Adicionado
- ✨ **Primeira versão beta do AutoPix Reforged**
- 🎮 **Sistema completo de comandos**:
  - `/autopix` - Comando principal com subcomandos
  - `/autopixmenu` - Interface gráfica (aliases: `/pixmenu`, `/apixmenu`)
- 💳 **Sistema de transações PIX**:
  - Criação e gerenciamento de transações
  - Validação automática via MercadoPago
  - Sistema de expiração configurável
  - Cleanup automático de transações antigas
- 🔧 **Sistema de configuração flexível**:
  - Arquivo TOML para configuração
  - Configuração de timeouts personalizáveis
  - Configuração de mensagens customizáveis
  - Configuração de sons e efeitos
- 🗄️ **Integração com banco de dados**:
  - Suporte completo ao MySQL
  - Persistência de transações
  - Sistema de conexão robusto
- 📱 **Geração de QR codes**:
  - QR codes automáticos para pagamentos PIX
  - Integração com biblioteca ZXing
- 🎨 **Interface de usuário**:
  - Sistema de mensagens coloridas
  - Menus interativos
  - Feedback visual para ações
- 🔗 **Integração MercadoPago**:
  - API completa para pagamentos
  - Validação automática de transações
  - Webhook support (preparado)
- 🎵 **Sistema de sons**:
  - Sons configuráveis para eventos
  - Feedback auditivo para ações
- 📊 **Sistema de ranking**:
  - Top doadores configurável
  - Estatísticas de pagamentos
- 📚 **Documentação completa**:
  - README detalhado
  - Guia de instalação
  - Configuração de exemplo
  - Documentação de API

### Técnico
- 🏗️ **Arquitetura modular**:
  - Separação clara de responsabilidades
  - Padrões de design bem definidos
  - Código limpo e documentado
- 🧪 **Infraestrutura de desenvolvimento**:
  - Templates para issues e PRs
  - Workflows de CI/CD
  - Sistema de labels organizados
  - Código de conduta e guias de contribuição
- 📦 **Dependências**:
  - OkHttp para comunicação HTTP
  - Gson para serialização JSON
  - MySQL Connector para banco de dados
  - ZXing para geração de QR codes
  - Apache Commons Lang para utilitários

### Compatibilidade
- ✅ **Minecraft 1.19.2**
- ✅ **Forge 43.5.0+**
- ✅ **Java 17+**
- ✅ **MySQL 5.7+**

### Notas de Migração
- 📋 **Primeira versão**: Não há migração necessária
- ⚠️ **Versão Beta**: Use apenas em ambiente de teste
- 🔧 **Configuração**: Siga o [Guia de Instalação](INSTALLATION.md)

---

## Formato das Entradas

### Tipos de Mudanças
- `Adicionado` para novas funcionalidades
- `Alterado` para mudanças em funcionalidades existentes
- `Descontinuado` para funcionalidades que serão removidas
- `Removido` para funcionalidades removidas
- `Corrigido` para correções de bugs
- `Segurança` para vulnerabilidades

### Categorias
- 🎮 **Gameplay** - Funcionalidades que afetam a experiência do jogador
- 🔧 **Configuração** - Sistema de configuração e personalização
- 🗄️ **Banco de Dados** - Persistência e armazenamento
- 📱 **Integração** - APIs externas e integrações
- 🎨 **Interface** - UI/UX e experiência visual
- 🔗 **API** - Interface de programação para desenvolvedores
- 🏗️ **Arquitetura** - Mudanças estruturais no código
- 📚 **Documentação** - Documentação e guias
- 🧪 **Testes** - Testes e qualidade de código
- 🔒 **Segurança** - Segurança e vulnerabilidades

---

**Links:**
- [Unreleased]: https://github.com/Oraculo-sh/autopix-reforged/compare/v1.0.0-beta.1...HEAD
- [v1.0.0-beta.1]: https://github.com/Oraculo-sh/autopix-reforged/releases/tag/v1.0.0-beta.1