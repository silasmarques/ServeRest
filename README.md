# 🛠️ Testes Automatizados da API Serverest com RestAssured

## 📖 Descrição do Projeto
Este projeto é uma suíte de testes automatizados para a API Serverest, utilizando **Java**, **RestAssured** e JUnit 5. Ele valida funcionalidades como cadastro de usuários, login, gerenciamento de produtos, pedidos e carrinhos, garantindo a integridade dos endpoints. O **Allure** é integrado para gerar relatórios detalhados dos testes.
 
## Sumário

- [Requisitos](#requisitos)
- [Projeto](#projeto)
- [Relatório](#relatorios)

---

## Requisitos
Antes de rodar os testes, certifique-se de ter instalado:
- **Java 17** ou superior
- **Gradle**
- **IntelliJ IDEA** (ou outra IDE compatível)
- **Git** (para controle de versão)

## Projeto
```
git clone https://github.com/silasmarques/ServeRest.git

cd seu-repositorio
```
## Relatório 

### 📊 Relatório
O relatório pode ser acessado online no link abaixo:

👉 [Relatório de Testes](https://silasmarques.github.io/ServeRest/)

Caso o relatório não seja gerado automaticamente após a execução do Teste no Pages, 
executar comandos abaixo.
```
git checkout gh-pages
cp reports/report.html ./index.html
git add index.html
git commit --allow-empty -m "Forçando atualização do relatório"
git push origin gh-pages
```
👉 [Workflows](https://github.com/silasmarques/ServeRest/actions)