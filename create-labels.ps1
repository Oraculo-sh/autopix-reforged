# Script para criar labels do GitHub para o projeto AutoPix Reforged
# Execute com: .\create-labels.ps1

Write-Host "Criando labels para o repositorio AutoPix Reforged..." -ForegroundColor Green

# Funcao para criar label
function Create-Label {
    param(
        [string]$Name,
        [string]$Color,
        [string]$Description
    )
    
    try {
        gh label create "$Name" --color "$Color" --description "$Description" 2>$null
        if ($LASTEXITCODE -eq 0) {
            Write-Host "Label '$Name' criado com sucesso" -ForegroundColor Green
        } else {
            Write-Host "Label '$Name' ja existe ou erro na criacao" -ForegroundColor Yellow
        }
    } catch {
        Write-Host "Erro ao criar label '$Name': $_" -ForegroundColor Red
    }
}

# Priority Labels
Write-Host "Criando labels de Prioridade..." -ForegroundColor Cyan
Create-Label "priority: critical" "d73a4a" "Critico - Requer atencao imediata"
Create-Label "priority: high" "ff6b6b" "Alta prioridade"
Create-Label "priority: medium" "ffa726" "Prioridade media"
Create-Label "priority: low" "81c784" "Baixa prioridade"

# Type Labels
Write-Host "Criando labels de Tipo..." -ForegroundColor Cyan
Create-Label "type: bug" "d73a4a" "Algo nao esta funcionando"
Create-Label "type: feature" "a2eeef" "Nova funcionalidade ou solicitacao"
Create-Label "type: enhancement" "84b6eb" "Melhoria em funcionalidade existente"
Create-Label "type: documentation" "0075ca" "Melhorias ou adicoes a documentacao"
Create-Label "type: refactor" "fbca04" "Refatoracao de codigo"
Create-Label "type: performance" "ff9500" "Melhorias de performance"
Create-Label "type: security" "b60205" "Questoes de seguranca"
Create-Label "type: test" "1d76db" "Adicao ou correcao de testes"

# Status Labels
Write-Host "Criando labels de Status..." -ForegroundColor Cyan
Create-Label "status: needs-triage" "ededed" "Precisa ser analisado pela equipe"
Create-Label "status: in-progress" "0052cc" "Trabalho em andamento"
Create-Label "status: blocked" "b60205" "Bloqueado por dependencia externa"
Create-Label "status: needs-review" "fbca04" "Precisa de revisao"
Create-Label "status: ready-to-merge" "0e8a16" "Pronto para merge"
Create-Label "status: wontfix" "ffffff" "Nao sera corrigido"
Create-Label "status: duplicate" "cfd3d7" "Issue ou PR duplicado"
Create-Label "status: invalid" "e4e669" "Nao parece correto"

# Component Labels
Write-Host "Criando labels de Componente..." -ForegroundColor Cyan
Create-Label "component: commands" "1f77b4" "Sistema de comandos"
Create-Label "component: config" "ff7f0e" "Sistema de configuracao"
Create-Label "component: database" "2ca02c" "Banco de dados e persistencia"
Create-Label "component: network" "d62728" "Comunicacao de rede"
Create-Label "component: ui" "9467bd" "Interface do usuario"
Create-Label "component: api" "8c564b" "API e integracoes externas"
Create-Label "component: events" "e377c2" "Sistema de eventos"
Create-Label "component: utils" "7f7f7f" "Utilitarios e helpers"

# Difficulty Labels
Write-Host "Criando labels de Dificuldade..." -ForegroundColor Cyan
Create-Label "difficulty: beginner" "7057ff" "Bom para iniciantes"
Create-Label "difficulty: intermediate" "008672" "Requer conhecimento intermediario"
Create-Label "difficulty: advanced" "d93f0b" "Requer conhecimento avancado"
Create-Label "difficulty: expert" "0d1117" "Apenas para experts"

# Version Labels
Write-Host "Criando labels de Versao..." -ForegroundColor Cyan
Create-Label "version: 1.19.2" "0366d6" "Minecraft 1.19.2"
Create-Label "version: 1.20.x" "0366d6" "Minecraft 1.20.x"
Create-Label "version: 1.21.x" "0366d6" "Minecraft 1.21.x"

# Special Labels
Write-Host "Criando labels Especiais..." -ForegroundColor Cyan
Create-Label "good first issue" "7057ff" "Bom para novos contribuidores"
Create-Label "help wanted" "008672" "Ajuda extra e bem-vinda"
Create-Label "breaking change" "d93f0b" "Mudanca que quebra compatibilidade"
Create-Label "dependencies" "0366d6" "Atualizacao de dependencias"
Create-Label "question" "d876e3" "Informacao adicional e solicitada"

# Platform Labels
Write-Host "Criando labels de Plataforma..." -ForegroundColor Cyan
Create-Label "platform: forge" "ff6b35" "Minecraft Forge"
Create-Label "platform: fabric" "f5f5dc" "Minecraft Fabric"
Create-Label "platform: neoforge" "00d4aa" "NeoForge"

# Review Labels
Write-Host "Criando labels de Review..." -ForegroundColor Cyan
Create-Label "review: approved" "0e8a16" "Aprovado pelos revisores"
Create-Label "review: changes-requested" "d93f0b" "Mudancas solicitadas"
Create-Label "review: needs-author" "fbca04" "Aguardando resposta do autor"

Write-Host "Processo de criacao de labels concluido!" -ForegroundColor Green
Write-Host "Verifique os labels criados em: https://github.com/Oraculo-sh/autopix-reforged/labels" -ForegroundColor Blue