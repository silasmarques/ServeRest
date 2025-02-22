Feature: Login no sistema

  Scenario: Login bem-sucedido
    Given que o usuário foi criado anteriormente
    When realizo login com esse usuário
    Then o status da resposta deve ser 200
