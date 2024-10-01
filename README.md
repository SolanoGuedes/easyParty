# EasyParty - Rede Social para Festas

## Descrição

O **EasyParty** é uma rede social inovadora projetada para conectar pessoas e melhorar o convívio social em eventos festivos. Com uma interface intuitiva, os usuários podem descobrir, criar e participar de festas na sua região, tornando cada celebração uma oportunidade de socialização.

## Funcionalidades

- **Eventos na Região**: Visualize os próximos eventos e as festas mais populares na sua área.
- **Criação de Eventos**: Qualquer usuário pode criar eventos, definindo se são públicos (visíveis para todos) ou privados (apenas para amigos).
- **Localização de Eventos**: Mostre a concentração de pessoas em diferentes locais, identificando onde podem ocorrer futuros eventos.
- **Check-in em Eventos**: Os usuários podem marcar presença em eventos, permitindo que amigos vejam onde estão.
- **Perfil Personalizado**: Cada usuário tem um perfil que exibe fotos dos eventos que participou e informações sobre suas atividades.
- **Sistema de Pontuação**: Os usuários ganham pontos por participar e organizar eventos, podendo trocá-los por recompensas.
- **Chat em Tempo Real**: Interaja com amigos e participantes dos eventos através de um sistema de chat desenvolvido em JavaScript com Node.js.

## Tecnologias

- **Backend**: Java Spring
- **Banco de Dados**: MySQL

## Funcionalidades
A classe **Usuario** é usada em diversas operações dentro do sistema, incluindo:

- **Cadastro de Usuário**: Permite a criação de novos usuários, garantindo que os dados atendam às validações.
- **Autenticação**: Após o login gera um token. A senha do usuário é criptografada antes de ser salva, garantindo a segurança.
- **Atualização de Dados**: Permite que os usuários atualizem suas informações pessoais, incluindo a senha, bio e outras.
- **Consulta de Usuário**: Possibilita a recuperação das informações do usuário com base no nome de usuário.
