# 🤝 Guia de Contribuição - AutoPix Reforged

Obrigado por considerar contribuir para o AutoPix Reforged! Este documento fornece diretrizes para contribuir com o projeto.

## 📋 Índice

- [Código de Conduta](#código-de-conduta)
- [Como Posso Contribuir?](#como-posso-contribuir)
- [Configuração do Ambiente de Desenvolvimento](#configuração-do-ambiente-de-desenvolvimento)
- [Processo de Contribuição](#processo-de-contribuição)
- [Diretrizes de Código](#diretrizes-de-código)
- [Diretrizes de Commit](#diretrizes-de-commit)
- [Reportando Bugs](#reportando-bugs)
- [Sugerindo Melhorias](#sugerindo-melhorias)

## 📜 Código de Conduta

Este projeto e todos os participantes são regidos pelo [Código de Conduta](CODE_OF_CONDUCT.md). Ao participar, você concorda em manter este código.

## 🎯 Como Posso Contribuir?

### 🐛 Reportando Bugs

- Use o template de [Bug Report](.github/ISSUE_TEMPLATE/bug_report.md)
- Verifique se o bug já não foi reportado
- Inclua informações detalhadas sobre o ambiente
- Forneça passos claros para reproduzir o problema

### ✨ Sugerindo Melhorias

- Use o template de [Feature Request](.github/ISSUE_TEMPLATE/feature_request.md)
- Explique claramente o problema que a funcionalidade resolveria
- Descreva a solução proposta em detalhes

### 📚 Melhorando Documentação

- Use o template de [Documentation](.github/ISSUE_TEMPLATE/documentation.md)
- Corrija erros de digitação, gramática ou informações desatualizadas
- Adicione exemplos ou esclarecimentos
- Traduza documentação para outros idiomas

### 💻 Contribuindo com Código

- Corrija bugs existentes
- Implemente novas funcionalidades
- Melhore a performance
- Adicione testes

## 🛠️ Configuração do Ambiente de Desenvolvimento

### Pré-requisitos

- **Java 17+** (recomendado: OpenJDK 17)
- **Git**
- **IDE** (recomendado: IntelliJ IDEA ou Eclipse)

### Configuração

1. **Fork o repositório**
   ```bash
   # Clone seu fork
   git clone https://github.com/SEU_USERNAME/autopix-reforged.git
   cd autopix-reforged
   
   # Adicione o repositório original como upstream
   git remote add upstream https://github.com/Oraculo-sh/autopix-reforged.git
   ```

2. **Configure o ambiente**
   ```bash
   # Torne o gradlew executável (Linux/macOS)
   chmod +x gradlew
   
   # Compile o projeto
   ./gradlew build
   
   # Execute testes
   ./gradlew test
   ```

3. **Configure sua IDE**
   - Importe o projeto como um projeto Gradle
   - Configure o JDK 17
   - Instale plugins recomendados (se usando IntelliJ):
     - Minecraft Development
     - Gradle

## 🔄 Processo de Contribuição

### 1. Preparação

```bash
# Sincronize com o repositório upstream
git fetch upstream
git checkout main
git merge upstream/main

# Crie uma nova branch para sua contribuição
git checkout -b feature/nome-da-funcionalidade
# ou
git checkout -b fix/nome-do-bug
```

### 2. Desenvolvimento

- Faça suas alterações
- Teste localmente
- Adicione/atualize testes se necessário
- Atualize documentação se necessário

### 3. Commit

```bash
# Adicione arquivos modificados
git add .

# Faça commit seguindo as diretrizes
git commit -m "feat: adiciona nova funcionalidade X"
```

### 4. Push e Pull Request

```bash
# Envie para seu fork
git push origin feature/nome-da-funcionalidade

# Crie um Pull Request no GitHub
```

## 📝 Diretrizes de Código

### Estilo de Código

- **Indentação**: 4 espaços (não tabs)
- **Encoding**: UTF-8
- **Line endings**: LF (Unix-style)
- **Máximo de caracteres por linha**: 120

### Convenções de Nomenclatura

```java
// Classes: PascalCase
public class TransactionManager {}

// Métodos e variáveis: camelCase
public void createTransaction() {}
private String playerName;

// Constantes: UPPER_SNAKE_CASE
public static final String DEFAULT_CONFIG = "autopix.toml";

// Packages: lowercase com pontos
package io.github.oraculo.autopix.manager;
```

### Estrutura de Arquivos

```
src/main/java/io/github/oraculo/autopix/
├── AutoPixMod.java              # Classe principal do mod
├── commands/                    # Comandos do mod
├── config/                      # Sistema de configuração
├── domain/                      # Entidades de domínio
├── events/                      # Manipuladores de eventos
├── manager/                     # Gerenciadores de sistema
├── network/                     # Comunicação de rede
└── utils/                       # Utilitários
```

### Documentação de Código

```java
/**
 * Gerencia transações PIX no sistema.
 * 
 * @author Seu Nome
 * @since 1.0.0
 */
public class TransactionManager {
    
    /**
     * Cria uma nova transação PIX.
     * 
     * @param playerId ID do jogador
     * @param amount Valor da transação
     * @return A transação criada
     * @throws IllegalArgumentException se o valor for inválido
     */
    public PixTransaction createTransaction(UUID playerId, double amount) {
        // implementação
    }
}
```

## 📋 Diretrizes de Commit

### Formato

```
tipo(escopo): descrição curta

Descrição mais detalhada se necessário.

Fixes #123
```

### Tipos de Commit

- **feat**: Nova funcionalidade
- **fix**: Correção de bug
- **docs**: Mudanças na documentação
- **style**: Mudanças de formatação (não afetam funcionalidade)
- **refactor**: Refatoração de código
- **perf**: Melhoria de performance
- **test**: Adição ou correção de testes
- **chore**: Tarefas de manutenção
- **ci**: Mudanças no CI/CD

### Exemplos

```bash
# Boa
git commit -m "feat(commands): adiciona comando /autopix balance"
git commit -m "fix(database): corrige conexão MySQL timeout"
git commit -m "docs(readme): atualiza instruções de instalação"

# Ruim
git commit -m "mudanças"
git commit -m "fix bug"
git commit -m "WIP"
```

## 🐛 Reportando Bugs

### Antes de Reportar

1. **Verifique issues existentes**
2. **Teste com a versão mais recente**
3. **Reproduza o bug consistentemente**
4. **Colete informações do ambiente**

### Informações Necessárias

- Versão do AutoPix Reforged
- Versão do Minecraft
- Versão do Forge
- Sistema operacional
- Logs relevantes
- Passos para reproduzir
- Comportamento esperado vs atual

## ✨ Sugerindo Melhorias

### Diretrizes

- **Seja específico** sobre o problema que a funcionalidade resolve
- **Descreva a solução** em detalhes
- **Considere alternativas** e suas implicações
- **Pense na compatibilidade** com versões existentes
- **Avalie o impacto** na performance

## 🧪 Testes

### Executando Testes

```bash
# Todos os testes
./gradlew test

# Testes específicos
./gradlew test --tests "*TransactionManagerTest*"

# Com relatórios detalhados
./gradlew test --info
```

### Escrevendo Testes

```java
@Test
public void testCreateTransaction() {
    // Arrange
    UUID playerId = UUID.randomUUID();
    double amount = 10.0;
    
    // Act
    PixTransaction transaction = manager.createTransaction(playerId, amount);
    
    // Assert
    assertNotNull(transaction);
    assertEquals(playerId, transaction.getPlayerId());
    assertEquals(amount, transaction.getAmount());
}
```

## 📞 Suporte

- **Issues**: Para bugs e feature requests
- **Discussions**: Para perguntas gerais
- **Discord**: [Link do servidor] (se disponível)
- **Email**: oraculo.sh@gmail.com

## 📄 Licença

Ao contribuir, você concorda que suas contribuições serão licenciadas sob a mesma licença do projeto.

---

**Obrigado por contribuir! 🎉**