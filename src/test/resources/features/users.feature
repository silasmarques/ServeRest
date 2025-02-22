Feature: Gerenciamento de Usuários

  Scenario: Criar um usuário único com sucesso
    Given que eu tenho os dados de um novo usuário único
    When eu envio uma requisição para criar um usuário único
#    Then a resposta deve retornar o status 201
    And a mensagem de sucesso deve conter "Cadastro realizado com sucesso"
