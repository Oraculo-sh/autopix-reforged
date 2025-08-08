# AutoPix Reforged

**Sistema de pagamentos PIX integrado ao Minecraft Forge**

Porte do plugin AutoPix original (Spigot/Bukkit) para Minecraft Forge 1.19.2.

## 📋 Sobre

O AutoPix Reforged é um mod que permite que jogadores realizem pagamentos PIX diretamente no jogo, com integração completa ao MercadoPago e geração automática de QR codes.

### ✨ Recursos

- **Comandos integrados**: `/autopix` e `/autopixmenu`
- **Integração MercadoPago**: Validação automática de pagamentos
- **QR Codes**: Geração automática para facilitar pagamentos
- **Sistema de transações**: Gerenciamento completo de pedidos
- **Configuração flexível**: Personalização via arquivo de configuração
- **Suporte a MySQL**: Armazenamento persistente de dados
- **Interface amigável**: Menus intuitivos para compras

## 🚀 Instalação

1. Baixe a versão mais recente do mod
2. Coloque o arquivo `.jar` na pasta `mods` do seu servidor/cliente Forge
3. Configure o arquivo de configuração conforme necessário
4. Reinicie o servidor/cliente

## ⚙️ Configuração

O mod utiliza o sistema de configuração do Forge. As principais configurações incluem:

- **MercadoPago**: Token de acesso e configurações da API
- **PIX**: Chave PIX e dados do beneficiário
- **Banco de dados**: Configurações de conexão MySQL
- **Timeouts**: Tempos limite para validação e expiração
- **Interface**: Personalização de menus e mensagens

## 🎮 Comandos

- `/autopix <código>` - Valida um código PIX
- `/autopixmenu` - Abre o menu de compras
- `/pix <código>` - Alias para `/autopix`
- `/comprarpix` - Alias para `/autopixmenu`

## 🔧 Desenvolvimento

### Pré-requisitos

- Java 17+
- Minecraft Forge 1.19.2 (versão 43.5.0+)
- Gradle

### Compilação

```bash
./gradlew build
```

### Estrutura do Projeto

```
src/main/java/io/github/oraculo/autopix/
├── AutoPixMod.java           # Classe principal do mod
├── config/                   # Configurações
├── commands/                 # Comandos do mod
├── domain/                   # Entidades de domínio
├── events/                   # Manipuladores de eventos
├── manager/                  # Gerenciadores de sistema
├── network/                  # Comunicação cliente-servidor
└── utils/                    # Utilitários
```

## 📝 Créditos

- **Autor original**: [warleysr](https://github.com/warleysr) - Plugin AutoPix para Spigot/Bukkit
- **Porte para Forge**: [Leonne Martins (Oraculo-sh)](https://github.com/Oraculo-sh)

## 📄 Licença

Este projeto está licenciado sob a Licença GPL-3.0 - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 🤝 Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para:

1. Fazer fork do projeto
2. Criar uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abrir um Pull Request

## 📞 Suporte

Para suporte e dúvidas:

- Abra uma [issue](https://github.com/Oraculo-sh/autopix-reforged/issues)
- Entre em contato via GitHub

---

**Nota**: Este é um porte não oficial do plugin AutoPix original. Certifique-se de ter as devidas permissões e configurações do MercadoPago antes de usar em produção.
