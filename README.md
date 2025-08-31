# Pokedle - Adivinhe o Pokémon!

<p align="center">
  <img src="https://raw.githubusercontent.com/douglasAVsantos/pokedle/main/src/main/resources/static/pokedle-logo-pokemons.webp" alt="Pokedle Logo" width="600"/>
</p>

<p align="center">
  Um jogo web divertido e viciante, inspirado no popular Wordle, mas com um toque do universo Pokémon!
</p>

---

## 🎮 Sobre o Jogo

Pokedle é uma aplicação full-stack onde um Pokémon da primeira geração é sorteado a cada dia. O seu objetivo é adivinhar qual é o Pokémon secreto, recebendo dicas visuais a cada tentativa.

A cada palpite, o jogo informa se os atributos (Tipo, Peso, Altura, Cor, etc.) do Pokémon que você escolheu correspondem aos do Pokémon do dia.

## ✨ Funcionalidades

- **Pokémon Diário**: Um novo desafio a cada dia para testar seus conhecimentos.
- **Histórico Interativo**: Cada palpite é exibido em um card com feedback visual claro:
  - 🟩 **Verde** para acertos.
  - 🟥 **Vermelho** para erros.
  - 🔼🔽 **Setas** para indicar se o peso e a altura do Pokémon correto são maiores ou menores.
- **Seleção Inteligente**: Uma lista suspensa pesquisável com todos os 151 Pokémon, incluindo suas imagens, para facilitar a escolha e evitar erros de digitação.
- **Jogabilidade Fluida**: O palpite é enviado automaticamente assim que um Pokémon é selecionado, sem a necessidade de cliques extras.
- **Interface Moderna**: Layout responsivo e agradável com animações que tornam a experiência mais dinâmica e divertida.
- **Fim de Jogo**: Ao acertar o Pokémon, o jogo exibe uma tela de vitória e encerra a partida.

## 🛠️ Tecnologias Utilizadas

Este projeto foi construído com uma stack moderna, focando em boas práticas de desenvolvimento tanto no backend quanto no frontend.

#### **Backend**
- **Java 17**
- **Spring Boot 3**
- **Spring WebClient**: Para consumo assíncrono e reativo da PokéAPI.
- **Gradlew**: Para gerenciamento de dependências.

#### **Frontend**
- **HTML5 & CSS3**: Para estrutura e estilo.
- **Thymeleaf**: Para renderização dinâmica das páginas no lado do servidor.
- **JavaScript**: Para interatividade e lógica no cliente.
- **jQuery & Select2**: Para criar a lista suspensa pesquisável e customizada.

#### **API Externa**
- **[PokéAPI](https://pokeapi.co/)**: Fonte de todos os dados dos Pokémon.

## 🚀 Como Executar o Projeto Localmente

Siga os passos abaixo para ter o Pokedle rodando na sua máquina.

#### **Pré-requisitos**
- **Java JDK 17** ou superior.
- **Gradlew**.
- Um editor de código ou IDE de sua preferência (ex: IntelliJ IDEA, VS Code).

#### **Passos**

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/douglasAVsantos/pokedle.git
   ```

2. **Navegue até o diretório do projeto:**
   ```bash
   cd pokedle
   ```

3. **Execute a aplicação com o Maven:**
   O Maven irá baixar todas as dependências e iniciar o servidor web embutido.
   ```bash
   mvn spring-boot:run
   ```

4. **Abra no navegador:**
   Após a aplicação iniciar, abra seu navegador e acesse o seguinte endereço:
   ```
   http://localhost:8080
   ```

5. **Pronto!** Agora é só começar a jogar e tentar adivinhar o Pokémon do dia.

---

## 👨‍💻 Autor

Desenvolvido por **Douglas Augusto**.

[LinkedIn](https://www.linkedin.com/in/douglas-avs) | [GitHub](https://github.com/douglasAVsantos)