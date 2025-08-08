# 📦 Guia de Instalação - AutoPix Reforged

## 🎯 Pré-requisitos

### Software Necessário
- **Minecraft Forge 1.19.2** (versão 43.5.0 ou superior)
- **Java 17** ou superior
- **Servidor MySQL** (para armazenamento de dados)

### Contas e Serviços
- **Conta MercadoPago** (para processamento de pagamentos)
- **Chave PIX** configurada no MercadoPago

## 🚀 Instalação

### 1. Download do Mod

1. Acesse a página de [Releases](https://github.com/Oraculo-sh/autopix-reforged/releases)
2. Baixe a versão mais recente do arquivo `autopix-1.x.x.jar`

### 2. Instalação no Servidor

1. **Pare o servidor** Minecraft
2. **Copie o arquivo JAR** para a pasta `mods/` do servidor
3. **Inicie o servidor** uma vez para gerar os arquivos de configuração
4. **Pare o servidor** novamente para configurar

### 3. Instalação no Cliente (Opcional)

> ⚠️ **Nota**: O mod funciona apenas no servidor, mas pode ser instalado no cliente para melhor experiência.

1. Copie o arquivo JAR para a pasta `mods/` do Minecraft
2. Certifique-se de ter o Forge 1.19.2 instalado

## ⚙️ Configuração

### 1. Configuração do MercadoPago

#### Criando uma Aplicação
1. Acesse [MercadoPago Developers](https://www.mercadopago.com.br/developers/)
2. Faça login na sua conta
3. Vá em **"Suas integrações"** → **"Criar aplicação"**
4. Preencha os dados:
   - **Nome da aplicação**: AutoPix Minecraft
   - **Modelo de negócio**: Marketplace
   - **Produtos**: Checkout Pro, Checkout API
5. Clique em **"Criar aplicação"**

#### Obtendo o Access Token
1. Na página da aplicação, vá para **"Credenciais"**
2. Copie o **"Access Token de Produção"**
3. ⚠️ **IMPORTANTE**: Use apenas o token de produção para transações reais

### 2. Configuração da Chave PIX

1. No app do MercadoPago, vá em **"PIX"** → **"Minhas chaves"**
2. Copie uma das suas chaves PIX (CPF, email, telefone, etc.)
3. Esta chave será usada para gerar os QR codes

### 3. Configuração do Banco de Dados

#### Criando o Banco
```sql
CREATE DATABASE autopix CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'autopix'@'localhost' IDENTIFIED BY 'senha_segura';
GRANT ALL PRIVILEGES ON autopix.* TO 'autopix'@'localhost';
FLUSH PRIVILEGES;
```

### 4. Configuração do Mod

1. **Localize o arquivo de configuração**:
   - Servidor: `config/autopix-common.toml`
   - Cliente: `config/autopix-client.toml`

2. **Copie o arquivo de exemplo**:
   ```bash
   cp autopix-config-example.toml config/autopix-common.toml
   ```

3. **Edite a configuração**:
   ```toml
   [mercadopago]
       token = "APP_USR-1234567890abcdef-123456-abcdef1234567890abcdef1234567890-123456789"
   
   [pix]
       key = "seu.email@exemplo.com"
       beneficiary_name = "Seu Nome"
   
   [mysql]
       host = "localhost"
       port = 3306
       database = "autopix"
       username = "autopix"
       password = "senha_segura"
   ```

## 🧪 Teste da Instalação

### 1. Verificação Básica

1. **Inicie o servidor**
2. **Entre no jogo**
3. **Execute o comando**: `/autopixmenu`
4. **Verifique** se o menu abre corretamente

### 2. Teste de Pagamento (Sandbox)

> ⚠️ **Use apenas tokens de teste para desenvolvimento!**

1. Configure o token de teste no arquivo de configuração
2. Crie uma transação de teste
3. Verifique se o QR code é gerado
4. Use o app de teste do MercadoPago para simular o pagamento

## 🔧 Solução de Problemas

### Problemas Comuns

#### "Mod não carrega"
- ✅ Verifique se o Forge 1.19.2 está instalado
- ✅ Confirme a versão do Java (17+)
- ✅ Verifique os logs do servidor

#### "Erro de conexão com banco"
- ✅ Verifique se o MySQL está rodando
- ✅ Confirme as credenciais de acesso
- ✅ Teste a conexão manualmente

#### "Token inválido"
- ✅ Verifique se o token está correto
- ✅ Confirme se é o token de produção
- ✅ Verifique se a aplicação está ativa

#### "QR Code não aparece"
- ✅ Verifique se `use_map = true`
- ✅ Confirme se a chave PIX está correta
- ✅ Verifique os logs para erros

### Logs Importantes

#### Localização dos Logs
- **Servidor**: `logs/latest.log`
- **Cliente**: `.minecraft/logs/latest.log`

#### Filtros Úteis
```bash
# Logs do AutoPix
grep "AutoPix" logs/latest.log

# Erros de configuração
grep "ERROR.*autopix" logs/latest.log

# Transações
grep "Transaction" logs/latest.log
```

## 🔒 Segurança

### Boas Práticas

1. **Nunca compartilhe** seu Access Token
2. **Use HTTPS** sempre que possível
3. **Mantenha o mod atualizado**
4. **Faça backup** das configurações
5. **Monitore** as transações regularmente

### Configuração de Firewall

```bash
# Permitir conexões MySQL (se necessário)
sudo ufw allow 3306

# Permitir Minecraft
sudo ufw allow 25565
```

## 📞 Suporte

Se você encontrar problemas:

1. **Consulte este guia** primeiro
2. **Verifique os logs** para erros específicos
3. **Abra uma issue** no [GitHub](https://github.com/Oraculo-sh/autopix-reforged/issues)
4. **Inclua sempre**:
   - Versão do mod
   - Versão do Forge
   - Logs relevantes
   - Passos para reproduzir o problema

---

**✅ Instalação concluída!** Seu servidor agora está pronto para aceitar pagamentos PIX no Minecraft!